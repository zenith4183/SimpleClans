package org.bitbucket.zenith4183.zclans.commands;

import org.bitbucket.zenith4183.zclans.ChatBlock;
import org.bitbucket.zenith4183.zclans.Clan;
import org.bitbucket.zenith4183.zclans.ClanPlayer;
import org.bitbucket.zenith4183.zclans.zClans;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * @author phaed
 */
public class ToggleCommand {

    public ToggleCommand() {
    }

    /**
     * Execute the command
     * @param player    player executing command
     * @param arg       command arguments
     */
    public void execute(Player player, String[] arg) {
        zClans plugin = zClans.getInstance();

        if (arg.length == 0) {
            return;
        }

        String cmd = arg[0];

        if (cmd.equalsIgnoreCase("bb") && plugin.getPermissionsManager().has(player, "zclans.member.bb-toggle")) {
        	ClanPlayer cp = plugin.getClanManager().getClanPlayer(player);

        	if (cp != null) {
                Clan clan = cp.getClan();

                if (clan.isVerified()) {
                    if (cp.isBbEnabled()) {
                        ChatBlock.sendMessage(player, ChatColor.AQUA + plugin.getLang("bboff"));
                        cp.setBbEnabled(false);
                    } else {
                        ChatBlock.sendMessage(player, ChatColor.AQUA + plugin.getLang("bbon"));
                        cp.setBbEnabled(true);
                    }
                    plugin.getStorageManager().updateClanPlayer(cp);
                }
            }
        }

        if (cmd.equalsIgnoreCase("tag") && plugin.getPermissionsManager().has(player, "zclans.member.tag-toggle")) {
        	ClanPlayer cp = plugin.getClanManager().getClanPlayer(player);

            if (cp != null) {
                Clan clan = cp.getClan();

                if (clan.isVerified()) {
                    if (cp.isTagEnabled()) {
                        ChatBlock.sendMessage(player, ChatColor.AQUA + plugin.getLang("tagoff"));
                        cp.setTagEnabled(false);
                    } else {
                        ChatBlock.sendMessage(player, ChatColor.AQUA + plugin.getLang("tagon"));
                        cp.setTagEnabled(true);
                    }
                    plugin.getStorageManager().updateClanPlayer(cp);
                }
            }
        }

        if (cmd.equalsIgnoreCase("deposit") && plugin.getPermissionsManager().has(player, "zclans.leader.deposit-toggle")) {
        	ClanPlayer cp = plugin.getClanManager().getClanPlayer(player);

            if (cp != null) {
                Clan clan = cp.getClan();
                if (clan.isLeader(player)) {
                    if (clan.isVerified()) {
                        clan.setAllowDeposit(!clan.isAllowDeposit());
                    }
                } else {
                    ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("no.leader.permissions"));
                }
            }
        }

        if (cmd.equalsIgnoreCase("withdraw") && plugin.getPermissionsManager().has(player, "zclans.leader.withdraw-toggle")) {
        	ClanPlayer cp = plugin.getClanManager().getClanPlayer(player);
            if (cp != null) {
                Clan clan = cp.getClan();
                if (clan.isVerified()) {
                    if (clan.isLeader(player)) {
                        clan.setAllowWithdraw(!clan.isAllowWithdraw());
                    } else {
                        ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("no.leader.permissions"));
                    }
                }
            }
        }
    }
}
