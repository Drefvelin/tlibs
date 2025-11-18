package me.Plugins.TLibs;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import me.Plugins.TLibs.Enums.APIType;
import me.Plugins.TLibs.Manager.Pulser;
import me.Plugins.TLibs.Objects.TLibAPI;
import me.Plugins.TLibs.Objects.API.BlockAPI;
import me.Plugins.TLibs.Objects.API.ItemAPI;

public class TLibs extends JavaPlugin{
	private static TLibs instance;
	private final static BlockAPI bApi = new BlockAPI();
	private final static ItemAPI iApi = new ItemAPI();
	private final Pulser pulser = new Pulser();
	
	@Override
	public void onEnable() {
		instance = this;
		Bukkit.getLogger().info("[TLibs] Initializing...");
		
		initializeAPIs();
		pulser.start();
		getServer().getPluginManager().registerEvents(pulser, this);
	
		Bukkit.getLogger().info("[TLibs] Complete!");
	}
	private void initializeAPIs() {
		bApi.setup(getServer());
		iApi.setup(getServer());
	}
	@Deprecated
	public static TLibAPI getApiInstance(APIType t) {
		if(t.equals(APIType.ITEM_API)) {
			return iApi;
		} else if(t.equals(APIType.BLOCK_API)) {
			return bApi;
		}
		return null;
	}

	public static TLibs getInstance() {
		return instance;
	}

	public static ItemAPI getItemAPI() {
		return iApi;
	}

	public static BlockAPI getBlockAPI() {
		return bApi;
	}
}
