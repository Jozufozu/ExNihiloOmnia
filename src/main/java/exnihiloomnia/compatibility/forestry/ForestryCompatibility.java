package exnihiloomnia.compatibility.forestry;

import exnihiloomnia.ENO;
import exnihiloomnia.compatibility.forestry.beelure.BlockBeeTrap;
import exnihiloomnia.compatibility.forestry.beelure.BlockBeeTrapTreated;
import exnihiloomnia.items.ENOItems;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ForestryCompatibility {

    public static Block BEE_TRAP;
    public static Block BEE_TRAP_TREATED;

    public static void preInit() {
        HiveRegistry.registerHives();

        BEE_TRAP = new BlockBeeTrap();
        BEE_TRAP_TREATED = new BlockBeeTrapTreated();

        GameRegistry.addShapelessRecipe(new ItemStack(BEE_TRAP), Blocks.HAY_BLOCK, ENOItems.MESH_SILK_WHITE);

        GameRegistry.register(BEE_TRAP);
        GameRegistry.register(BEE_TRAP_TREATED);

        GameRegistry.register(new ItemBlock(BEE_TRAP).setRegistryName("bee_trap"));
        GameRegistry.register(new ItemBlock(BEE_TRAP_TREATED).setRegistryName("bee_trap_treated"));
    }

    @SideOnly(Side.CLIENT)
    public static void loadTexture() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(BEE_TRAP), 0, new ModelResourceLocation(ENO.MODID + ":bee_trap"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(BEE_TRAP_TREATED), 0, new ModelResourceLocation(ENO.MODID + ":bee_trap_treated"));
    }
}
