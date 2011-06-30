package me.schooley.ironboots;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
import org.bukkit.util.config.Configuration;

public class IronBoots extends JavaPlugin {
	
	public static IronBoots plugin;
	public static Logger log = Logger.getLogger("Minecraft");
    private Configuration config;
    //------------------Listeners-----------------
    private final IronBootsPlayerListener playerListener = new IronBootsPlayerListener(this);
	private final IronBootsInventoryListener inventoryListener = new IronBootsInventoryListener(this);
    //------------------Defaults------------------
    public static boolean autoFloat = true;
    public static String bootType = "IRON_BOOTS";
    public static boolean sneak = false;
    //--------------------------------------------
    public static Map<Player, Boolean> playerEventBoots = new HashMap<Player, Boolean>();
    public static Map<Player, Boolean> playerEventSneak = new HashMap<Player, Boolean>();
    //--------------------------------------------    
	public void onDisable() {
		
		getServer().getScheduler().cancelTasks(this);
		config.save();
		log.info("IronBoots has been disabled.");
		
	}
    
	public void onEnable() {
        PluginDescriptionFile pdfFile = this.getDescription();
		log.info(pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!" );
		
		loadConfig();

		//-------------------Events-------------------
		PluginManager pm = getServer().getPluginManager();
		
		if (pm.isPluginEnabled("BukkitContrib")) {
			log.info("IronBoots has detected BukkitContrib.");
		} else {
			log.info("IronBoots cannot detect BukkitContrib. BukkitContrib is now installing...");
			//INSTALL BUKKITCONTRIB!
		}
		
		pm.registerEvent(Event.Type.PLAYER_LOGIN, playerListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.CUSTOM_EVENT, inventoryListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_TOGGLE_SNEAK, playerListener, Event.Priority.Normal, this);
		//--------------------------------------------
		
		getServer().getScheduler().scheduleAsyncRepeatingTask(this, new Runnable() {
		    public void run() {
		    	for (World world : getServer().getWorlds()) {
		            for (Player player : world.getPlayers()) {
		            	if (playerEventSneak.containsKey(player)) { //checks this since it's the last declared hashmap.
		            		log.info(player.getDisplayName() + playerEventBoots.get(player));
		            		autoBob(player);
		            	}
		            }
		        }
		    }
		}, 0L, 20L); //oh my, is everyone going to autoBob in sync?! cute.
	}
	
	protected void autoBob(Player player) {
		
		Material ground = player.getLocation().getBlock().getType();
		Material deepWater;
		boolean jumpTrigger = (ground == Material.WATER || ground == Material.STATIONARY_WATER);
		boolean sinkTrigger;
		boolean boots = playerEventBoots.get(player);
		
		Vector vel = player.getVelocity();
		double velX = vel.getX();
		double velY = vel.getY();
		double velZ = vel.getZ();
		
		if (jumpTrigger && velY <= -0.02) { //velY check attempts to make autoBob only activate once the player falls/sinks
			
			deepWater = player.getLocation().add(0, -4, 0).getBlock().getType();
			sinkTrigger = (deepWater == Material.STATIONARY_WATER);
			IronBoots.checkBoots(player);
			
			if (boots || sinkTrigger) {
					player.setVelocity(new Vector(velX, -0.2, velZ));
			} else if (playerEventSneak.get(player)) {
				player.setVelocity(new Vector(velX, 0.3, velZ));
			}
		}
		
	}

	public static void checkBoots(Player player) {
		
		boolean boots = playerEventBoots.get(player);
		
		if (player.getInventory().getBoots().getType().toString().equalsIgnoreCase(bootType)) {
			if (!boots) {
				player.sendMessage("You've equipped Iron Boots.");
			}
			boots = true;
		} else {
			if (boots) {
				player.sendMessage("You've unequipped Iron Boots.");
			}
			boots = false;
		}
		playerEventBoots.put(player, boots);
		
	}
	
	private void loadConfig() {
		config = this.getConfiguration();
		config.load();
		
		Object[][] propArray = new Object[][]{{autoFloat, "Basic.AutoFloat"}, {sneak, "Basic.EnableSneak"}, {bootType, "Basic.BootType"}};
		
		for (int i = 0; i < 3; i++) {
			
			String prop = (String)propArray[i][1];
			//OBJECT? default = propArray[i][0];
			if (config.getProperty(prop) == null) {
				
				log.info("Config node " + prop + " in IronBoots not found. Adding node with default value: " + propArray[i][0] + ".");
				config.setProperty(prop, propArray[i][0]);
				
			} else {
				propArray[i][0] = config.getProperty(prop);
			}
			
		}
		
		config.save();
		
	}
	
}