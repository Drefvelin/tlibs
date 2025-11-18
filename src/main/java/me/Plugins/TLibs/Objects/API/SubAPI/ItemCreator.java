package me.Plugins.TLibs.Objects.API.SubAPI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import dev.lone.itemsadder.api.CustomStack;
import me.Plugins.TLibs.Objects.TLibAPI;
import me.Plugins.TLibs.Objects.API.ItemAPI;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.manager.ItemManager;

public class ItemCreator extends TLibAPI{
	public ItemCreator(ItemAPI api) {
		this.initialize(api.getServer());
	}
	
	@SuppressWarnings("deprecation")
	public ItemStack getItemFromPath(String s) {
		String type = s.split("\\.")[0]; //v.emerald
		ItemStack item = new ItemStack(Material.DIRT, 1);
		if(type.equalsIgnoreCase("v")) {
			item.setType(Material.valueOf(s.split("\\.")[1].toUpperCase()));
		} else if(type.equalsIgnoreCase("m")) {
			if(!(this.getPluginChecker().checkPlugin("MMOItems") && this.getPluginChecker().checkPlugin("MythicLib"))) {
				Bukkit.getLogger().info("[TLibs] ERROR! This operation requires MMOItems and MythicLib!");
				return new ItemStack(Material.DIRT, 1);
			}
			ItemManager itemManager = MMOItems.plugin.getItems();
			if(itemManager.getMMOItem(MMOItems.plugin.getTypes().get(s.split("\\.")[1].toUpperCase()), s.split("\\.")[2].toUpperCase()) == null){
				Bukkit.getLogger().info(s + " ia a malformed item input");
				return null;
			}
			item =  itemManager.getMMOItem(MMOItems.plugin.getTypes().get(s.split("\\.")[1].toUpperCase()), s.split("\\.")[2].toUpperCase()).newBuilder().build(); //m.material.salt
		} else if (type.equalsIgnoreCase("modeled")) {
			String raw = s.substring(s.indexOf('(') + 1, s.lastIndexOf(')')); // Extract content inside (...)
			String[] parts = raw.split(";");
			Map<String, String> attributes = new HashMap<>();

			for (String part : parts) {
				String[] keyValue = part.split("=", 2);
				if (keyValue.length == 2) {
					attributes.put(keyValue[0].toLowerCase(), keyValue[1]);
				}
			}

			// Default to DIRT if invalid type
			Material material = Material.DIRT;
			if (attributes.containsKey("type")) {
				try {
					material = Material.valueOf(attributes.get("type").toUpperCase());
				} catch (IllegalArgumentException e) {
					Bukkit.getLogger().warning("[TLibs] Invalid material type in modeled item: " + attributes.get("type"));
				}
			}

			item = new ItemStack(material, 1);
			ItemMeta meta = item.getItemMeta();

			if (meta != null) {
				if (attributes.containsKey("name")) {
					meta.setDisplayName(attributes.get("name"));
				}

				if (attributes.containsKey("model")) {
					try {
						int modelData = Integer.parseInt(attributes.get("model"));
						meta.setCustomModelData(modelData);
					} catch (NumberFormatException e) {
						Bukkit.getLogger().warning("[TLibs] Invalid model data in modeled item: " + attributes.get("model"));
					}
				}

				item.setItemMeta(meta);
			}
		} else {
			if(!(this.getPluginChecker().checkPlugin("ItemsAdder"))) {
				Bukkit.getLogger().info("[TLibs] ERROR! This operation requires ItemsAdder and LoneLibs!");
				return new ItemStack(Material.DIRT, 1);
			}
			String itemPath = s.split("\\.")[1]; //ia.tfmc:abyssalite
			CustomStack stack = CustomStack.getInstance(itemPath);
			if(stack != null) {
				item = stack.getItemStack();
				item.setAmount(1);
			}
		}
		return item;
	}
	public ItemStack getItemsAdderItem(String path) {
		CustomStack stack = CustomStack.getInstance(path);
		if(stack != null) {
			ItemStack i = stack.getItemStack();
			return i;
		}
		return null;
	}
	
	public ItemStack getItemFromConfig(ConfigurationSection config) {
		ItemStack i = new ItemStack(Material.valueOf(config.getString("material", "DIRT").toUpperCase()), 1);
		ItemMeta meta = i.getItemMeta();
		meta.setDisplayName(StringFormatter.formatHex(config.getString("name", "No Name")));
		if(config.contains("model_data")) {
			meta.setCustomModelData(config.getInt("model_data"));
		}
		if(config.contains("enchants")) {
			for(String s : config.getStringList("enchants")) {
				meta.addEnchant(Enchantment.getByKey(NamespacedKey.minecraft(s.split("\\.")[0])), Integer.parseInt(s.split("\\.")[1]), true);
			}
		}
		if(config.contains("hide_enchants")) {
			if(config.getBoolean("hide_enchants") == true) {
				meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			}
		}
		if(config.contains("lore")) {
			List<String> newLore = new ArrayList<String>();
			for(String s : config.getStringList("lore")) {
				newLore.add(StringFormatter.formatHex(s));
			}
			meta.setLore(newLore);
		}
		i.setItemMeta(meta);
		return i;
	}
}
