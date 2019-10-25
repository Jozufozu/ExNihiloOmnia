package com.jozufozu.exnihiloomnia.common.registries.recipes

import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.states.BarrelStateSummoning
import com.jozufozu.exnihiloomnia.common.lib.LibRegistries
import com.jozufozu.exnihiloomnia.common.registries.JsonHelper
import com.jozufozu.exnihiloomnia.common.registries.RegistryLoader
import com.jozufozu.exnihiloomnia.common.util.Color
import com.jozufozu.exnihiloomnia.common.util.contains
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.Ingredient
import net.minecraft.nbt.JsonToNBT
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.JSONUtils
import net.minecraft.util.ResourceLocation
import net.minecraft.util.SoundEvent
import net.minecraftforge.common.crafting.CraftingHelper
import net.minecraftforge.common.util.Constants
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fml.common.registry.ForgeRegistries
import net.minecraftforge.registries.IForgeRegistryEntry

class SummoningRecipe(
        val fluid: FluidStack,
        val catalyst: Ingredient,
        val time: Int,
        val color: Color,
        val entityNBT: NBTTagCompound,
        val summonSound: SoundEvent?
) : IForgeRegistryEntry.Impl<SummoningRecipe>() {

    val barrelState by lazy {
        val name = ResourceLocation(registryName!!.resourceDomain, "fermenting_${registryName!!.resourcePath}")
        BarrelStateSummoning(this, name)
    }

    fun matches(catalyst: ItemStack, fluid: FluidStack): Boolean {
        return this.catalyst.apply(catalyst) && this.fluid.isFluidEqual(fluid)
    }

    companion object Serde {

        @Throws(JsonParseException::class)
        @JvmStatic fun deserialize(recipe: JsonObject): SummoningRecipe? {
            if (LibRegistries.FLUID_INPUT !in recipe) throw JsonSyntaxException("summoning recipe is missing \"${LibRegistries.FLUID_INPUT}\"")
            if (LibRegistries.INPUT !in recipe) throw JsonSyntaxException("summoning recipe is missing \"${LibRegistries.INPUT}\"")
            if ("entity" !in recipe) throw JsonSyntaxException("summoning recipe is missing \"entity\"")

            if (!CraftingHelper.processConditions(recipe, LibRegistries.CONDITIONS, RegistryLoader.CONTEXT)) return null

            val item = CraftingHelper.getIngredient(JSONUtils.getJsonObject(recipe, LibRegistries.INPUT), RegistryLoader.CONTEXT)

            val time = JSONUtils.getInt(recipe, LibRegistries.TIME, 400)
            val color = JsonHelper.deserializeColor(JSONUtils.getString(recipe, LibRegistries.COLOR, "ffffff"))

            RegistryLoader.pushPopCtx(LibRegistries.FLUID_INPUT)
            val fluid = JsonHelper.deserializeFluid(recipe[LibRegistries.FLUID_INPUT])

            RegistryLoader.pushPopCtx("entity")
            val entityTag = JsonToNBT.getTagFromJson(JSONUtils.getJsonObject(recipe, "entity").toString())

            if (!entityTag.hasKey("id", Constants.NBT.TAG_STRING)) throw JsonSyntaxException("entity must have \"id\". e.g. 'minecraft:creeper'")

            val id = entityTag.getString("id")
            ForgeRegistries.ENTITIES.getValue(ResourceLocation(id)) ?: throw JsonSyntaxException("entity has invalid \"id\": '$id'")

            entityTag.removeTag("Pos")
            entityTag.removeTag("UUID")

            val soundName: String? = JSONUtils.getString(recipe, "sound", null)
            val summonSound = if (soundName == null) {
                null
            } else {
                val sound = SoundEvent.REGISTRY.getObject(ResourceLocation(soundName))
                if (sound == null) {
                    RegistryLoader.warn("'$sound' is not a valid sound, ignoring")
                    null
                } else {
                    sound
                }
            }

            return SummoningRecipe(fluid, item, time, color, entityTag, summonSound)
        }
    }
}
