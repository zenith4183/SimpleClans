package org.bitbucket.zenith4183.zclans.commands;

import org.bitbucket.zenith4183.zclans.ChatBlock;
import org.bitbucket.zenith4183.zclans.ClanPlayer;
import org.bitbucket.zenith4183.zclans.zClans;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * @author phaed
 */
public class ResetKDRCommand {

    public ResetKDRCommand() {
    }

    /**
     * Execute the command
     *
     * @param player
     * @param arg
     */
    public void execute(Player player, String[] arg) {
        zClans plugin = zClans.getInstance();
        if (plugin.getPermissionsManager().has(player, "zclans.admin.resetkdr")) {
            for (ClanPlayer cp : zClans.getInstance().getClanManager().getAllClanPlayers()) {
                cp.setCivilianKills(0);
                cp.setNeutralKills(0);
                cp.setRivalKills(0);
                cp.setDeaths(0);
            }
            ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("kdr.of.all.players.was.reset"));
        } else {
            ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("insufficient.permissions"));
        }
    }
}
