package pl.lakasabasz.mc.planer;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.plugin.java.annotation.permission.Permission;
@Permission(name = "planer.cmd.plan")
@Permission(name = "planer.cmd.planer")
@Permission(name = "planer.cmd.*")
@Permission(name = "planer.*")
public class Permissions {
	public enum Type{
		ALL,
		COMMAND_ALL,
		COMMAND_PLAN,
		COMMAND_PLANER
	}
	static private Map<Type, String> base = new HashMap<Type, String>();
	static {
		base.put(Type.ALL, "planer.*");
		base.put(Type.COMMAND_ALL, "planer.cmd.*");
		base.put(Type.COMMAND_PLAN, "planer.cmd.plan");
		base.put(Type.COMMAND_PLANER, "planer.cmd.planer");
	}
	
	static public String get(Type key) {
		return base.get(key);
	}
}
