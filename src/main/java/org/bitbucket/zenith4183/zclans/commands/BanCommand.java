package org.bitbucket.zenith4183.zclans.commands;

import org.bitbucket.zenith4183.zclans.ChatBlock;
import org.bitbucket.zenith4183.zclans.ClanPlayer;
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
public class BanCommand
{
    public BanCommand()
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

                if (cp == null) {
                    ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("no.player.matched"));
                    return;
                }
                
                if (!cp.isBanned())
                {
                    UUID PlayerUniqueId = UUIDUtil.nameToUUID(banned);
                    Player pl = zClans.getInstance().getServer().getPlayer(PlayerUniqueId);

                    if (pl != null)
                    {
                        ChatBlock.sendMessage(pl, ChatColor.AQUA + plugin.getLang("you.banned"));
                    }

                    cp.banPlayer();
                    plugin.getClanManager().ban(banned);
                    ChatBlock.sendMessage(player, ChatColor.AQUA + plugin.getLang("player.added.to.banned.list"));
                }
                else
                {
                    ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("this.player.is.already.banned"));
                }

            }
            else
            {
                ChatBlock.sendMessage(player, MessageFormat.format(plugin.getLang("usage.ban.unban"), ChatColor.RED, plugin.getSettingsManager().getCommandClan()));
            }
        }
        else
        {
            ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("insufficient.permissions"));
        }
    }
}
