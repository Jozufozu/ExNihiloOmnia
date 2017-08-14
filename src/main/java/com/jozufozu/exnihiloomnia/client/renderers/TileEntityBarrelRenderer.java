package com.jozufozu.exnihiloomnia.client.renderers;

import com.jozufozu.exnihiloomnia.common.blocks.barrel.logic.TileEntityBarrel;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;

public class TileEntityBarrelRenderer extends TileEntitySpecialRenderer<TileEntityBarrel>
{
    @Override
    public void render(TileEntityBarrel te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        te.getState().draw(te, x, y, z, partialTicks);
        
        super.render(te, x, y, z, partialTicks, destroyStage, alpha);
    }
}
