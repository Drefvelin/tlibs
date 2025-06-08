package me.Plugins.TLibs.Objects.API.SubAPI;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import dev.lone.itemsadder.api.CustomStack;
import io.lumine.mythic.lib.api.item.NBTItem;
import me.Plugins.TLibs.Objects.TLibAPI;
import me.Plugins.TLibs.Objects.API.ItemAPI;

public class ItemChecker extends TLibAPI{
	public ItemChecker(ItemAPI api) {
		this.initialize(api.getServer());
	}
	
	public String getAsStringPath(ItemStack i) {
		String path = "v."+i.getType().toString().toLowerCase();
		if(this.getPluginChecker().checkPlugin("MMOItems") && this.getPluginChecker().checkPlugin("MythicLib")) {
			NBTItem nbt = NBTItem.get(i);
			if(nbt.hasType()) {
				path = "m."+nbt.getType().toLowerCase()+"."+nbt.getString("MMOITEMS_ITEM_ID").toLowerCase();
			}
		}
		if(this.getPluginChecker().checkPlugin("ItemsAdder") && this.getPluginChecker().checkPlugin("LoneLibs")) {
			CustomStack stack = CustomStack.byItemStack(i);
			if(stack != null) {
				path = "ia."+stack.getNamespacedID();
			}
		}
		return path;
	}

	public boolean checkItemWithPath(ItemStack item, String s) {
		String type = s.split("\\.")[0]; //v.emerald
		if(type.equalsIgnoreCase("v")) {
			if(item.getType().equals(Material.valueOf(s.split("\\.")[1].toUpperCase()))) return true;
		} else if(type.equalsIgnoreCase("m")) {
			if(!(this.getPluginChecker().checkPlugin("MMOItems") && this.getPluginChecker().checkPlugin("MythicLib"))) {
				Bukkit.getLogger().info("[TLibs] ERROR! This operation requires MMOItems and MythicLib!");
				return false;
			}
			NBTItem nbt = NBTItem.get(item);
			if(!nbt.hasType()) return false;
			if(nbt.getType().equalsIgnoreCase(s.split("\\.")[1]) && nbt.getString("MMOITEMS_ITEM_ID").equalsIgnoreCase(s.split("\\.")[2])) return true;
		} else if(type.equalsIgnoreCase("ia")) {
			if(!(this.getPluginChecker().checkPlugin("ItemsAdder") && this.getPluginChecker().checkPlugin("LoneLibs"))) {
				Bukkit.getLogger().info("[TLibs] ERROR! This operation requires ItemsAdder and LoneLibs!");
				return false;
			}
			String itemPath = s.split("\\.")[1]; //ia.tfmc:abyssalite
			CustomStack stack = CustomStack.byItemStack(item);
			if(stack != null) {
				if(itemPath.equalsIgnoreCase(stack.getNamespacedID())) return true;
			}
		}
		return false;
	}
	
	public String getMMOItemsType(ItemStack i) {
		NBTItem nbt = NBTItem.get(i);
		if(!nbt.hasType()) return "none";
		return nbt.getType();
	}
}
