package exnihiloomnia.config;

import exnihiloomnia.ENO;
import exnihiloomnia.ENOConfig;
import exnihiloomnia.blocks.barrels.states.BarrelStates;
import exnihiloomnia.compatibility.ENOCompatibility;
import exnihiloomnia.crafting.ENOCrafting;
import exnihiloomnia.registries.ENORegistries;
import exnihiloomnia.world.ENOWorld;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.ArrayList;
import java.util.List;

public class ConfigGui extends GuiConfig {

    public ConfigGui(GuiScreen parentScreen) { // The screen we were before entering our Config GUI
        super(parentScreen,
                getElements(),
                ENO.MODID,
                false,
                false,
                GuiConfig.getAbridgedConfigPath(ENO.config.toString()));
    }

    public static List<IConfigElement> getElements() {
        ArrayList<IConfigElement> configElements = new ArrayList<IConfigElement>();

        configElements.add(new ConfigElement(ENO.config.getCategory(ENOConfig.MISC)));
        configElements.add(new ConfigElement(ENO.config.getCategory(ENOCompatibility.CATEGORY_COMPAT_OPTIONS).setRequiresMcRestart(true)));
        configElements.add(new ConfigElement(ENO.config.getCategory(BarrelStates.CATEGORY_BARREL_OPTIONS).setRequiresMcRestart(true)));
        configElements.add(new ConfigElement(ENO.config.getCategory(ENOConfig.AUTOMATION)));
        configElements.add(new ConfigElement(ENO.config.getCategory(ENOCrafting.CATEGORY_CRAFTING_OPTIONS).setRequiresMcRestart(true)));
        configElements.add(new ConfigElement(ENO.config.getCategory(ENOWorld.CATEGORY_WORLD_GEN).setRequiresMcRestart(true)));
        configElements.add(new ConfigElement(ENO.config.getCategory(ENOWorld.CATEGORY_WORLD_MOD)));
        configElements.add(new ConfigElement(ENO.config.getCategory(ENORegistries.CATEGORY_DEFAULT_RECIPES)));
        configElements.add(new ConfigElement(ENO.config.getCategory(ENORegistries.CATEGORY_ORE_RECIPES)));
        configElements.addAll(new ConfigElement(ENO.config.getCategory(Configuration.CATEGORY_GENERAL).setRequiresMcRestart(true)).getChildElements());

        return configElements;
    }
}
