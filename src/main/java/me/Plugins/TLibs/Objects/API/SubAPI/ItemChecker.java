package me.Plugins.TLibs.Objects.API.SubAPI;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
		if(path.split("\\.")[0].equalsIgnoreCase("v") && i.hasItemMeta()) {
			ItemMeta meta = i.getItemMeta();
			boolean hasName = meta.hasDisplayName();
			boolean hasModel = meta.hasCustomModelData();

			if (hasName || hasModel) {
				StringBuilder sb = new StringBuilder("modeled.(");
				sb.append("type=").append(i.getType().toString().toLowerCase());
				if (hasName) {
					sb.append(";name=").append(meta.getDisplayName());
				}
				if (hasModel) {
					sb.append(";model=").append(meta.getCustomModelData());
				}
				sb.append(")");
				return sb.toString();
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
		} else if (type.equalsIgnoreCase("modeled")) {
			if (!item.hasItemMeta()) return false;

			String raw = s.substring(s.indexOf('(') + 1, s.lastIndexOf(')')); // type=emerald;name=§eYellowshard;model=3
			String[] parts = raw.split(";");
			Map<String, String> attributes = new HashMap<>();

			for (String part : parts) {
				String[] keyValue = part.split("=", 2);
				if (keyValue.length == 2) {
					attributes.put(keyValue[0].toLowerCase(), keyValue[1]);
				}
			}

			// Check type
			if (attributes.containsKey("type")) {
				Material material;
				try {
					material = Material.valueOf(attributes.get("type").toUpperCase());
				} catch (IllegalArgumentException e) {
					return false;
				}
				if (item.getType() != material) return false;
			}

			// Check name
			if (attributes.containsKey("name")) {
				String expectedName = attributes.get("name");
				if (!item.getItemMeta().hasDisplayName()) return false;
				String actualName = item.getItemMeta().getDisplayName();
				if (!actualName.equals(expectedName)) return false;
			}

			// Check model
			if (attributes.containsKey("model")) {
				if (!item.getItemMeta().hasCustomModelData()) return false;
				int expectedModel;
				try {
					expectedModel = Integer.parseInt(attributes.get("model"));
				} catch (NumberFormatException e) {
					return false;
				}
				int actualModel = item.getItemMeta().getCustomModelData();
				if (actualModel != expectedModel) return false;
			}

			return true;
		}
		return false;
	}
	
	public String getMMOItemsType(ItemStack i) {
		NBTItem nbt = NBTItem.get(i);
		if(!nbt.hasType()) return "none";
		return nbt.getType();
	}
}
