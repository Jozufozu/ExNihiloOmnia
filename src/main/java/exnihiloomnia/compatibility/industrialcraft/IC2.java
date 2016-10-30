package exnihiloomnia.compatibility.industrialcraft;

import exnihiloomnia.ENO;
import exnihiloomnia.items.ENOItems;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlockSpecial;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class IC2 {

    public static Item SEED_RUBBER;

    public static void registerItems() {
        SEED_RUBBER = new ItemBlockSpecial(Block.REGISTRY.getObject(new ResourceLocation("ic2", "sapling"))).setUnlocalizedName("seed_rubber").setRegistryName("seed_rubber").setCreativeTab(ENOItems.ENO_TAB);

        GameRegistry.register(SEED_RUBBER);
    }

    @SideOnly(Side.CLIENT)
    public static void loadTexture() {
        ModelLoader.setCustomModelResourceLocation(SEED_RUBBER, 0, new ModelResourceLocation(ENO.MODID + ":seed_rubber"));
    }
}
