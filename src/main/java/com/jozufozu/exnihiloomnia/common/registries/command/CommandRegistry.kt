package com.jozufozu.exnihiloomnia.common.registries.command;

import com.jozufozu.exnihiloomnia.common.registries.ReloadableRegistry;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.SyntaxErrorException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class CommandRegistry extends CommandBase
{
    @Override
    public String getName()
    {
        return "enoreg";
    }
    
    @Override
    public String getUsage(ICommandSender sender)
    {
        return "commands.exnihiloomnia.enoreg.usage";
    }
    
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length != 2)
        {
            throw new SyntaxErrorException("commands.exnihiloomnia.enoreg.usage");
        }
        
        boolean all = "all".equals(args[1]);
        
        long start = System.nanoTime();
        
        int count = 0;
        
        for (ReloadableRegistry registry : ReloadableRegistry.getRegistries())
        {
            String resourcePath = registry.registryName.getResourcePath();
            if (all || resourcePath.equals(args[1]))
            {
                if ("reload".equals(args[0]))
                {
                    registry.load();
                }
                else if ("clear".equals(args[0]))
                {
                    registry.clear();
                }
                count++;
            }
        }
    
        double seconds = ((double) System.nanoTime() - start) * 1E-9;
        
        notifyCommandListener(sender, this, "commands.exnihiloomnia.enoreg.done", count, seconds);
    }
    
    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
        if (args.length == 1)
        {
            return getListOfStringsMatchingLastWord(args,"reload", "clear");
        }
        if (args.length == 2)
        {
            List<String> completions = new ArrayList<>();
            completions.add("all");
            
            for (ReloadableRegistry registry : ReloadableRegistry.getRegistries())
            {
                completions.add(registry.registryName.getResourcePath());
            }
            
            return getListOfStringsMatchingLastWord(args, completions);
        }
        
        return super.getTabCompletions(server, sender, args, targetPos);
    }
}
