package pl.lakasabasz.mc.planer.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import pl.lakasabasz.mc.planer.Area;
import pl.lakasabasz.mc.planer.Main;

public class BlockController implements Listener {
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockPlace(BlockPlaceEvent e) {
		if(!e.getBlock().getLocation().getWorld().equals(Main.getInstance().getWorld())) return;
		if(!Main.getInstance().getPlayersInAreas().containsKey(e.getPlayer())) {
			e.setCancelled(true);
		}
		Area where = Main.getInstance().getPlayersInAreas().get(e.getPlayer()).getArea();
		if(!where.isLocationInside(e.getBlock().getLocation())) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockBreak(BlockBreakEvent e) {
		if(!e.getBlock().getLocation().getWorld().equals(Main.getInstance().getWorld())) return;
		if(!Main.getInstance().getPlayersInAreas().containsKey(e.getPlayer())) {
			e.setCancelled(true);
		}
		Area where = Main.getInstance().getPlayersInAreas().get(e.getPlayer()).getArea();
		if(!where.isLocationInside(e.getBlock().getLocation())) {
			e.setCancelled(true);
		}
	}
}
