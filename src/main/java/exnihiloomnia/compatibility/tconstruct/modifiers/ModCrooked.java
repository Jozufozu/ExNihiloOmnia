package exnihiloomnia.compatibility.tconstruct.modifiers;

import exnihiloomnia.blocks.ENOBlocks;
import exnihiloomnia.registries.crook.CrookRegistry;
import exnihiloomnia.util.Color;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import slimeknights.tconstruct.library.modifiers.ModifierTrait;
import slimeknights.tconstruct.tools.melee.item.BroadSword;
import slimeknights.tconstruct.tools.melee.item.Cleaver;
import slimeknights.tconstruct.tools.melee.item.LongSword;
import slimeknights.tconstruct.tools.melee.item.Rapier;
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

        if (CrookRegistry.isCrookable(block)) {
            World world = event.getWorld();
            BlockPos pos = event.getPos();
            EntityPlayer player = event.getHarvester();

            if (player != null) {
                ItemStack item = player.getActiveItemStack();
                //Simulate a block break to cause the first round of items to drop.
                block.getBlock().dropBlockAsItem(world, pos, world.getBlockState(pos), EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, item));
                if (!ENOBlocks.INFESTED_LEAVES.equals(block.getBlock())) {
                    for (ItemStack itemStack : CrookRegistry.getEntryForBlockState(block).rollRewards(player))
                        Block.spawnAsEntity(world, pos, itemStack);
                }
            }
        }
    }
}
