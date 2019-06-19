package com.jozufozu.exnihiloomnia.common.blocks

import com.jozufozu.exnihiloomnia.ExNihilo
import com.jozufozu.exnihiloomnia.common.blocks.sieve.SieveTileEntity
import com.mojang.datafixers.DataFixUtils
import com.mojang.datafixers.types.Type
import net.minecraft.tileentity.TileEntity
import net.minecraft.tileentity.TileEntityType
import net.minecraft.util.ResourceLocation
import net.minecraft.util.SharedConstants
import net.minecraft.util.datafix.DataFixesManager
import net.minecraft.util.datafix.TypeReferences
import net.minecraftforge.event.RegistryEvent
import java.util.function.Supplier

object ExNihiloTileEntities {
    private val tileEntities: ArrayList<TileEntityType<*>> = ArrayList()

    val SIEVE = register(ResourceLocation(), TileEntityType.Builder.create<SieveTileEntity>( Supplier { SieveTileEntity() }, ExNihiloBlocks.OAK_SIEVE, ExNihiloBlocks.SPRUCE_SIEVE, ExNihiloBlocks.BIRCH_SIEVE, ExNihiloBlocks.JUNGLE_SIEVE, ExNihiloBlocks.ACACIA_SIEVE, ExNihiloBlocks.DARK_OAK_SIEVE,))

    fun registerBlocks(event: RegistryEvent.Register<TileEntityType<*>>) {
        tileEntities.forEach(event.registry::register)
    }

    private fun <T : TileEntity> register(key: ResourceLocation, builder: TileEntityType.Builder<T>): TileEntityType<T> {
        var type: Type<*>? = null

        try {
            type = DataFixesManager.getDataFixer().getSchema(DataFixUtils.makeKey(SharedConstants.getVersion().worldVersion)).getChoiceType(TypeReferences.BLOCK_ENTITY, key)
        } catch (illegalstateexception: IllegalArgumentException) {
            if (SharedConstants.developmentMode) {
                throw illegalstateexception
            }

            ExNihilo.log.warn("No data fixer registered for block entity $key")
        }

//        if (builder.blocks.isEmpty()) {
//            ExNihilo.log.warn("Block entity type {} requires at least one valid block to be defined!", key as Any)
//        }

        return builder.build(type).also { it.registryName = key }
    }
}