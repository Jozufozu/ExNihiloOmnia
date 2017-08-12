package com.jozufozu.exnihiloomnia.common.world;

import com.google.common.collect.Maps;
import com.google.gson.*;
import com.jozufozu.exnihiloomnia.ExNihilo;
import com.jozufozu.exnihiloomnia.common.registries.RegistryLoader;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import org.apache.commons.io.IOUtils;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpawnIsland
{
    public static final Gson GSON = new GsonBuilder().setLenient().create();
    
    private BlockPos origin = new BlockPos(0, 0, 0);
    
    private Map<String, Placement> palette = Maps.newHashMap();
    private Map<BlockPos, String> structure = Maps.newHashMap();
    
    public void placeInWorld(World world, BlockPos origin)
    {
        long nanoStart = System.nanoTime();
        
        int xOff = origin.getX() - this.origin.getX();
        int yOff = origin.getY() - this.origin.getY();
        int zOff = origin.getZ() - this.origin.getZ();
    
        for (BlockPos pos : this.structure.keySet())
        {
            Placement placement = this.palette.get(this.structure.get(pos));
            BlockPos place = pos.add(xOff, yOff, zOff);
            world.setBlockState(place, placement.state, 2);
            TileEntity tileEntity = world.getTileEntity(place);
            
            if (tileEntity != null && placement.tileData != null)
            {
                placement.tileData.setInteger("x", place.getX());
                placement.tileData.setInteger("y", place.getY());
                placement.tileData.setInteger("z", place.getZ());
                
                tileEntity.readFromNBT(placement.tileData);
                world.notifyBlockUpdate(place, placement.state, placement.state, 3);
            }
        }
        
        long nanoNow = System.nanoTime();
        ExNihilo.log.info(String.format("Loaded spawn island into world. Took %.3f", ((double) (nanoNow - nanoStart)) * 1E-9));
    }
    
    public static SpawnIsland createFromWorld(World world, BlockPos from, BlockPos to, BlockPos origin)
    {
        long nanoStart = System.nanoTime();
        int startX = Math.min(from.getX(), to.getX());
        int startY = Math.min(from.getY(), to.getY());
        int startZ = Math.min(from.getZ(), to.getZ());
    
        int sizeX = Math.abs(from.getX() - to.getX());
        int sizeY = Math.abs(from.getY() - to.getY());
        int sizeZ = Math.abs(from.getZ() - to.getZ());
    
        HashMap<Placement, List<BlockPos>> process = new HashMap<>();
    
        BlockPos.MutableBlockPos read = new BlockPos.MutableBlockPos();
        
        for (int y = startY; y < startY + sizeY; y++)
        {
            for (int x = startX; x < startX + sizeX; x++)
            {
                for (int z = startZ; z < startZ + sizeZ; z++)
                {
                    read.setPos(x, y, z);
                    
                    if (world.isAirBlock(read))
                        continue;
                    
                    IBlockState state = world.getBlockState(read);
                    NBTTagCompound tileData = null;
                    
                    if (state.getBlock().hasTileEntity(state))
                    {
                        TileEntity tileEntity = world.getTileEntity(read);
                        
                        if (tileEntity != null)
                        {
                            tileData = tileEntity.serializeNBT();
                            tileData.removeTag("id");
                            tileData.removeTag("x");
                            tileData.removeTag("y");
                            tileData.removeTag("z");
                        }
                    }
                    
                    Placement placement = new Placement(state, tileData);
                    BlockPos relative = read.toImmutable();
    
                    process.computeIfAbsent(placement, k -> new ArrayList<>()).add(relative);
                }
            }
        }
        
        SpawnIsland out = new SpawnIsland();
        
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int minZ = Integer.MAX_VALUE;
    
        for (Placement placement : process.keySet())
        {
            for (BlockPos pos : process.get(placement))
            {
                minX = Math.min(minX, pos.getX());
                minY = Math.min(minY, pos.getY());
                minZ = Math.min(minZ, pos.getZ());
            }
        }
        
        out.origin = origin.add(-minX, -minY, -minZ);
        
        int i = 0;
        
        for (Placement placement : process.keySet())
        {
            String var = String.valueOf(i);
            
            out.palette.put(var, placement);
    
            for (BlockPos pos : process.get(placement))
            {
                out.structure.put(pos.add(-minX, -minY, -minZ), var);
            }
            
            i++;
        }
    
        long nanoNow = System.nanoTime();
        
        double seconds = ((double) (nanoNow - nanoStart)) * 1E-9;
        
        for (EntityPlayer player : world.playerEntities)
        {
            player.sendMessage(new TextComponentString(String.format("Made spawn island. Took %.3f", seconds)));
        }
        
        return out;
    }
    
    public void save(EntityPlayer user)
    {
        PrintWriter writer = null;
        try
        {
            LocalDateTime now = LocalDateTime.now();
            String name = user.getDisplayNameString() + "-" + now.getYear() + now.getMonthValue() + now.getDayOfMonth() + "-" + now.getHour() + now.getMinute() + now.getSecond() + ".json";
            File file = new File(ExNihilo.PATH, String.format("/spawn_island/%s", name));
            file.createNewFile();
            
            writer = new PrintWriter(file);
            writer.append(serialize());
    
            user.sendMessage(new TextComponentTranslation("info.exnihiloomnia.save_island.saved", name));
        }
        catch (IOException e)
        {
            user.sendMessage(new TextComponentTranslation("info.exnihiloomnia.save_island.error", e.getMessage()));
        }
        finally
        {
            IOUtils.closeQuietly(writer);
        }
    }
    
    public String serialize()
    {
        JsonObject object = new JsonObject();
        
        object.add("origin", serializeBlockPos(this.origin));
        
        JsonObject palette = new JsonObject();
        
        this.palette.forEach((s, placement) -> palette.add(s, serializePlacement(placement)));
        
        object.add("palette", palette);
        
        JsonArray structure = new JsonArray();
        this.structure.forEach((pos, s) -> {
            JsonObject block = new JsonObject();
            block.addProperty("block", s);
            block.add("pos", serializeBlockPos(pos));
            structure.add(block);
        });
        
        object.add("structure", structure);
        
        return GSON.toJson(object);
    }
    
    public static JsonArray serializeBlockPos(BlockPos pos)
    {
        JsonArray array = new JsonArray();
        array.add(pos.getX());
        array.add(pos.getY());
        array.add(pos.getZ());
        return array;
    }
    
    public static JsonObject serializePlacement(Placement placement)
    {
        JsonObject placementObject = new JsonObject();
    
        Block block = placement.state.getBlock();
        placementObject.addProperty("id", Block.REGISTRY.getNameForObject(block).toString());
        placementObject.addProperty("data", block.getMetaFromState(placement.state));
    
        if (placement.tileData != null)
        {
            placementObject.add("nbt", serializeNBT(placement.tileData));
        }
        
        return placementObject;
    }
    
    public static JsonElement serializeNBT(NBTBase nbt)
    {
        if (nbt instanceof NBTTagCompound)
        {
            JsonObject out = new JsonObject();
            
            NBTTagCompound compound = (NBTTagCompound) nbt;
            
            for (String s : compound.getKeySet())
            {
                out.add(s, serializeNBT(compound.getTag(s)));
            }
            
            return out;
        }
        else if (nbt instanceof NBTTagList)
        {
            JsonArray out = new JsonArray();
            NBTTagList list = ((NBTTagList) nbt);
    
            for (NBTBase nbtBase : list)
            {
                out.add(serializeNBT(nbtBase));
            }
            
            return out;
        }
        else if (nbt instanceof NBTTagString)
        {
            return new JsonPrimitive(((NBTTagString) nbt).getString());
        }
        else if (nbt instanceof NBTTagInt)
        {
            return new JsonPrimitive(((NBTTagInt) nbt).getInt());
        }
        else if (nbt instanceof NBTTagByte)
        {
            return new JsonPrimitive(((NBTTagByte) nbt).getByte());
        }
        else if (nbt instanceof NBTTagFloat)
        {
            return new JsonPrimitive(((NBTTagFloat) nbt).getFloat());
        }
        else if (nbt instanceof NBTTagLong)
        {
            return new JsonPrimitive(((NBTTagLong) nbt).getLong());
        }
        else if (nbt instanceof NBTTagShort)
        {
            return new JsonPrimitive(((NBTTagShort) nbt).getShort());
        }
        else if (nbt instanceof NBTTagDouble)
        {
            return new JsonPrimitive(((NBTTagDouble) nbt).getDouble());
        }
        return new JsonPrimitive("");
    }
    
    public static SpawnIsland deserialize(JsonObject object) throws JsonSyntaxException
    {
        SpawnIsland out = new SpawnIsland();
        
        if (!object.has("palette"))
        {
            throw new JsonSyntaxException("Structure json needs a palette!");
        }
        
        for (Map.Entry<String, JsonElement> blocks : JsonUtils.getJsonObject(object, "palette").entrySet())
        {
            JsonObject state = blocks.getValue().getAsJsonObject();
            out.palette.put(blocks.getKey(), deserializePlacement(state));
        }
        
        if (!object.has("structure"))
        {
            throw new JsonSyntaxException("Structure json needs a structure!");
        }
        
        for (JsonElement structure : JsonUtils.getJsonArray(object, "structure"))
        {
            try
            {
                if (!structure.isJsonObject())
                {
                    throw new JsonSyntaxException("Invalid structure entry: " + structure.toString());
                }
                
                JsonObject block = structure.getAsJsonObject();

                if (!block.has("block"))
                {
                    throw new JsonSyntaxException("Structure json block info has no block!");
                }

                String paletteName = JsonUtils.getString(block, "block");

                BlockPos pos = deserializeBlockPos(block.getAsJsonArray("pos"));
                
                out.structure.put(pos, paletteName);
            }
            catch (JsonParseException e)
            {
                ExNihilo.log.warn(e.getMessage());
            }
        }
        
        if (object.has("origin"))
        {
            out.origin = deserializeBlockPos(object.getAsJsonArray("origin"));
        }
        
        return out;
    }
    
    private static Placement deserializePlacement(JsonObject placement) throws JsonSyntaxException
    {
        Block block = null;
    
        if (placement.has("id"))
        {
            ResourceLocation resourcelocation = new ResourceLocation(JsonUtils.getString(placement, "id"));
        
            if (!Block.REGISTRY.containsKey(resourcelocation))
            {
                throw new JsonSyntaxException("Unknown block type '" + resourcelocation + "'");
            }
        
            block = Block.REGISTRY.getObject(resourcelocation);
        }
        
        if (block == null)
        {
            throw new JsonSyntaxException("Could not find block!");
        }
    
        IBlockState state = block.getDefaultState();
        
        if (placement.has("data"))
        {
            state = block.getStateFromMeta(JsonUtils.getInt(placement, "data"));
        }
        
        NBTTagCompound tag = null;
        
        if (placement.has("nbt"))
        {
            try
            {
                tag = JsonToNBT.getTagFromJson(RegistryLoader.GSON.toJson(placement.get("nbt")));
            }
            catch (NBTException e)
            {
                ExNihilo.log.error("Error reading tile data in structure json");
            }
        }
        
        return new Placement(state, tag);
    }
    
    private static BlockPos deserializeBlockPos(JsonArray pos) throws JsonParseException
    {
        if (pos.size() != 3)
        {
            throw new JsonSyntaxException("BlockPos needs to have x, y and z!");
        }
        
        int x = pos.get(0).getAsJsonPrimitive().getAsInt();
        int y = pos.get(1).getAsJsonPrimitive().getAsInt();
        int z = pos.get(2).getAsJsonPrimitive().getAsInt();
        
        return new BlockPos(x, y, z);
    }
    
    public static class Placement
    {
        public final IBlockState state;
        public final NBTTagCompound tileData;
    
        public Placement(IBlockState state)
        {
            this.state = state;
            this.tileData = null;
        }
    
        public Placement(IBlockState state, @Nullable NBTTagCompound tileData)
        {
            this.state = state;
            this.tileData = tileData;
        }
    
        @Override
        public boolean equals(Object obj)
        {
            if (!(obj instanceof Placement))
            {
                return false;
            }
            
            Placement other = ((Placement) obj);
    
            return this.state == other.state && this.tileData == other.tileData;
        }
    
        @Override
        public int hashCode()
        {
            int result = state.toString().hashCode();
            result = 31 * result + (tileData != null ? tileData.hashCode() : 0);
            return result;
        }
    }
}
