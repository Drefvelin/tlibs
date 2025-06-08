package me.Plugins.TLibs;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import me.Plugins.TLibs.Enums.APIType;
import me.Plugins.TLibs.Objects.TLibAPI;
import me.Plugins.TLibs.Objects.API.BlockAPI;
import me.Plugins.TLibs.Objects.API.ItemAPI;

public class TLibs extends JavaPlugin{
	private final static BlockAPI bApi = new BlockAPI();
	private final static ItemAPI iApi = new ItemAPI();
	
	@Override
	public void onEnable() {
		Bukkit.getLogger().info("[TLibs] Initializing...");
		
		initializeAPIs();
	
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

	public static ItemAPI getItemAPI() {
		return iApi;
	}

	public static BlockAPI getBlockAPI() {
		return bApi;
	}
}
