package me.Plugins.TLibs.Objects.API.SubAPI;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;

import dev.lone.itemsadder.api.CustomBlock;
import dev.lone.itemsadder.api.CustomFurniture;
import me.Plugins.TLibs.Objects.TLibAPI;
import me.Plugins.TLibs.Objects.API.BlockAPI;

public class BlockChecker extends TLibAPI{
	public BlockChecker(BlockAPI api) {
		this.initialize(api.getServer());
	}
	public boolean checkBlock(Block b, String configPath) {
    	String type = configPath.split("\\(")[0];
    	String path = configPath.split("\\(")[1].replace(")", "");
    	if(type.equalsIgnoreCase("v")) {
    		if(Material.valueOf(path.toUpperCase()) != null) {
    			if(b.getType().equals(Material.valueOf(path.toUpperCase()))) return true;
    		}
    	} else if(type.equalsIgnoreCase("iaf")) {
    		return blockIsFurniture(b, path);
    	} else if(type.equalsIgnoreCase("iab")) {
    		return blockIsIA(b, path);
    	}
    	return false;
    }
	private boolean blockIsFurniture(Block b, String furniture) {
		if(!b.getType().equals(Material.BARRIER)) return false;
		if(!(this.getPluginChecker().checkPlugin("ItemsAdder") && this.getPluginChecker().checkPlugin("LoneLibs"))) {
			Bukkit.getLogger().info("[TLibs] ERROR! This operation requires ItemsAdder and LoneLibs!");
			return false;
		}
		List<Entity> nearbyEntities = (List<Entity>) b.getWorld().getNearbyEntities(b.getLocation().clone().add(0.5, 0, 0.5), 0.4, 0.4, 0.4);
		for(Entity a : b.getWorld().getEntities()){
            if(nearbyEntities.contains(a)){
				if(!(a instanceof ItemFrame)) continue;
            	CustomFurniture f = CustomFurniture.byAlreadySpawned(a);
                if(f != null) {
                	if((f.getNamespace()+":"+f.getId()).equalsIgnoreCase(furniture)) {
                		return true;
                	}
                }
            }
        }
		return false;
	}
	private boolean blockIsIA(Block b, String path) {
		if(!(this.getPluginChecker().checkPlugin("ItemsAdder") && this.getPluginChecker().checkPlugin("LoneLibs"))) {
			Bukkit.getLogger().info("[TLibs] ERROR! This operation requires ItemsAdder and LoneLibs!");
			return false;
		}
		CustomBlock block = CustomBlock.byAlreadyPlaced(b);
		if(block != null) {
			return (block.getNamespace()+":"+block.getId()).equalsIgnoreCase(path);
		}
		return false;
	}
}
