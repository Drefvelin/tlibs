package me.Plugins.TLibs.Objects.API;

import org.bukkit.Server;

import me.Plugins.TLibs.Objects.TLibAPI;
import me.Plugins.TLibs.Objects.API.SubAPI.ArmorMerger;
import me.Plugins.TLibs.Objects.API.SubAPI.ItemChecker;
import me.Plugins.TLibs.Objects.API.SubAPI.ItemCreator;

public class ItemAPI extends TLibAPI{
	private ItemCreator creator;
	private ItemChecker checker;
	private ArmorMerger armorMerger;
	
	public void setup(Server s) {
		this.initialize(s);
		creator = new ItemCreator(this);
		checker = new ItemChecker(this);
		armorMerger = new ArmorMerger(this);
	}
	
	public ArmorMerger getArmorMerger() {
		return armorMerger;
	}
	
	public ItemCreator getCreator() {
		return creator;
	}
	
	public ItemChecker getChecker() {
		return checker;
	}
}
