package pl.lakasabasz.mc.planer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.dependency.Dependency;
import org.bukkit.plugin.java.annotation.permission.ChildPermission;
import org.bukkit.plugin.java.annotation.permission.Permission;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion;
import org.bukkit.plugin.java.annotation.plugin.Description;
import org.bukkit.plugin.java.annotation.plugin.LogPrefix;
import org.bukkit.plugin.java.annotation.plugin.Plugin;
import org.bukkit.plugin.java.annotation.plugin.author.Author;

import net.md_5.bungee.api.ChatColor;
import pl.lakasabasz.mc.planer.cmd.Plan;
import pl.lakasabasz.mc.planer.cmd.Planer;
import pl.lakasabasz.mc.planer.event.BlockController;

@Plugin(name = "Planer", version = "1.0.1.0")
@Description(value = "Plugin obsługujący identyczny drugi świat do planowania budowli")
@Author(value = "Łukasz Łakasabasz Mastalerz")
@LogPrefix("Planer")
@org.bukkit.plugin.java.annotation.command.Command(name = "plan", desc = "Komenda teleportująca", permission = "planer.cmd.plan", usage = "/plan")
@org.bukkit.plugin.java.annotation.command.Command(name = "planer", desc = "Komenda główna", permission = "planer.cmd.planer", usage = "/planer")
@Permission(name = "planer.cmd.plan")
@Permission(name = "planer.cmd.planer")
@Permission(name = "planer.cmd.*", children = {@ChildPermission(name ="planer.cmd.plan"), @ChildPermission(name = "planer.cmd.planer")})
@Permission(name = "planer.*")
@Dependency(value = "WorldEdit")
@ApiVersion(ApiVersion.Target.v1_16)
public class Main extends JavaPlugin {
	
	private World planWorld;
	private static Main instance;
	private Map<Player, TPPlayerPack> positionMap = new HashMap<Player, TPPlayerPack>();
	private List<Area> areaList = new ArrayList<Area>();
	
	@Override
	public void saveConfig() {
		List<Map<String, Object>> areasList = new ArrayList<Map<String, Object>>();
		for(Area a : this.areaList) {
			Map<String, Object> areaSerial = a.serialize();
			areasList.add(areaSerial);
		}
		this.getConfig().set("areas", areasList);
		this.getConfig().set("world", planWorld.getName());
		super.saveConfig();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void onEnable() {
		this.saveDefaultConfig();
		instance = this;
		List<Map<?, ?>> areasList = this.getConfig().getMapList("areas");
		for(Map<?, ?> area : areasList) {
			Area a = new Area();
			if(!a.deserialize((Map<String, Object>) area)) {
				Bukkit.getLogger().log(Level.SEVERE, "Cannot load area, please save config backup before perform any command overize this record will be removed");
			} else {
				this.areaList.add(a);
			}
		}
		String world = this.getConfig().getString("world");
		if(world == null) {
			Bukkit.getLogger().log(Level.SEVERE, "Error in config.yml, cannot get world name");
			this.setEnabled(false);
			return;
		}
		this.planWorld = Bukkit.getWorld(world);
		if(this.planWorld == null) {
			Bukkit.getLogger().log(Level.SEVERE, "Error in config.yml, cannot load world " + world);
			this.setEnabled(false);
			return;
		}
		Bukkit.getPluginManager().registerEvents(new BlockController(), this);
		Bukkit.getLogger().log(Level.INFO, "[Planer] " + ChatColor.GOLD + "Loaded");
	}
	
	@Override
	public void onDisable() {
		this.saveConfig();
		Bukkit.getLogger().log(Level.INFO, "[Planer] " + ChatColor.GOLD + "Unloaded");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			Bukkit.getLogger().log(Level.WARNING, "[Planer] Ta komenda jest możliwa do uruchomienia tylko przez gracza");
		}
		if(cmd.getName().equalsIgnoreCase("plan") && cmd.testPermission(sender)) {
			Plan.exec((Player) sender);
			return true;
		}
		else if(cmd.getName().equalsIgnoreCase("planer") && cmd.testPermission(sender)) {
			Planer.exec((Player) sender, args);
			return true;
		}
		return false;
	}

	public static Main getInstance() {
		return instance;
	}

	public void addArea(Area area) {
		this.areaList.add(area);
	}

	public List<Area> getAreaList() {
		return this.areaList;
	}

	public void removeAreaAt(int id) {
		this.areaList.remove(id);
	}

	public World getWorld() {
		return planWorld;
	}

	public Map<Player, TPPlayerPack> getPlayersInAreas() {
		return positionMap;
	}

	public void addTPPPack(Player p, TPPlayerPack tpppack) {
		this.positionMap.put(p, tpppack);
	}

	public void removePlayerInArea(Player sender) {
		this.positionMap.remove(sender);
	}
}
