package pl.lakasabasz.mc.planer;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public class TPPlayerPack {
	private Area area;
	private ItemStack[] items;
	private Location startingPoint;
	
	public TPPlayerPack() {
		
	}
	public TPPlayerPack(Area a, ItemStack[] i, Location l) {
		area = a;
		items = i;
		startingPoint = l;
	}
	
	public Area getArea() {
		return area;
	}
	public void setArea(Area area) {
		this.area = area;
	}
	public ItemStack[] getItems() {
		return items;
	}
	public void setItems(ItemStack[] items) {
		this.items = items;
	}
	public Location getStartingPoint() {
		return startingPoint;
	}
	public void setStartingPoint(Location startingPoint) {
		this.startingPoint = startingPoint;
	}
}
