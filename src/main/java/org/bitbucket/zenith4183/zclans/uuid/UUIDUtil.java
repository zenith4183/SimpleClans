package org.bitbucket.zenith4183.zclans.uuid;

import java.util.UUID;
import org.bitbucket.zenith4183.zclans.zClans;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;


public class UUIDUtil {
	
	public static UUID nameToUUID(String name) {
		Player player = zClans.getInstance().getServer().getPlayerExact(name);
		
		if (player != null) {
			return player.getUniqueId();
		}
		else {
			@SuppressWarnings("deprecation")
			OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
			return offlinePlayer.getUniqueId();
		}
				
	}
	
	public static Player nameToPlayer(String name) {
		Player player = zClans.getInstance().getServer().getPlayerExact(name);
		
		return player;
	}
	
	public static UUID stringToUUID(String string) {
		UUID uuid = UUID.fromString(string);
		return uuid;		
	}
	
	public static String UUIDToName(UUID uuid) {
		Player player = zClans.getInstance().getServer().getPlayer(uuid);
		
		if (player != null) {
			return player.getName();
		}
		else {
			OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
			return offlinePlayer.getName();
		}
		
	}
	
	public static String stringUUIDToName(String uuid) {
		return UUIDToName(stringToUUID(uuid));
	}

}
