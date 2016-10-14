package exnihiloomnia.compatibility.tconstruct.modifiers;

import exnihiloomnia.items.ENOItems;
import exnihiloomnia.util.Color;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import slimeknights.tconstruct.library.modifiers.ModifierTrait;
import slimeknights.tconstruct.tools.melee.item.*;
import slimeknights.tconstruct.tools.tools.*;

public class ModCrooked extends ModifierTrait {

    public ModCrooked() {
        super("crooked", new Color(200, 200, 200).toInt());
    }

    @Override
    public boolean canApplyTogether(Enchantment enchantment) {
        return enchantment != Enchantments.SILK_TOUCH;
    }

    @Override
    public boolean canApplyCustom(ItemStack stack) {
        return stack.getItem() instanceof Mattock
                || stack.getItem() instanceof Hatchet
                || stack.getItem() instanceof LumberAxe
                || stack.getItem() instanceof Cleaver
                || stack.getItem() instanceof BroadSword
                || stack.getItem() instanceof LongSword
                || stack.getItem() instanceof Rapier
                || stack.getItem() instanceof Pickaxe
                || stack.getItem() instanceof Scythe;
    }

    @Override
    public void blockHarvestDrops(ItemStack tool, BlockEvent.HarvestDropsEvent event) {
        IBlockState block = event.getState();
        World world = event.getWorld();

        if (block.getMaterial() == Material.LEAVES || block.getBlock() instanceof BlockTallGrass) {
            //drop doubling
            event.getDrops().addAll(block.getBlock().getDrops(world, event.getPos(), block, event.getFortuneLevel()));

            if ((block.getMaterial().equals(Material.LEAVES) && world.rand.nextInt(100) == 0)) {
                //silkworms!
                event.getDrops().add(new ItemStack(ENOItems.SILKWORM));
            }
        }
    }
}
