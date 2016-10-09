package exnihiloomnia.compatibility.tconstruct.modifiers;

import exnihiloomnia.registries.hammering.HammerRegistry;
import exnihiloomnia.util.Color;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent;
import slimeknights.tconstruct.library.modifiers.ModifierTrait;
import slimeknights.tconstruct.tools.item.Excavator;
import slimeknights.tconstruct.tools.item.Hammer;
import slimeknights.tconstruct.tools.item.Pickaxe;
import slimeknights.tconstruct.tools.item.Shovel;

/**
 * Modifier that makes tools behave like a hammer
 */
public class ModHammered extends ModifierTrait {

    public ModHammered() {
        super("hammered", new Color("55FFFF").toInt());
    }

    @Override
    public boolean canApplyCustom(ItemStack stack) {
        return stack.getItem() instanceof Pickaxe
                || stack.getItem() instanceof Hammer
                || stack.getItem() instanceof Shovel
                || stack.getItem() instanceof Excavator;
    }

    @Override
    public boolean canApplyTogether(Enchantment enchantment) {
        return enchantment != Enchantments.SILK_TOUCH;
    }

    @Override
    public void blockHarvestDrops(ItemStack tool, BlockEvent.HarvestDropsEvent event) {
        IBlockState block = event.getState();

        if (!event.isSilkTouching() && HammerRegistry.isHammerable(block)) {
            event.getDrops().clear();

            event.getDrops().addAll(HammerRegistry.getEntryForBlockState(event.getState()).rollRewards(event.getHarvester()));
        }
    }
}
