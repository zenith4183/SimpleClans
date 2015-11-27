package net.sacredlabyrinth.phaed.simpleclans.managers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;

import org.bukkit.entity.Player;

/**
 * @author zenith4183
 */
public final class KillCampingManager {
	
	private SimpleClans plugin;
	
	public KillCampingManager() {
		plugin = SimpleClans.getInstance();
	}

	public Map<String, ArrayList<String>> playerKills = new HashMap<String, ArrayList<String>>();
	public Map<String, ArrayList<String>> clanKills = new HashMap<String, ArrayList<String>>();
	public Map<String, ArrayList<Long>> playerKillTime = new HashMap<String, ArrayList<Long>>();
	public Map<String, ArrayList<Long>> clanKillTime = new HashMap<String, ArrayList<Long>>();

	public void addPlayerKill(String attackerUuid, String victimUuid) {
		long time = System.currentTimeMillis();
		
		if (!playerKills.containsKey(attackerUuid)) {
			playerKills.put(attackerUuid, new ArrayList<>(Arrays.asList(victimUuid)));
			playerKillTime.put(attackerUuid, new ArrayList<>(Arrays.asList(time)));
			return;
		}
		if (!playerKills.get(attackerUuid).contains(victimUuid)) {
			playerKills.get(attackerUuid).add(victimUuid);
			playerKillTime.get(attackerUuid).add(time);
			return;
		}
		int index = playerKills.get(attackerUuid).indexOf(victimUuid);
		playerKillTime.get(attackerUuid).set(index, time);
	}
	
	public void addClanKill(String clan, String victimUuid) {
		long time = System.currentTimeMillis();
		
		if (!clanKills.containsKey(clan)) {
			clanKills.put(clan, new ArrayList<>(Arrays.asList(victimUuid)));
			clanKillTime.put(clan, new ArrayList<>(Arrays.asList(time)));
			return;
		}
		if (!playerKills.get(clan).contains(victimUuid)) {
			playerKills.get(clan).add(victimUuid);
			playerKillTime.get(clan).add(time);
			return;
		}
		int index = playerKills.get(clan).indexOf(victimUuid);
		playerKillTime.get(clan).set(index, time);
	}
	
	public boolean isPlayerCamping(Player attacker, Player victim) {
		String[] attackerAddress = attacker.getAddress().toString().split(":");
		String[] victimAddress = victim.getAddress().toString().split(":");
		
		if (attackerAddress[0].equals(victimAddress[0])) {
			return true;
		}
				
		ClanPlayer cp = plugin.getClanManager().getCreateClanPlayer(attacker.getUniqueId());
		String clan = cp.getTag();
		
		String attackerUuid = attacker.getUniqueId().toString();
		String victimUuid = victim.getUniqueId().toString();
		long time = System.currentTimeMillis();
		long playerCampingExpireTime = plugin.getSettingsManager().getKillCooldown() * 1000 * 60;
		
		if (playerKills.containsKey(attackerUuid) 
				&& playerKills.get(attackerUuid).contains(victimUuid)) {
			int index = playerKills.get(attackerUuid).indexOf(victimUuid);
			long lastKillTime = playerKillTime.get(attackerUuid).get(index);
			
			if (time - lastKillTime < playerCampingExpireTime) {
				return true;
			}
		}
		
		if (clanKills.containsKey(clan) 
				&& clanKills.get(clan).contains(victimUuid)) {
			int index = clanKills.get(clan).indexOf(victimUuid);
			long lastKillTime = clanKillTime.get(clan).get(index);
			
			if (time - lastKillTime < playerCampingExpireTime) {
				return true;
			}
		}
		return false;
	}
	
}
