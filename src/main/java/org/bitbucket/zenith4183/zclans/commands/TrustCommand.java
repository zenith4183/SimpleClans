package org.bitbucket.zenith4183.zclans.commands;

import org.bitbucket.zenith4183.zclans.ChatBlock;
import org.bitbucket.zenith4183.zclans.Clan;
import org.bitbucket.zenith4183.zclans.ClanPlayer;
import org.bitbucket.zenith4183.zclans.Helper;
import org.bitbucket.zenith4183.zclans.zClans;
import org.bitbucket.zenith4183.zclans.uuid.UUIDUtil;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.MessageFormat;

/**
 *
 * @author phaed
 */
public class TrustCommand
{
    public TrustCommand()
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

        if (plugin.getPermissionsManager().has(player, "zclans.leader.settrust"))
        {
            ClanPlayer cp = plugin.getClanManager().getClanPlayer(player);

            if (cp != null)
            {
                Clan clan = cp.getClan();

                if (clan.isLeader(player))
                {
                    if (arg.length == 1)
                    {
                        String trusted = arg[0];

                        if (trusted != null)
                        {
                            if (!trusted.equals(player.getName()))
                            {
                                if (clan.isMember(UUIDUtil.nameToUUID(trusted)))
                                {
                                    if (!clan.isLeader(UUIDUtil.nameToUUID(trusted)))
                                    {
                                        ClanPlayer tcp = plugin.getClanManager().getClanPlayerName(trusted);
                                        if (tcp == null) 
                                        {
                                            ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("no.player.matched"));
                                            return;
                                        }
                                        
                                        if (!tcp.isTrusted())
                                        {
                                            clan.addBb(player.getName(), ChatColor.AQUA + MessageFormat.format(plugin.getLang("has.been.given.trusted.status.by"), Helper.capitalize(trusted), player.getName()));
                                            tcp.setTrusted(true);
                                            plugin.getStorageManager().updateClanPlayer(tcp);
                                        }
                                        else
                                        {
                                            ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("this.player.is.already.trusted"));
                                        }
                                    }
                                    else
                                    {
                                        ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("leaders.are.already.trusted"));
                                    }
                                }
                                else
                                {
                                    ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("the.player.is.not.a.member.of.your.clan"));
                                }
                            }
                            else
                            {
                                ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("you.cannot.trust.yourself"));
                            }
                        }
                        else
                        {
                            ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("no.player.matched"));
                        }
                    }
                    else
                    {
                        ChatBlock.sendMessage(player, ChatColor.RED + MessageFormat.format(plugin.getLang("usage.0.trust.player"), plugin.getSettingsManager().getCommandClan()));
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
