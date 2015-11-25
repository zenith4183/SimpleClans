package net.sacredlabyrinth.phaed.simpleclans.uuid;

import java.util.UUID;
import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;


public class UUIDUtil {
	
	public static UUID nameToUUID(String name) {
		Player player = SimpleClans.getInstance().getServer().getPlayerExact(name);
		
		if (player != null) {
			return player.getUniqueId();
		}
		else {
			@SuppressWarnings("deprecation")
			OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
			return offlinePlayer.getUniqueId();
		}
				
	}
	
	public static UUID stringToUUID(String string) {
		UUID uuid = UUID.fromString(string);
		return uuid;		
	}
	
	public static String UUIDToName(UUID uuid) {
		Player player = SimpleClans.getInstance().getServer().getPlayer(uuid);
		
		if (player != null) {
			return player.getName();
		}
		else {
			@SuppressWarnings("deprecation")
			OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
			return offlinePlayer.getName();
		}
		
	}
	
	public static String stringUUIDToName(String uuid) {
		return UUIDToName(stringToUUID(uuid));
	}

}
