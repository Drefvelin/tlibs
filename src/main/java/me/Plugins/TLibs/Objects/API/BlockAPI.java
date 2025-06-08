package me.Plugins.TLibs.Objects.API;

import org.bukkit.Server;

import me.Plugins.TLibs.Objects.TLibAPI;
import me.Plugins.TLibs.Objects.API.SubAPI.BlockChecker;

public class BlockAPI extends TLibAPI{
	private BlockChecker checker;
	
	public void setup(Server s) {
		this.initialize(s);
		checker = new BlockChecker(this);
	}
	
	public BlockChecker getChecker() {
		return checker;
	}
}
