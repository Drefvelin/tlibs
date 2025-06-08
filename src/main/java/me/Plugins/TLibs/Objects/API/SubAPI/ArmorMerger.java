package me.Plugins.TLibs.Objects.API.SubAPI;


import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import dev.lone.LoneLibs.nbt.nbtapi.NBT;
import io.lumine.mythic.lib.api.item.ItemTag;
import io.lumine.mythic.lib.api.item.NBTItem;
import me.Plugins.TLibs.Objects.TLibAPI;
import me.Plugins.TLibs.Objects.API.ItemAPI;

public class ArmorMerger extends TLibAPI{
	ItemAPI api;
	public ArmorMerger(ItemAPI api) {
		this.initialize(api.getServer());
		this.api = api;
	}
	public ItemStack merge(ItemStack item, String s) {
		if(!(this.getPluginChecker().checkPlugin("ItemsAdder") && this.getPluginChecker().checkPlugin("LoneLibs"))) {
			Bukkit.getLogger().info("[TLibs] ERROR! This operation requires ItemsAdder and LoneLibs!");
			return new ItemStack(Material.DIRT, 1);
		}
		ItemStack skin = api.getCreator().getItemFromPath(s);
		if(s.split("\\.")[0].equalsIgnoreCase("ia")) {
			String namespace = s.split("\\.")[1].split("\\:")[0];
			String id = s.split("\\.")[1].split("\\:")[1];
			NBTItem mnbt = NBTItem.get(item);
			mnbt.addTag(new ItemTag("ia", namespace+"."+id));
			item = mnbt.toItem();
		}
		item.setType(skin.getType());
		ItemMeta skinMeta = skin.getItemMeta();
		ItemMeta m = item.getItemMeta();
		m.setCustomModelData(skinMeta.getCustomModelData());
		if(skin.getType().toString().toLowerCase().contains("leather")) {
			LeatherArmorMeta ls = (LeatherArmorMeta) skinMeta;
			LeatherArmorMeta lm = (LeatherArmorMeta) m;
			lm.setColor(ls.getColor());
			item.setItemMeta(lm);
			if(s.split("\\.")[0].equalsIgnoreCase("ia")) {
				String namespace = s.split("\\.")[1].split("\\:")[0];
				String id = s.split("\\.")[1].split("\\:")[1];
				NBT.modify(item, nbt ->{
					nbt.getOrCreateCompound("itemsadder");
					nbt.getCompound("itemsadder").setString("namespace", namespace);
					nbt.getCompound("itemsadder").setString("id", id);
				});
			}
		} else {
			item.setItemMeta(m);
		}
		return item;
	}
}
