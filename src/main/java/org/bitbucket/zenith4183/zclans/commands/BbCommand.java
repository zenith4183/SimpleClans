package org.bitbucket.zenith4183.zclans.commands;

import org.bitbucket.zenith4183.zclans.*;
import org.bitbucket.zenith4183.zclans.*;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * @author phaed
 */
public class BbCommand {

    public BbCommand() {
    }

    /**
     * Execute the command
     *
     * @param player
     * @param arg
     */
    public void execute(Player player, String[] arg) {
        zClans plugin = zClans.getInstance();

        ClanPlayer cp = plugin.getClanManager().getClanPlayer(player);

        if (cp != null) {
            Clan clan = cp.getClan();

            if (clan.isVerified()) {
                if (arg.length == 0) {
                    if (plugin.getPermissionsManager().has(player, "zclans.member.bb")) {
                        clan.displayBb(player);
                    } else {
                        ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("insufficient.permissions"));
                    }
                } else if (arg.length == 1 && arg[0].equalsIgnoreCase("clear")) {
                    if (plugin.getPermissionsManager().has(player, "zclans.leader.bb-clear")) {
                        if (cp.isTrusted() && cp.isLeader()) {
                            cp.getClan().clearBb();
                            ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("cleared.bb"));
                        } else {
                            ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("no.leader.permissions"));
                        }
                    } else {
                        ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("insufficient.permissions"));
                    }
                } else if (plugin.getPermissionsManager().has(player, "zclans.member.bb-add")) {
                    if (cp.isTrusted()) {
                        String msg = Helper.toMessage(arg);
                        clan.addBb(player.getName(), ChatColor.AQUA + player.getName() + ": " + ChatColor.WHITE + msg);
                        plugin.getStorageManager().updateClan(clan);
                    } else {
                        ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("no.leader.permissions"));
                    }
                } else {
                    ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("insufficient.permissions"));
                }
            } else {
                ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("clan.is.not.verified"));
            }
        } else {
            ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("not.a.member.of.any.clan"));
        }
    }
}