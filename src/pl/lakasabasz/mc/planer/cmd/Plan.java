package pl.lakasabasz.mc.planer.cmd;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import pl.lakasabasz.mc.planer.Area;
import pl.lakasabasz.mc.planer.Main;
import pl.lakasabasz.mc.planer.TPPlayerPack;

public class Plan {

	public static void exec(Player sender) {
		if(sender.getLocation().getWorld().equals(Main.getInstance().getWorld())) {
			if(Main.getInstance().getPlayersInAreas().containsKey(sender)) {
				sender.setGameMode(GameMode.SURVIVAL);
				TPPlayerPack tpppack = Main.getInstance().getPlayersInAreas().get(sender);
				sender.getInventory().setContents(tpppack.getItems());
				sender.teleport(tpppack.getStartingPoint());
				Main.getInstance().removePlayerInArea(sender);
			} else {
				try {
					ItemStack[] inventory = loadInventory(sender.getUniqueId().toString());
					sender.getInventory().setContents(inventory);
				} catch (IOException e) {
					sender.sendMessage("[Planer] Nie ma informacji o wcześniejszym użyciu komendy /plan");
					return;
				} catch (InvalidConfigurationException e) {
					sender.sendMessage("[Planer] Plik z zapisanym ekwipunkiem został uszkodzony");
					return;
				}
				sender.setGameMode(GameMode.SURVIVAL);
				sender.teleport(Bukkit.getWorld("world").getSpawnLocation());
			}
		} else if(sender.getLocation().getWorld().getName().equalsIgnoreCase("world")) {
			Location current = sender.getLocation();
			Area where = null;
			for(Area a : Main.getInstance().getAreaList()) {
				if(a.isLocationInside(current)) {
					where = a;
				}
			}
			if(where == null) {
				sender.sendMessage("[Planer] Nie stoisz w miejscu zarejestrownym jako plan");
				return;
			}
			
			UUID playeruuid = sender.getUniqueId();
			YamlConfiguration yc = new YamlConfiguration();
			List<Map<String, Object>> itemList = new ArrayList<Map<String, Object>>();
			for(ItemStack is : sender.getInventory().getContents()) {
				if(is == null) is = new ItemStack(Material.AIR);
				itemList.add(is.serialize());
			}
			yc.set("inventory", itemList);
			try {
				yc.save(new File(Main.getInstance().getDataFolder().getAbsolutePath() + "/" + playeruuid.toString()));
			} catch (IOException e) {
				sender.sendMessage("[Planer] Błąd zapisu pliku z twoim ekwipunkiem");
				e.printStackTrace();
				return;
			}
			
			TPPlayerPack tpppack = new TPPlayerPack(where, sender.getInventory().getContents(), current);
			Main.getInstance().addTPPPack(sender, tpppack);
			sender.getInventory().clear();
			current = current.clone();
			current.setWorld(Main.getInstance().getWorld());
			sender.teleport(current);
			sender.setGameMode(GameMode.CREATIVE);
		}
	}
	
	@SuppressWarnings("unchecked")
	private static ItemStack[] loadInventory(String uuid) throws FileNotFoundException, IOException, InvalidConfigurationException {
		YamlConfiguration yc = new YamlConfiguration();
		yc.load(new File(Main.getInstance().getDataFolder().getAbsoluteFile() + "/" + uuid));
		List<ItemStack> tret = new ArrayList<ItemStack>();
		List<Map<?, ?>> serialized = yc.getMapList("inventory");
		for(Map<?, ?> item : serialized) {
			tret.add(ItemStack.deserialize((Map<String, Object>) item));
		}
		return tret.toArray(new ItemStack[tret.size()]);
	}
}
