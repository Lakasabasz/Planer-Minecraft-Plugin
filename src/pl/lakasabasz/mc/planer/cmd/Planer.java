package pl.lakasabasz.mc.planer.cmd;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;

import pl.lakasabasz.mc.planer.Area;
import pl.lakasabasz.mc.planer.Main;

public class Planer {

	private static List<BlockVector3> getSelection(Player p){
		List<BlockVector3> ret = new ArrayList<BlockVector3>();
		WorldEditPlugin worldEdit = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
		try {
			Region sel = worldEdit.getSession(p).getSelection(worldEdit.getSession(p).getSelectionWorld());
			ret.add(sel.getBoundingBox().getPos1());
			ret.add(sel.getBoundingBox().getPos2());
			return ret;
		} catch (IncompleteRegionException | NullPointerException e) {
			return null;
		}
	}
	
	private static boolean create(String name, Player p) {
		List<BlockVector3> rawsel = getSelection(p);
		if(rawsel == null) return false;
		Vector begin = new Vector(rawsel.get(0).getBlockX(), rawsel.get(0).getBlockY(), rawsel.get(0).getBlockZ());
		Vector end = new Vector(rawsel.get(1).getBlockX(), rawsel.get(1).getBlockY(), rawsel.get(1).getBlockZ());
		Area area = new Area(begin, end, p.getLocation());
		area.setName(name);
		Main.getInstance().addArea(area);
		Main.getInstance().saveConfig();
		return true;
	}
	
	private static boolean remove(int id) {
		if(id < 0 || Main.getInstance().getAreaList().size() <= id) return false;
		Main.getInstance().removeAreaAt(id);
		Main.getInstance().saveConfig();
		return true;
	}
	
	private static boolean edit(int id, Player p) {
		List<BlockVector3> rawsel = getSelection(p);
		if(rawsel == null) return false;
		if(id < 0 || Main.getInstance().getAreaList().size() <= id) return false;
		Main.getInstance().getAreaList().get(id).setVectorBegin(new Vector(rawsel.get(0).getBlockX(), rawsel.get(0).getBlockY(), rawsel.get(0).getBlockZ()));
		Main.getInstance().getAreaList().get(id).setVectorEnd(new Vector(rawsel.get(1).getBlockX(), rawsel.get(1).getBlockY(), rawsel.get(1).getBlockZ()));
		Main.getInstance().saveConfig();
		return true;
	}
	
	private static void list(Player p) {
		p.sendMessage("[Planer] Lista dostępnych planów:");
		for(int id = 0; id<Main.getInstance().getAreaList().size(); id++) {
			p.sendMessage(id + "    " + Main.getInstance().getAreaList().get(id).getName());
		}
		p.sendMessage("Łącznie: " + Main.getInstance().getAreaList().size());
	}
	
	public static boolean exec(Player p, String[] args) {
		if(args.length == 1 && args[0].equalsIgnoreCase("list")) { // planer list
			list(p);
			return true;
		} else if(args.length == 2) {
			if(args[0].equalsIgnoreCase("create")) {
				if(!create(args[1], p)) {
					p.sendMessage("[Planer] Nie masz aktywnego zaznaczenia");
				} else {
					p.sendMessage("[Planer] Utworzono nowy plan");
				}
				return true;
			}
			else if(args[0].equalsIgnoreCase("remove")) {
				try {
					int id = Integer.parseInt(args[1]);
					if(remove(id)) {
						p.sendMessage("[Planer] Pomyślnie usunięto plan. Lista id została zmieniona, sprawdź przed wykonianiem komendy");
					} else {
						p.sendMessage("[Planer] Wybrany indeks nie istnieje");
					}
				} catch(NumberFormatException e) {
					p.sendMessage("[Planer] Podaj indeks, a nie nazwę");
				}
				return true;
			} // planer remove
			else if(args[0].equalsIgnoreCase("edit")) {
				try {
					int id = Integer.parseInt(args[1]);
					if(edit(id, p)) {
						p.sendMessage("[Planer] Pomyślnie edytowano plan");
					} else {
						p.sendMessage("[Planer] Wybrany indeks nie istnieje lub nie ma zaznaczonego obszaru");
					}
				} catch(NumberFormatException e) {
					p.sendMessage("[Planer] Podaj indeks, a nie nazwę");
				}
				return true;
			}
		}
		printHelp(p);
		return true;
	}

	private static void printHelp(Player p) {
		p.sendMessage("[Planer] /planer");
		p.sendMessage("/planer list                Wyświetla listę zarejestrowanych planów");
		p.sendMessage("/planer create <nazwa>  Tworzy nowy plan");
		p.sendMessage("/planer remove <id>      Usuwa plan");
		p.sendMessage("/planer edit <id>         Edytuje wymiary planu");
	}
}
