package exnihiloomnia.registries;

import exnihiloomnia.ENO;
import exnihiloomnia.registries.composting.CompostRegistry;
import exnihiloomnia.registries.crucible.CrucibleRegistry;
import exnihiloomnia.registries.crucible.HeatRegistry;
import exnihiloomnia.registries.hammering.HammerRegistry;
import exnihiloomnia.registries.sifting.SieveRegistry;
import mezz.jei.JeiHelpers;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.Loader;

import javax.annotation.Nullable;
import java.util.*;

public class CommandRegistry extends CommandBase {
    private static List<String> aliases = new ArrayList<String>();

    public CommandRegistry() {
        aliases.add("enoreg");
    }

    @Override
    public String getCommandName() {
        return "exnihiloregistry";
    }

    @Override
    public List<String> getCommandAliases() {
        return aliases;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return ENO.MODID + ".commands.registry.usage";
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
        if (args.length == 1)
            return  getListOfStringsMatchingLastWord(args, "reload", "clear", "load");
        else if (args.length == 2)
            return getListOfStringsMatchingLastWord(args, "heat", "crucible", "sieve", "hammer", "compost");
        else
            return Collections.emptyList();
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 2)
            throw new WrongUsageException(ENO.MODID + ".commands.registry.usage", 0);
        else {
            String command = args[0];
            IRegistry reg = getRegistryFromString(args[1]);

            if (reg != null) {
                ENORegistries.configure(ENO.config);
                if (ENO.config.hasChanged())
                    ENO.config.load();

                if (command.equals("clear")) {
                    reg.clear();
                } else if (command.equals("load")) {
                    reg.initialize();
                } else if (command.equals("reload")) {
                    reg.clear();
                    reg.initialize();
                }
                else
                    throw new WrongUsageException(ENO.MODID + ".commands.registry.usage", 0);

                if (Loader.isModLoaded("JEI"))
                    new JeiHelpers().reload();
            }
            else
                throw new WrongUsageException(ENO.MODID + ".commands.registry.usage", 0);
        }
    }

    private IRegistry getRegistryFromString(String s) {
        if (s.equals("heat"))
            return HeatRegistry.INSTANCE;
        else if (s.equals("crucible"))
            return CrucibleRegistry.INSTANCE;
        else if (s.equals("sieve"))
            return SieveRegistry.INSTANCE;
        else if (s.equals("hammer"))
            return HammerRegistry.INSTANCE;
        else if (s.equals("compost"))
            return CompostRegistry.INSTANCE;
        else
            return null;
    }
}
