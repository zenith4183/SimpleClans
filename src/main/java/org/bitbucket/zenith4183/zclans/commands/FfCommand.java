package org.bitbucket.zenith4183.zclans.commands;

import org.bitbucket.zenith4183.zclans.ChatBlock;
import org.bitbucket.zenith4183.zclans.ClanPlayer;
import org.bitbucket.zenith4183.zclans.zClans;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.MessageFormat;

/**
 * @author phaed
 */
public class FfCommand
{
    public FfCommand()
    {
    }

    /**
     * Execute the command
     *
     * @param player
     * @param arg
     */
    public void execute(Player player, String[] arg)
    {
        zClans plugin = zClans.getInstance();

        if (plugin.getPermissionsManager().has(player, "zclans.member.ff"))
        {
            ClanPlayer cp = plugin.getClanManager().getClanPlayer(player);

            if (cp != null)
            {
                if (arg.length == 1)
                {
                    String action = arg[0];

                    if (action.equalsIgnoreCase(plugin.getLang("allow")))
                    {
                        cp.setFriendlyFire(true);
                        plugin.getStorageManager().updateClanPlayer(cp);
                        ChatBlock.sendMessage(player, ChatColor.AQUA + plugin.getLang("personal.friendly.fire.is.set.to.allowed"));
                    }
                    else if (action.equalsIgnoreCase(plugin.getLang("auto")))
                    {
                        cp.setFriendlyFire(false);
                        plugin.getStorageManager().updateClanPlayer(cp);
                        ChatBlock.sendMessage(player, ChatColor.AQUA + plugin.getLang("friendy.fire.is.now.managed.by.your.clan"));
                    }
                    else
                    {
                        ChatBlock.sendMessage(player, ChatColor.RED + MessageFormat.format(plugin.getLang("usage.0.ff.allow.auto"), plugin.getSettingsManager().getCommandClan()));
                    }
                }
                else
                {
                    ChatBlock.sendMessage(player, ChatColor.RED + MessageFormat.format(plugin.getLang("usage.0.ff.allow.auto"), plugin.getSettingsManager().getCommandClan()));
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
