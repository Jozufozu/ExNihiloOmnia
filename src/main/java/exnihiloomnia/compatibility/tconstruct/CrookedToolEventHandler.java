package exnihiloomnia.compatibility.tconstruct;

import exnihiloomnia.compatibility.tconstruct.modifiers.ModCrooked;
import exnihiloomnia.items.crooks.ItemCrook;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.tools.ToolCore;
import slimeknights.tconstruct.library.traits.ITrait;
import slimeknights.tconstruct.library.utils.TagUtil;

public class CrookedToolEventHandler {

    @SubscribeEvent
    public void pull(PlayerInteractEvent.EntityInteract event) {
        ItemStack stack = event.getItemStack();
        EntityPlayer player = event.getEntityPlayer();
        Entity entity = event.getTarget();

        if (stack != null && stack.getItem() instanceof ToolCore) {

            NBTTagList tagList = TagUtil.getTraitsTagList(stack);

            for(int i = 0; i < tagList.tagCount(); i++) {
                ITrait trait = TinkerRegistry.getTrait(tagList.getStringTagAt(i));

                if(trait instanceof ModCrooked) {
                    if (!player.worldObj.isRemote) {
                        double distance = Math.sqrt(Math.pow(player.posX - entity.posX, 2) + Math.pow(player.posZ - entity.posZ, 2));

                        double scalarX = (player.posX - entity.posX) / distance;
                        double scalarZ = (player.posZ - entity.posZ) / distance;

                        double velX = scalarX * ItemCrook.pullingForce;
                        double velY = 0;
                        double velZ = scalarZ * ItemCrook.pullingForce;

                        if (player.posY > entity.posY)
                            velY = 0.5d;

                        entity.addVelocity(velX, velY, velZ);

                        stack.damageItem(1, player);
                    }
                }
            }
        }
    }
}
