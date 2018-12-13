package com.jozufozu.exnihiloomnia.common.world

import com.google.common.collect.Maps
import com.google.gson.*
import com.jozufozu.exnihiloomnia.ExNihilo
import net.minecraft.block.Block
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.*
import net.minecraft.util.JsonUtils
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.TextComponentString
import net.minecraft.util.text.TextComponentTranslation
import net.minecraft.world.World
import java.io.File
import java.io.IOException
import java.io.PrintWriter
import java.time.LocalDateTime
import java.util.ArrayList
import kotlin.collections.HashMap

data class Placement(val state: IBlockState) {
    override fun equals(other: Any?): Boolean {
        (other as? Placement)?.let {
            return it.state.toString() == state.toString()
        } ?: return false
    }
    override fun hashCode(): Int {
        return state.toString().hashCode()
    }
}

class SpawnIsland {
    private var origin = BlockPos(0, 0, 0)

    private val palette = ArrayList<Placement>()
    private val structure = Maps.newHashMap<BlockPos, Int>()
    private val tiles = Maps.newHashMap<BlockPos, NBTTagCompound>()

    fun placeInWorld(world: World, origin: BlockPos) {
        val nanoStart = System.nanoTime()

        val xOff = origin.x - this.origin.x
        val yOff = origin.y - this.origin.y
        val zOff = origin.z - this.origin.z

        for ((i, pos) in this.structure.keys.withIndex()) {
            val placement = this.palette[i]
            val place = pos.add(xOff, yOff, zOff)
            world.setBlockState(place, placement.state, 2)
        }

        for ((pos, tile) in tiles.entries) {
            val pos = pos.add(xOff, yOff, zOff)
            val tileEntity = world.getTileEntity(pos)

            if (tileEntity != null) {
                tile.setInteger("x", pos.x)
                tile.setInteger("y", pos.y)
                tile.setInteger("z", pos.z)

                tileEntity.readFromNBT(tile)
                world.getBlockState(pos).let { world.notifyBlockUpdate(pos, it, it, 2) }
            }
        }

        val nanoNow = System.nanoTime()
        ExNihilo.log.info(String.format("Loaded spawn island into world. Took %.3f", (nanoNow - nanoStart).toDouble() * 1E-9))
    }

    fun save(user: EntityPlayer) {
        val writer: PrintWriter?
        try {
            val now = LocalDateTime.now()
            val name = user.displayNameString + "-" + now.year + now.monthValue + now.dayOfMonth + "-" + now.hour + now.minute + now.second + ".json"
            val file = File(ExNihilo.PATH, String.format("/spawn_island/%s", name))
            file.createNewFile()

            writer = PrintWriter(file)
            writer.append(serialize())

            writer.close()

            user.sendMessage(TextComponentTranslation("info.exnihiloomnia.save_island.saved", name))
        } catch (e: IOException) {
            user.sendMessage(TextComponentTranslation("info.exnihiloomnia.save_island.error", e.message))
        }
    }

    fun serialize(): String {
        val json = JsonObject()

        json.add("origin", serializeBlockPos(this.origin))

        val palette = JsonArray()
        val tiles = JsonArray()
        val structure = JsonArray()

        this.palette.forEach { palette.add(serializePlacement(it)) }
        this.tiles.forEach { pos, tile ->
            JsonObject().let {
                it.add("pos", serializeBlockPos(pos))
                it.add("tile", serializeNBT(tile))
                tiles.add(it)
            }
        }
        this.structure.forEach { pos, s ->
            JsonObject().let {
                it.add("pos", serializeBlockPos(pos))
                it.addProperty("id", s)
                structure.add(it)
            }
        }

        json.add("palette", palette)
        json.add("tiles", tiles)
        json.add("structure", structure)

        return GSON.toJson(json)
    }

    companion object {
        @JvmField val GSON: Gson = GsonBuilder().setLenient().create()

        fun createFromWorld(world: World, from: BlockPos, to: BlockPos, origin: BlockPos): SpawnIsland {
            val nanoStart = System.nanoTime()
            val startX = Math.min(from.x, to.x)
            val startY = Math.min(from.y, to.y)
            val startZ = Math.min(from.z, to.z)

            val sizeX = Math.abs(from.x - to.x)
            val sizeY = Math.abs(from.y - to.y)
            val sizeZ = Math.abs(from.z - to.z)

            val process = HashMap<Placement, MutableList<BlockPos>>()
            val tiles = HashMap<BlockPos, NBTTagCompound>()

            val read = BlockPos.MutableBlockPos()

            for (y in startY until startY + sizeY) {
                for (x in startX until startX + sizeX) {
                    for (z in startZ until startZ + sizeZ) {
                        read.setPos(x, y, z)

                        if (world.isAirBlock(read))
                            continue

                        val state = world.getBlockState(read)
                        val relative = read.toImmutable()

                        if (state.block.hasTileEntity(state)) {
                            val tileEntity = world.getTileEntity(read)

                            if (tileEntity != null) {
                                val tileData = tileEntity.serializeNBT()
                                tileData.removeTag("id")
                                tileData.removeTag("x")
                                tileData.removeTag("y")
                                tileData.removeTag("z")

                                tiles[relative] = tileData
                            }
                        }

                        val placement = Placement(state)

                        process.computeIfAbsent(placement) { ArrayList() }.add(relative)
                    }
                }
            }

            val out = SpawnIsland()

            var minX = Integer.MAX_VALUE
            var minY = Integer.MAX_VALUE
            var minZ = Integer.MAX_VALUE

            process.values.forEach {
                it.forEach { pos ->
                    minX = Math.min(minX, pos.x)
                    minY = Math.min(minY, pos.y)
                    minZ = Math.min(minZ, pos.z)
                }
            }

            out.origin = origin.add(-minX, -minY, -minZ)

            var i = 0
            for ((placement, locations) in process.entries) {
                out.palette[i] = placement

                for (pos in locations) out.structure[pos.add(-minX, -minY, -minZ)] = i
                i++
            }

            val nanoNow = System.nanoTime()

            val seconds = (nanoNow - nanoStart).toDouble() * 1E-9

            for (player in world.playerEntities) {
                player.sendMessage(TextComponentString(String.format("Made spawn island. Took %.3f", seconds)))
            }

            return out
        }

        fun serializeBlockPos(pos: BlockPos): JsonArray {
            val array = JsonArray()
            array.add(pos.x)
            array.add(pos.y)
            array.add(pos.z)
            return array
        }

        fun serializePlacement(placement: Placement): JsonObject {
            val json = JsonObject()

            val block = placement.state.block
            json.addProperty("id", Block.REGISTRY.getNameForObject(block).toString())
            json.addProperty("data", block.getMetaFromState(placement.state))

            return json
        }

        fun serializeNBT(nbt: NBTBase): JsonElement {
            return when (nbt) {
                is NBTTagCompound -> JsonObject().let { for (s in nbt.keySet) it.add(s, serializeNBT(nbt.getTag(s))); it }
                is NBTTagList -> JsonArray().let { for (nbtBase in nbt) it.add(serializeNBT(nbtBase)); it }
                is NBTTagString -> JsonPrimitive(nbt.string)
                is NBTTagInt -> JsonPrimitive(nbt.int)
                is NBTTagByte -> JsonPrimitive(nbt.byte)
                is NBTTagFloat -> JsonPrimitive(nbt.float)
                is NBTTagLong -> JsonPrimitive(nbt.long)
                is NBTTagShort -> JsonPrimitive(nbt.short)
                is NBTTagDouble -> JsonPrimitive(nbt.double)
                else -> JsonNull.INSTANCE
            }
        }

        @Throws(JsonSyntaxException::class)
        fun deserialize(json: JsonObject): SpawnIsland {
            val out = SpawnIsland()

            if (!json.has("palette")) {
                throw JsonSyntaxException("Structure json needs a palette!")
            }

            for ((key, value) in JsonUtils.getJsonObject(json, "p").entrySet()) {
                val state = value.asJsonObject
                out.palette[key.toInt()] = deserializePlacement(state)
            }

            if (!json.has("structure")) {
                throw JsonSyntaxException("Structure json needs a structure!")
            }

            for (place in JsonUtils.getJsonArray(json, "s")) {
                try {
                    if (!place.isJsonObject) {
                        throw JsonSyntaxException("Invalid structure entry: " + place.toString())
                    }

                    val block = place.asJsonObject

                    if (!block.has("block")) {
                        throw JsonSyntaxException("Structure json block info has no block!")
                    }

                    val paletteName = JsonUtils.getInt(block, "block")

                    val pos = deserializeBlockPos(block.getAsJsonArray("pos"))

                    out.structure[pos] = paletteName
                } catch (e: JsonParseException) {
                    ExNihilo.log.warn(e.message)
                }
            }

            if (json.has("origin")) {
                out.origin = deserializeBlockPos(json.getAsJsonArray("origin"))
            }

            return out
        }

        @Throws(JsonSyntaxException::class)
        private fun deserializePlacement(placement: JsonObject): Placement {
            val string = JsonUtils.getString(placement, "n")

            val name = ResourceLocation(string)

            if (!Block.REGISTRY.containsKey(name)) {
                throw JsonSyntaxException("Unknown block type '$name'")
            }

            val block = Block.REGISTRY.getObject(name)

            val state = if (placement.has("d")) {
                block.getStateFromMeta(JsonUtils.getInt(placement, "d"))
            } else {
                block.defaultState
            }

            return Placement(state)
        }

        @Throws(JsonParseException::class)
        private fun deserializeBlockPos(pos: JsonArray): BlockPos {
            if (pos.size() != 3) {
                throw JsonSyntaxException("BlockPos needs to have x, y and z!")
            }

            val x = pos.get(0).asJsonPrimitive.asInt
            val y = pos.get(1).asJsonPrimitive.asInt
            val z = pos.get(2).asJsonPrimitive.asInt

            return BlockPos(x, y, z)
        }
    }
}
