package me.Plugins.TLibs.InternalUtils;

import org.bukkit.Server;

public class PluginChecker {
	Server server;
	public PluginChecker(Server s) {
		this.server = s;
	}
	public boolean checkPlugin(String plugin) {
		if(server.getPluginManager().getPlugin(plugin) != null) return true;
		return false;
	}
}
