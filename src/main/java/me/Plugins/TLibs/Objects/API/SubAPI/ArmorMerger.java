package me.Plugins.TLibs.Objects.API.SubAPI;


import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.persistence.PersistentDataType;

import de.tr7zw.nbtapi.NBT;
import io.lumine.mythic.lib.api.item.ItemTag;
import io.lumine.mythic.lib.api.item.NBTItem;
import me.Plugins.TLibs.TLibs;
import me.Plugins.TLibs.Enums.APIType;
import me.Plugins.TLibs.Objects.TLibAPI;
import me.Plugins.TLibs.Objects.API.ItemAPI;
import net.tfminecraft.gunsandgadgets.GunsAndGadgets;
import net.tfminecraft.gunsandgadgets.guns.skins.SkinData;
import net.tfminecraft.gunsandgadgets.guns.skins.SkinState;
import net.tfminecraft.gunsandgadgets.loader.SkinLoader;

public class ArmorMerger extends TLibAPI{
	ItemAPI api;
	public ArmorMerger(ItemAPI api) {
		this.initialize(api.getServer());
		this.api = api;
	}
	@SuppressWarnings("null")
	public ItemStack merge(ItemStack item, Optional<String> name, String s) {
		ItemAPI api = (ItemAPI) TLibs.getApiInstance(APIType.ITEM_API);
		ItemStack skin = new ItemStack(Material.EMERALD, 1);
		if(s.split("\\(")[0].equalsIgnoreCase("localmodel")) {
			String info = s.split("\\(")[1].replace(")", "");
			try {
				skin = new ItemStack(Material.valueOf(info.split("\\.")[0].toUpperCase()), 1);
				ItemMeta m = skin.getItemMeta();
				m.setCustomModelData(Integer.parseInt(info.split("\\.")[1]));
				skin.setItemMeta(m);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if(s.split("\\(")[0].equalsIgnoreCase("gunskin")){
			String value = s.split("\\(")[1].replace(")", "");
			SkinData gunskin = SkinLoader.getByString(value);
			if(gunskin == null) {
				return item;
			}
			ItemMeta m = item.getItemMeta();
			NamespacedKey skinKey = new NamespacedKey(GunsAndGadgets.getInstance(), "skin_id");
            m.getPersistentDataContainer().set(skinKey, PersistentDataType.STRING, gunskin.getId());
			item.setItemMeta(m);
			int bullets = m.getPersistentDataContainer()
            	.getOrDefault(new NamespacedKey(GunsAndGadgets.getInstance(), "bullets_loaded"), PersistentDataType.INTEGER, 0);
			if(bullets > 0) GunsAndGadgets.getInstance().getGunManager().applyModel(skin, gunskin, SkinState.AIM);
			else GunsAndGadgets.getInstance().getGunManager().applyModel(skin, gunskin, SkinState.CARRY);
		} else {
			skin = api.getCreator().getItemFromPath(s);

			if(s.split("\\.")[0].equalsIgnoreCase("ia")) {
				String namespace = s.split("\\.")[1].split("\\:")[0];
				String id = s.split("\\.")[1].split("\\:")[1];
				NBTItem mnbt = NBTItem.get(item);
				mnbt.addTag(new ItemTag("ia", namespace+"."+id));
				item = mnbt.toItem();
			}
		}
		if(skin.getItemMeta().hasCustomModelData()) {
			NBTItem mnbt = NBTItem.get(item);
			mnbt.addTag(new ItemTag("amodel", String.valueOf(skin.getItemMeta().getCustomModelData())));
			item = mnbt.toItem();
		}
		item.setType(skin.getType());
		ItemMeta skinMeta = skin.getItemMeta();
		ItemMeta m = item.getItemMeta();
		if(name.isPresent()) {
			m.setDisplayName(name.get());
		}
		if(skinMeta.hasCustomModelData()) m.setCustomModelData(skinMeta.getCustomModelData());
		
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
