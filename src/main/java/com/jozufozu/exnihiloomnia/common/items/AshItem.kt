package com.jozufozu.exnihiloomnia.common.items

import com.jozufozu.exnihiloomnia.common.lib.ItemsLib
import net.minecraft.item.BoneMealItem
import net.minecraft.item.ItemUseContext
import net.minecraft.util.ActionResultType

class AshItem : ModItem(ItemsLib.ASH, Properties()) {

    override fun onItemUse(context: ItemUseContext): ActionResultType {
        if (BoneMealItem.applyBonemeal(context.item, context.world, context.pos, context.player!!)) {
            if (!context.world.isRemote) {
                context.world.playEvent(2005, context.pos, 0)
            }
        }
        return super.onItemUse(context)
    }
}
