package org.bitbucket.zenith4183.zclans.commands;

import org.bitbucket.zenith4183.zclans.ChatBlock;
import org.bitbucket.zenith4183.zclans.Clan;
import org.bitbucket.zenith4183.zclans.ClanPlayer;
import org.bitbucket.zenith4183.zclans.Helper;
import org.bitbucket.zenith4183.zclans.zClans;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.MessageFormat;
import java.util.UUID;
import org.bitbucket.zenith4183.zclans.uuid.UUIDUtil;

/**
 *
 * @author phaed
 */
public class DemoteCommand
{
    public DemoteCommand()
    {
    }

    /**
     * Execute the command
     * @param player
     * @param arg
     */
    public void execute(Player player, String[] arg)
    {
        zClans plugin = zClans.getInstance();

        if (plugin.getPermissionsManager().has(player, "zclans.leader.demote"))
        {
            ClanPlayer cp = plugin.getClanManager().getClanPlayer(player);

            if (cp != null)
            {
                Clan clan = cp.getClan();

                if (clan.isLeader(player))
                {
                    if (arg.length == 1)
                    {
                        String demotedName = arg[0];
                        boolean allOtherLeadersOnline;
                        
                        if (demotedName == null) 
                        {
                            ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("no.player.matched"));
                            return;
                        }
                        
                        UUID PlayerUniqueId = UUIDUtil.nameToUUID(demotedName);
                        if (PlayerUniqueId == null)
                        {
                            ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("no.player.matched"));
                            return;
                        }
                        allOtherLeadersOnline = clan.allOtherLeadersOnline(PlayerUniqueId);

                        if (allOtherLeadersOnline)
                        {
                            if (clan.isLeader(PlayerUniqueId))
                            {
                                if (clan.getLeaders().size() == 1|| !plugin.getSettingsManager().isConfirmationForDemote())
                                {
                                    clan.addBb(player.getName(), ChatColor.AQUA + MessageFormat.format(plugin.getLang("demoted.back.to.member"), Helper.capitalize(demotedName)));
                                    clan.demote(PlayerUniqueId);
                                }
                                else
                                {
                                    plugin.getRequestManager().addDemoteRequest(cp, demotedName, clan);
                                    ChatBlock.sendMessage(player, ChatColor.AQUA + plugin.getLang("demotion.vote.has.been.requested.from.all.leaders"));
                                }
                            }
                            else
                            {
                                ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("player.is.not.a.leader.of.your.clan"));
                            }


                        }
                        else
                        {
                            ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("leaders.must.be.online.to.vote.on.demotion"));
                        }
                    }
                    else
                    {
                        ChatBlock.sendMessage(player, ChatColor.RED + MessageFormat.format(plugin.getLang("usage.demote.leader"), plugin.getSettingsManager().getCommandClan()));
                    }
                }
                else
                {
                    ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("no.leader.permissions"));
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
