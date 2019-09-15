package com.jozufozu.exnihiloomnia.common.world

import com.google.gson.*
import com.jozufozu.exnihiloomnia.ExNihilo
import com.jozufozu.exnihiloomnia.common.util.contains
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
import java.util.*
import kotlin.collections.HashMap
import kotlin.math.max
import kotlin.math.min

data class Placement(val state: IBlockState) {
    fun serialize(): JsonObject {
        val json = JsonObject()

        val block = state.block
        json.addProperty("id", Block.REGISTRY.getNameForObject(block).toString())

        if (state.properties.isNotEmpty()) {
            json.addProperty("variant", block.blockState.validStates.indexOf(state))
        }

        return json
    }

    override fun equals(other: Any?): Boolean {
        (other as? Placement)?.let {
            return it.state.toString() == state.toString()
        } ?: return false
    }
    override fun hashCode() = state.hashCode()

    companion object {
        fun deserialize(placement: JsonObject): Placement {
            val blockName = JsonUtils.getString(placement, "id")

            val block = Block.REGISTRY.getObject(ResourceLocation(blockName))

            return Placement(if ("variant" in placement) block.blockState.validStates[JsonUtils.getInt(placement, "variant")] else block.defaultState)
        }
    }
}

class SpawnIsland {

    private val palette = ArrayList<Placement>()
    private val structure = hashMapOf<BlockPos, Int>()
    private val tiles = hashMapOf<BlockPos, NBTTagCompound>()

    fun placeInWorld(world: World, origin: BlockPos) {
        val nanoStart = System.nanoTime()

        for ((pos, paletteId) in this.structure.entries) {
            val placement = this.palette[paletteId]
            val place = origin.add(pos)
            world.setBlockState(place, placement.state, 2)
        }

        for ((pos, tile) in tiles.entries) {
            val place = origin.add(pos)
            val tileEntity = world.getTileEntity(place)

            if (tileEntity != null) {
                tile.setInteger("x", place.x)
                tile.setInteger("y", place.y)
                tile.setInteger("z", place.z)

                tileEntity.readFromNBT(tile)
                world.getBlockState(place).let { world.notifyBlockUpdate(place, it, it, 2) }
            }
        }

        val nanoNow = System.nanoTime()
        ExNihilo.log.info(String.format("Loaded spawn island into world. Took %.3f", (nanoNow - nanoStart).toDouble() * 1E-9))
    }

    fun save(user: EntityPlayer) {
        var writer: PrintWriter? = null
        try {
            val now = LocalDateTime.now()
            val name = user.displayNameString + "-" + now.year + now.monthValue + now.dayOfMonth + "-" + now.hour + now.minute + now.second + ".json"
            val root = File(ExNihilo.PATH, "/spawn_island")
            root.mkdirs()

            val file = File(root, name)
            file.createNewFile()

            writer = PrintWriter(file)
            writer.append(serialize())

            user.sendMessage(TextComponentTranslation("info.exnihiloomnia.save_island.saved", name))
        } catch (e: IOException) {
            user.sendMessage(TextComponentTranslation("info.exnihiloomnia.save_island.error", e.message))
        } finally {
            writer?.close()
        }
    }

    fun serialize(): String {
        val json = JsonObject()

        val palette = JsonArray()
        val structure = JsonArray()

        this.palette.forEach { palette.add(it.serialize()) }

        if (this.tiles.isNotEmpty()) {
            val tiles = JsonArray()
            this.tiles.forEach { (pos, tile) ->
                tiles.add(JsonObject().also {
                    it.add("pos", serializeBlockPos(pos))
                    it.add("tile", serializeNBT(tile))
                })
            }
            json.add("tiles", tiles)
        }
        this.structure.forEach { (pos, s) ->
            structure.add(JsonObject().also {
                it.add("pos", serializeBlockPos(pos))
                it.addProperty("id", s)
            })
        }

        json.add("palette", palette)
        json.add("structure", structure)

        return GSON.toJson(json)
    }

    companion object {
        @JvmField val GSON: Gson = GsonBuilder().setLenient().create()

        fun createFromWorld(world: World, from: BlockPos, to: BlockPos, origin: BlockPos): SpawnIsland {
            val nanoStart = System.nanoTime()
            val startX = min(from.x, to.x)
            val startY = min(from.y, to.y)
            val startZ = min(from.z, to.z)

            val endX = max(from.x, to.x)
            val endY = max(from.y, to.y)
            val endZ = max(from.z, to.z)

            val stateLocations = HashMap<Placement, MutableList<BlockPos>>()

            val read = BlockPos.MutableBlockPos()

            val out = SpawnIsland()

            for (y in startY until endY) {
                for (x in startX until endX) {
                    for (z in startZ until endZ) {
                        read.setPos(x, y, z)

                        if (world.isAirBlock(read))
                            continue

                        val state = world.getBlockState(read)
                        val immutable = read.subtract(origin)

                        stateLocations.getOrPut(Placement(state)) { ArrayList() }.add(immutable)

                        if (state.block.hasTileEntity(state)) {
                            val tileEntity = world.getTileEntity(read)

                            if (tileEntity != null) {
                                val tileData = tileEntity.serializeNBT()
                                tileData.removeTag("id")
                                tileData.removeTag("x")
                                tileData.removeTag("y")
                                tileData.removeTag("z")

                                out.tiles[immutable] = tileData
                            }
                        }
                    }
                }
            }

            for ((i, entry) in stateLocations.entries.withIndex()) {
                out.palette.add(entry.key)

                for (pos in entry.value) out.structure[pos] = i
            }

            val nanoNow = System.nanoTime()

            val seconds = (nanoNow - nanoStart).toDouble() * 1E-9

            for (player in world.playerEntities) {
                player.sendMessage(TextComponentString(String.format("Made spawn island. Took %.3f seconds", seconds)))
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

            for (value in JsonUtils.getJsonArray(json, "palette")) {
                val state = value.asJsonObject
                out.palette.add(Placement.deserialize(state))
            }

            if ("tiles" in json) {
                for (tile in JsonUtils.getJsonArray(json, "tiles")) {
                    try {
                        if (tile !is JsonObject) {
                            throw JsonSyntaxException("Invalid tile entity: $tile")
                        }

                        val pos = deserializeBlockPos(JsonUtils.getJsonArray(tile, "pos"))

                        out.tiles[pos] = JsonToNBT.getTagFromJson(tile["tile"].toString())
                    } catch (e: JsonParseException) {
                        ExNihilo.log.warn(e.message)
                    }
                }
            }

            for (place in JsonUtils.getJsonArray(json, "structure")) {
                try {
                    if (place !is JsonObject) {
                        throw JsonSyntaxException("Invalid structure entry: $place")
                    }

                    if (!place.has("id")) {
                        throw JsonSyntaxException("Structure json block info has no block!")
                    }

                    val paletteName = JsonUtils.getInt(place, "id")

                    val pos = deserializeBlockPos(JsonUtils.getJsonArray(place, "pos"))

                    out.structure[pos] = paletteName
                } catch (e: JsonParseException) {
                    ExNihilo.log.warn(e.message)
                }
            }

            return out
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
