package org.bitbucket.zenith4183.zclans.managers;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bitbucket.zenith4183.zclans.ClanPlayer;
import org.bitbucket.zenith4183.zclans.zClans;

import org.bukkit.entity.Player;

/**
 * @author zenith4183
 */
public final class KillCampingManager {
	
	private zClans plugin;
	
	public KillCampingManager() {
		plugin = zClans.getInstance();
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
		if (!clanKills.get(clan).contains(victimUuid)) {
			clanKills.get(clan).add(victimUuid);
			clanKillTime.get(clan).add(time);
			return;
		}
		int index = clanKills.get(clan).indexOf(victimUuid);
		clanKillTime.get(clan).set(index, time);
	}
	
	public boolean isPlayerCamping(Player attacker, Player victim) {
		if (plugin.getSettingsManager().isBlockIPCamping()) {
			InetSocketAddress attackerAddressFull = attacker.getAddress();
			InetSocketAddress victimAddressFull = victim.getAddress();

			if (attackerAddressFull != null && victimAddressFull != null) {
				String[] attackerAddress = attacker.getAddress().toString().split(":");
				String[] victimAddress = victimAddressFull.toString().split(":");

				if (attackerAddress[0].equals(victimAddress[0])) {
					return true;
				}
			}
		}
		
		ClanPlayer acp = plugin.getClanManager().getCreateClanPlayer(attacker.getUniqueId());
		String aclan = acp.getTag();
		
		String attackerUuid = attacker.getUniqueId().toString();
		String victimUuid = victim.getUniqueId().toString();
		long time = System.currentTimeMillis();
		long playerCampingExpireTime = plugin.getSettingsManager().getKillCooldown() * 1000 * 60;
		boolean clanWide = plugin.getSettingsManager().getCooldownClanWide();
		
		if (playerKills.containsKey(attackerUuid) 
				&& playerKills.get(attackerUuid).contains(victimUuid)) {
			int index = playerKills.get(attackerUuid).indexOf(victimUuid);
			long lastKillTime = playerKillTime.get(attackerUuid).get(index);
			
			if (time - lastKillTime < playerCampingExpireTime) {
				return true;
			}
		}
		
		if (clanWide) {
			if (clanKills.containsKey(aclan) 
					&& clanKills.get(aclan).contains(victimUuid)) {
				int index = clanKills.get(aclan).indexOf(victimUuid);
				long lastKillTime = clanKillTime.get(aclan).get(index);
				
				if (time - lastKillTime < playerCampingExpireTime) {
					return true;
				}
			}
		}
		
		if (playerKills.containsKey(victimUuid) 
				&& playerKills.get(victimUuid).contains(attackerUuid)) {
			int index = playerKills.get(victimUuid).indexOf(attackerUuid);
			long lastKillTime = playerKillTime.get(victimUuid).get(index);
			
			if (time - lastKillTime < playerCampingExpireTime) {
				return true;
			}			
		}
		
		
		if (clanWide) {
			ClanPlayer vcp = plugin.getClanManager().getCreateClanPlayer(victim.getUniqueId());
			String vclan = vcp.getTag();

			if (vclan != null) {
				if (clanKills.containsKey(vclan) 
						&& clanKills.get(vclan).contains(attackerUuid)) {
					int index = clanKills.get(vclan).indexOf(attackerUuid);
					long lastKillTime = clanKillTime.get(vclan).get(index);
					
					if (time - lastKillTime < playerCampingExpireTime) {
						return true;
					}
				}
			}
		}
	
		return false;
	}
	
}
