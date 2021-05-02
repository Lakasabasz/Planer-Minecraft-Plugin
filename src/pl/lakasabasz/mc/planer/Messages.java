package pl.lakasabasz.mc.planer;

import java.util.HashMap;
import java.util.Map;

import net.md_5.bungee.api.ChatColor;

public class Messages {

	public enum MessagesEnum{
		CONSOLE_SENDER_ERROR,
		PERMISSION_ERROR
	}
	private static Map<MessagesEnum, String> msgs = new HashMap<MessagesEnum, String>();
	static {
		msgs.put(MessagesEnum.CONSOLE_SENDER_ERROR, "[ERROR] " + ChatColor.DARK_RED + "This command is only for players");
		msgs.put(MessagesEnum.PERMISSION_ERROR, "[ERROR] " + ChatColor.DARK_RED + "To perform this action you need permissions");
	}
	public static String get(MessagesEnum key) {
		return msgs.get(key);
	}
}
