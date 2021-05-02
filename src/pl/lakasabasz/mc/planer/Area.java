package pl.lakasabasz.mc.planer;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class Area {
	private Vector begin;
	private Vector end;
	private Location spawn;
	private String name;
	
	public Area() {
	
	}
	
	public Area(Vector begin, Vector end, Location spawn) {
		this.begin = begin;
		this.end = end;
		this.spawn = spawn;
	}
	
	public Map<String, Object> serialize(){
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("begin", this.begin.serialize());
		ret.put("end", end.serialize());
		ret.put("spawn", this.spawn.serialize());
		ret.put("name", name);
		return ret;
	}
	
	@SuppressWarnings("unchecked")
	public boolean deserialize(Map<String, Object> area) {
		if(!(area.containsKey("begin") && area.containsKey("end") && area.containsKey("spawn") && area.containsKey("name"))) return false;
		this.begin = Vector.deserialize((Map<String, Object>) area.get("begin"));
		this.end = Vector.deserialize((Map<String, Object>) area.get("end"));
		this.spawn = Location.deserialize((Map<String, Object>) area.get("spawn"));
		this.name = (String) area.get("name");
		return true;
	}
	
	public boolean isLocationInside(Location l) {
		double x[] = {begin.getX(), end.getX()};
		if(x[0] > x[1]) {
			double t = x[0];
			x[0] = x[1];
			x[1] = t;
		}
		double z[] = {begin.getZ(), end.getZ()};
		if(z[0] > z[1]) {
			double t = z[0];
			z[0] = z[1];
			z[1] = t;
		}
		
		return (z[0] <= l.getZ() && l.getZ() <= z[1]) && (x[0] <= l.getX() && l.getX() <= x[1]);
	}
	
	public void setVectorBegin(Vector begin) {
		this.begin = begin;
	}
	
	public void setVectorEnd(Vector end) {
		this.end = end;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}
}
