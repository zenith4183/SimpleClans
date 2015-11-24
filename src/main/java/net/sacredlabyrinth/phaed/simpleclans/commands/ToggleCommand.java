package net.sacredlabyrinth.phaed.simpleclans.commands;

import net.sacredlabyrinth.phaed.simpleclans.ChatBlock;
import net.sacredlabyrinth.phaed.simpleclans.Clan;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;
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
     *
     * @param player
     * @param arg
     */
    public void execute(Player player, String[] arg) {
        SimpleClans plugin = SimpleClans.getInstance();

        if (arg.length == 0) {
            return;
        }

        String cmd = arg[0];

        if (cmd.equalsIgnoreCase("bb")) {
            if (plugin.getPermissionsManager().has(player, "simpleclans.member.bb-toggle")) {
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
        }

        if (cmd.equalsIgnoreCase("tag")) {
            if (plugin.getPermissionsManager().has(player, "simpleclans.member.tag-toggle")) {
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
        }

        if (cmd.equalsIgnoreCase("deposit")) {
            if (plugin.getPermissionsManager().has(player, "simpleclans.leader.deposit-toggle")) {
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
        }

        if (cmd.equalsIgnoreCase("withdraw")) {
            if (plugin.getPermissionsManager().has(player, "simpleclans.leader.withdraw-toggle")) {
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
}
