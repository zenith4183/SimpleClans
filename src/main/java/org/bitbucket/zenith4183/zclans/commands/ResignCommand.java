package org.bitbucket.zenith4183.zclans.commands;

import org.bitbucket.zenith4183.zclans.ChatBlock;
import org.bitbucket.zenith4183.zclans.Clan;
import org.bitbucket.zenith4183.zclans.ClanPlayer;
import org.bitbucket.zenith4183.zclans.Helper;
import org.bitbucket.zenith4183.zclans.zClans;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.MessageFormat;

/**
 *
 * @author phaed
 */
public class ResignCommand
{
    public ResignCommand()
    {
    }

    /**
     * Execute the command
     * @param player    player executing command
     * @param arg       command arguments
     */
    public void execute(Player player, String[] arg)
    {
        zClans plugin = zClans.getInstance();

        if (plugin.getPermissionsManager().has(player, "zclans.member.resign"))
        {
            ClanPlayer cp = plugin.getClanManager().getClanPlayer(player);

            if (cp != null)
            {
                Clan clan = cp.getClan();

                if (!clan.isLeader(player) || clan.getLeaders().size() > 1)
                {
                    clan.addBb(player.getName(), ChatColor.AQUA + MessageFormat.format(plugin.getLang("0.has.resigned"), Helper.capitalize(player.getName())));

                    clan.removePlayerFromClan(player.getUniqueId());

                }
                else if (clan.isLeader(player) && clan.getLeaders().size() == 1)
                {
                    plugin.getClanManager().serverAnnounce(ChatColor.AQUA + MessageFormat.format(plugin.getLang("clan.has.been.disbanded"), clan.getName()));
                    clan.disband();
                }
                else
                {
                    ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("last.leader.cannot.resign.you.must.appoint.another.leader.or.disband.the.clan"));
                }
            }
            else
            {
                ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("not.a.member.of.any.clan"));
            }
        }
        else
        {
            ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("insufficient.permissions"));
        }
    }
}
