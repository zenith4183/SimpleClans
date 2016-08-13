package org.bitbucket.zenith4183.zclans.commands;

import org.bitbucket.zenith4183.zclans.*;
import org.bitbucket.zenith4183.zclans.uuid.UUIDUtil;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.MessageFormat;
import java.util.UUID;

public class SetRankCommand
{
    public SetRankCommand()
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

        if (plugin.getPermissionsManager().has(player, "zclans.leader.setrank"))
        {
            ClanPlayer cp = plugin.getClanManager().getClanPlayer(player);

            if (cp != null)
            {
                Clan clan = cp.getClan();

                if (clan.isVerified())
                {
                    if (clan.isLeader(player))
                    {
                        if (arg.length >= 1)
                        {
                            String playerName = arg[0];
                            String rank = Helper.toMessage(Helper.removeFirst(arg));
                            UUID playerUUID = UUIDUtil.nameToUUID(playerName);
                            
                            if (clan.isMember(playerUUID) || clan.isLeader(playerUUID))
                            {
                                ClanPlayer cpm = plugin.getClanManager().getClanPlayer(UUIDUtil.nameToUUID(playerName));
                                cpm.setRank(rank);
                                plugin.getStorageManager().updateClanPlayer(cpm);

                                ChatBlock.sendMessage(player, ChatColor.AQUA + plugin.getLang("player.rank.changed"));
                            }
                            else
                            {
                                ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("no.player.matched"));
                            }
                        }
                        else
                        {
                            ChatBlock.sendMessage(player, ChatColor.RED + MessageFormat.format(plugin.getLang("usage.setrank"), plugin.getSettingsManager().getCommandClan()));
                        }
                    }
                    else
                    {
                        ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("no.leader.permissions"));
                    }
                }
                else
                {
                    ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("clan.is.not.verified"));
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
