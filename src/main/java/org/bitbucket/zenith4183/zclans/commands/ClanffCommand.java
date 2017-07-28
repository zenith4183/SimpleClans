package org.bitbucket.zenith4183.zclans.commands;

import org.bitbucket.zenith4183.zclans.ChatBlock;
import org.bitbucket.zenith4183.zclans.Clan;
import org.bitbucket.zenith4183.zclans.ClanPlayer;
import org.bitbucket.zenith4183.zclans.zClans;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.MessageFormat;

/**
 *
 * @author phaed
 */
public class ClanffCommand {
    public ClanffCommand() {
    }

    /**
     * Execute the command
     * @param player    player executing command
     * @param arg       command arguments
     */
    public void execute(Player player, String[] arg) {
        zClans plugin = zClans.getInstance();

        if (!plugin.getPermissionsManager().has(player, "zclans.leader.ff")) {
            ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("insufficient.permissions"));
            return;
        }

        ClanPlayer cp = plugin.getClanManager().getClanPlayer(player);

        if (cp == null) {
            ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("not.a.member.of.any.clan"));
            return;
        }

        Clan clan = cp.getClan();

        if (!clan.isLeader(player)) {
            ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("no.leader.permissions"));
            return;
        }

        if (arg.length != 1) {
            ChatBlock.sendMessage(player, ChatColor.RED + MessageFormat.format(plugin.getLang("usage.clanff"), plugin.getSettingsManager().getCommandClan()));
            return;
        }

        String action = arg[0];

        if (action.equalsIgnoreCase(plugin.getLang("allow"))) {
            clan.addBb(player.getName(), ChatColor.AQUA + plugin.getLang("clan.wide.friendly.fire.is.allowed"));
            clan.setFriendlyFire(true);
            plugin.getStorageManager().updateClan(clan);
        }
        else if (action.equalsIgnoreCase(plugin.getLang("block")))  {
            clan.addBb(player.getName(), ChatColor.AQUA + plugin.getLang("clan.wide.friendly.fire.blocked"));
            clan.setFriendlyFire(false);
            plugin.getStorageManager().updateClan(clan);
        }
        else {
            ChatBlock.sendMessage(player, ChatColor.RED + MessageFormat.format(plugin.getLang("usage.clanff"), plugin.getSettingsManager().getCommandClan()));
        }

    }
}
