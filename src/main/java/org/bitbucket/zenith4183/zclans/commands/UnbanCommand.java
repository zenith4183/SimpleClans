package org.bitbucket.zenith4183.zclans.commands;

import org.bitbucket.zenith4183.zclans.ChatBlock;
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
public class UnbanCommand
{
    public UnbanCommand()
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

        if (plugin.getPermissionsManager().has(player, "zclans.mod.ban"))
        {
            if (arg.length == 1)
            {
                String banned = arg[0];

                ClanPlayer cp = plugin.getClanManager().getCreateClanPlayerUUID(banned);
                
                if (cp != null && cp.isBanned())
                {
                    Player pl = Helper.getPlayer(banned);

                    if (pl != null)
                    {
                        ChatBlock.sendMessage(pl, ChatColor.AQUA + plugin.getLang("you.have.been.unbanned.from.clan.commands"));
                    }

                    plugin.getClanManager().unban(banned);
                    ChatBlock.sendMessage(player, ChatColor.AQUA + plugin.getLang("player.removed.from.the.banned.list"));
                }
                else
                {
                    ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("this.player.is.not.banned"));
                }
            }
            else
            {
                ChatBlock.sendMessage(player, ChatColor.RED + MessageFormat.format(plugin.getLang("usage.ban.unban"), plugin.getSettingsManager().getCommandClan()));
            }
        }
        else
        {
            ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("insufficient.permissions"));
        }
    }
}
