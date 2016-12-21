package exnihiloomnia.compatibility.forestry;

import exnihiloomnia.ENO;
import exnihiloomnia.compatibility.ENOCompatibility;
import exnihiloomnia.compatibility.forestry.beelure.BlockBeeTrap;
import exnihiloomnia.compatibility.forestry.beelure.BlockBeeTrapTreated;
import exnihiloomnia.items.ENOItems;
import exnihiloomnia.registries.sifting.SieveRegistry;
import exnihiloomnia.registries.sifting.SieveRegistryEntry;
import exnihiloomnia.util.enums.EnumMetadataBehavior;
import forestry.core.PluginCore;
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

    public static void preInit() {
        HiveRegistry.registerHives();

        ENOCompatibility.BEE_TRAP = new BlockBeeTrap();
        ENOCompatibility.BEE_TRAP_TREATED = new BlockBeeTrapTreated();

        GameRegistry.register(ENOCompatibility.BEE_TRAP);
        GameRegistry.register(ENOCompatibility.BEE_TRAP_TREATED);

        GameRegistry.register(new ItemBlock(ENOCompatibility.BEE_TRAP).setRegistryName("bee_trap"));
        GameRegistry.register(new ItemBlock(ENOCompatibility.BEE_TRAP_TREATED).setRegistryName("bee_trap_treated"));
    }

    public static void initialize() {
        GameRegistry.addShapelessRecipe(new ItemStack(ENOCompatibility.BEE_TRAP), Blocks.HAY_BLOCK, ENOItems.MESH_SILK_WHITE);

        SieveRegistryEntry gravel = new SieveRegistryEntry(Blocks.GRAVEL.getDefaultState(), EnumMetadataBehavior.IGNORED);
        gravel.addReward(new ItemStack(PluginCore.items.apatite), 6);
        SieveRegistry.add(gravel);
    }

    @SideOnly(Side.CLIENT)
    public static void loadTexture() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ENOCompatibility.BEE_TRAP), 0, new ModelResourceLocation(ENO.MODID + ":bee_trap"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ENOCompatibility.BEE_TRAP_TREATED), 0, new ModelResourceLocation(ENO.MODID + ":bee_trap_treated"));
    }
}
