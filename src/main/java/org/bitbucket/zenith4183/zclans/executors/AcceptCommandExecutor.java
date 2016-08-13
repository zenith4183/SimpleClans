package org.bitbucket.zenith4183.zclans.executors;

import org.bitbucket.zenith4183.zclans.*;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.MessageFormat;

public class AcceptCommandExecutor implements CommandExecutor
{
    private zClans plugin;

    public AcceptCommandExecutor()
    {
        plugin = zClans.getInstance();
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings)
    {
        Player player = (Player) commandSender;

        ClanPlayer cp = plugin.getClanManager().getClanPlayer(player);
        
        if (cp != null)
        {
            if (cp.isBanned())
            {
                ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("banned"));
                return false;
            }
        	
            Clan clan = cp.getClan();

            if (clan.isLeader(player))
            {
                if (plugin.getRequestManager().hasRequest(clan.getTag()))
                {
                    if (cp.getVote() == null)
                    {
                        plugin.getRequestManager().accept(cp);
                        clan.leaderAnnounce(ChatColor.GREEN + MessageFormat.format(plugin.getLang("voted.to.accept"), Helper.capitalize(player.getName())));
                    }
                    else
                    {
                        ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("you.have.already.voted"));
                    }
                }
                else
                {
                    ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("nothing.to.accept"));
                }
            }
            else
            {
                ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("no.leader.permissions"));
            }
        }
        else
        {
            if (plugin.getRequestManager().hasRequest(player.getName().toLowerCase()))
            {

                cp = plugin.getClanManager().getCreateClanPlayer(player.getUniqueId());

                plugin.getRequestManager().accept(cp);
            }
            else
            {
                ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("nothing.to.accept"));
            }
        }
        return false;
    }
}
