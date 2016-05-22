package org.bitbucket.zenith4183.zclans.executors;

import org.bitbucket.zenith4183.zclans.ChatBlock;
import org.bitbucket.zenith4183.zclans.ClanPlayer;
import org.bitbucket.zenith4183.zclans.zClans;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.MessageFormat;

public class MoreCommandExecutor implements CommandExecutor
{
    zClans plugin;

    public MoreCommandExecutor()
    {
        plugin = zClans.getInstance();
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings)
    {
        Player player = (Player) commandSender;

        ClanPlayer cp = plugin.getClanManager().getClanPlayer(player);
        
        if (cp != null && cp.isBanned())
        {
            ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("banned"));
            return false;
        }

        ChatBlock chatBlock = plugin.getStorageManager().getChatBlock(player);

        if (chatBlock != null && chatBlock.size() > 0)
        {
            chatBlock.sendBlock(player, plugin.getSettingsManager().getPageSize());

            if (chatBlock.size() > 0)
            {
                ChatBlock.sendBlank(player);
                ChatBlock.sendMessage(player, plugin.getSettingsManager().getPageHeadingsColor() + MessageFormat.format(plugin.getLang("view.next.page"), plugin.getSettingsManager().getCommandMore()));
            }
            ChatBlock.sendBlank(player);
        }
        else
        {
            ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("nothing.more.to.see"));
        }

        return false;
    }
}
