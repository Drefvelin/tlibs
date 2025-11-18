package me.Plugins.TLibs.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ItemPulseEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private Player player;
    private ItemStack item;
    private final int slot;
    private Inventory i;

    public ItemPulseEvent(Player player, ItemStack item, int slot, Inventory i) {
        this.player = player;
        this.item = item;
        this.slot = slot;
        this.i = i;
    }

    public Player getPlayer() { return player; }
    public ItemStack getItem() { return item; }
    public int getSlot() { return slot; }
    public Inventory getInventory() { return i; }
    public void setItem(ItemStack item) {
        i.setItem(slot, item);
    }

    @Override
    public HandlerList getHandlers() { return handlers; }
    public static HandlerList getHandlerList() { return handlers; }
}
