
package com.jozufozu.exnihiloomnia.common.blocks

import com.jozufozu.exnihiloomnia.ExNihilo
import com.jozufozu.exnihiloomnia.common.blocks.barrel.BarrelBlock
import com.jozufozu.exnihiloomnia.common.blocks.crucible.CrucibleTileEntity
import com.jozufozu.exnihiloomnia.common.blocks.sieve.SieveTileEntity
import com.mojang.datafixers.DataFixUtils
import com.mojang.datafixers.types.Type
import net.minecraft.tileentity.BarrelTileEntity
import net.minecraft.tileentity.TileEntity
import net.minecraft.tileentity.TileEntityType
import net.minecraft.util.ResourceLocation
import net.minecraft.util.SharedConstants
import net.minecraft.util.datafix.DataFixesManager
import net.minecraft.util.datafix.TypeReferences
import net.minecraftforge.event.RegistryEvent

object ExNihiloTileEntities {
    private val tileEntities: ArrayList<TileEntityType<*>> = ArrayList()

    val SIEVE = register(ResourceLocation(ExNihilo.MODID, "sieve"), TileEntityType.Builder.create<SieveTileEntity>( { SieveTileEntity() }, ExNihiloBlocks.OAK_SIEVE, ExNihiloBlocks.SPRUCE_SIEVE, ExNihiloBlocks.BIRCH_SIEVE, ExNihiloBlocks.JUNGLE_SIEVE, ExNihiloBlocks.ACACIA_SIEVE, ExNihiloBlocks.DARK_OAK_SIEVE))
    val BARREL = register(ResourceLocation(ExNihilo.MODID, "barrel"), TileEntityType.Builder.create<BarrelTileEntity>( { BarrelTileEntity() }, *BarrelBlock.INSTANCES.toTypedArray()))
    val CRUCIBLE = register(ResourceLocation(ExNihilo.MODID, "crucible"), TileEntityType.Builder.create<CrucibleTileEntity>( { CrucibleTileEntity() }, ExNihiloBlocks.CRUCIBLE))

    fun register(event: RegistryEvent.Register<TileEntityType<*>>) {
        tileEntities.forEach(event.registry::register)
    }

    private fun <T : TileEntity> register(key: ResourceLocation, builder: TileEntityType.Builder<T>): TileEntityType<T> {
        var type: Type<*>? = null

        try {
            type = DataFixesManager.getDataFixer().getSchema(DataFixUtils.makeKey(SharedConstants.getVersion().worldVersion)).getChoiceType(TypeReferences.BLOCK_ENTITY, key.path)
        } catch (illegalstateexception: IllegalArgumentException) {
            if (SharedConstants.developmentMode) {
                throw illegalstateexception
            }

            ExNihilo.log.warn("No data fixer registered for block entity $key")
        }

        return builder.build(type).also { it.registryName = key }
    }
}