package exnihiloomnia.entities.thrown.stone;

import exnihiloomnia.items.ENOItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class EntityStoneRenderer implements IRenderFactory<EntityStone>{

    public static final EntityStoneRenderer INSTANCE = new EntityStoneRenderer();

    @Override
    public Render<EntityStone> createRenderFor(RenderManager manager) {
        return new RenderSnowball<EntityStone>(manager, ENOItems.STONE, Minecraft.getMinecraft().getRenderItem());
    }

}
