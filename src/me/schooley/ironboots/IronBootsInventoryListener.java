package me.schooley.ironboots;

import org.bukkit.entity.Player;
import org.bukkitcontrib.event.inventory.InventoryClickEvent;
import org.bukkitcontrib.event.inventory.InventoryListener;

public class IronBootsInventoryListener extends InventoryListener {
	
	public static IronBoots plugin;
	
	public IronBootsInventoryListener(IronBoots instance) {
		IronBoots.plugin = instance;
	}
	
	public void onInventoryClick(InventoryClickEvent event) {
		
		Player player = event.getPlayer();
		IronBoots.checkBoots(player);
		
	}
}