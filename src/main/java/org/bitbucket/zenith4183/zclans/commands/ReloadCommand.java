package org.bitbucket.zenith4183.zclans.commands;

import org.bitbucket.zenith4183.zclans.ChatBlock;
import org.bitbucket.zenith4183.zclans.Clan;
import org.bitbucket.zenith4183.zclans.zClans;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author phaed
 */
public class ReloadCommand
{
    public ReloadCommand()
    {
    }

    /**
     * Execute the command
     *
     * @param player    player executing command
     * @param arg       command arguments
     */
    public void execute(CommandSender sender, String[] arg)
    {
        zClans plugin = zClans.getInstance();

        if (sender instanceof Player && !plugin.getPermissionsManager().has((Player)sender, "zclans.admin.reload"))
        {
        	ChatBlock.sendMessage(sender, ChatColor.RED + "Does not match a clan command");
        	return;
        }

        plugin.getSettingsManager().load();
        plugin.getLanguageManager().load();
        plugin.getStorageManager().importFromDatabase();
        zClans.getInstance().getPermissionsManager().loadPermissions();

        for (Clan clan : plugin.getClanManager().getClans())
        {
            zClans.getInstance().getPermissionsManager().updateClanPermissions(clan);
        }
        ChatBlock.sendMessage(sender, ChatColor.AQUA + plugin.getLang("configuration.reloaded"));

    }
}
