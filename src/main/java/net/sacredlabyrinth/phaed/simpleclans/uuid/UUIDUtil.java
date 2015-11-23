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

}
