package me.Plugins.TLibs.Objects;

import org.bukkit.Server;

import me.Plugins.TLibs.Interface.ApiInterface;
import me.Plugins.TLibs.InternalUtils.PluginChecker;

public class TLibAPI implements ApiInterface{
	private PluginChecker pc;
	private Server server;
	
	@Override
	public void initialize(Server s) {
		this.server = s;
		pc = new PluginChecker(s);
	}
	public Server getServer() {
		return server;
	}
	public PluginChecker getPluginChecker() {
		return pc;
	}
}
