package me.Plugins.TLibs.MMOItem;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import me.Plugins.TLibs.Event.MMOItemRebuildEvent;
import me.Plugins.TLibs.Objects.API.SubAPI.ItemRebuildMerger;
import me.Plugins.TLibs.TLibs;
import me.Plugins.TLibs.Utils.RebuildDebug;

public class MMOItemRebuildBaselineListener implements Listener {

	@EventHandler(priority = EventPriority.LOWEST)
	public void onRebuild(MMOItemRebuildEvent event) {
		RebuildDebug.log("MMOItemRebuildEvent BaselineListener LOWEST reason=" + event.getReason());
		event.setNewItem(ItemRebuildMerger.applyBaseline(event.getOldItem(), event.getNewItem(),
				TLibs.getRebuildConfig(), event.getPdcSnapshot()));
		RebuildDebug.log("MMOItemRebuildEvent BaselineListener done");
	}
}
