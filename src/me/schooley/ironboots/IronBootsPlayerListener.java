package me.schooley.ironboots;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class IronBootsPlayerListener extends PlayerListener {
	
	public IronBootsPlayerListener(IronBoots instance) {
		IronBoots.plugin = instance;
	}

	public void onPlayerLogin(PlayerLoginEvent event){
		
		Player player = event.getPlayer();
		//is there a better way to declare the defaults without having to do it for each player who logs in?
		IronBoots.playerEventBoots.put(player, false); //preset to false for message on login.
		IronBoots.playerEventSneak.put(player, false); //in case EnableSneak is not..enabled.
		IronBoots.checkBoots(player);
		
	}
	
	public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
		if (IronBoots.sneak) {
			Player player = event.getPlayer();
			boolean isSneaking = player.isSneaking();
			IronBoots.playerEventSneak.put(player, isSneaking);
		}
	}
	
}