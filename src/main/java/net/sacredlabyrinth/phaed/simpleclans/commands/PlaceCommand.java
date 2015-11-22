package net.sacredlabyrinth.phaed.simpleclans.commands;

import net.sacredlabyrinth.phaed.simpleclans.*;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.MessageFormat;

public class PlaceCommand
{
    public PlaceCommand()
    {
    }

    /**
     * Execute the command
     *
     * @param sender
     * @param arg
     */
    public void execute(CommandSender sender, String[] arg)
    {
        SimpleClans plugin = SimpleClans.getInstance();

        if (sender instanceof Player)
        {
            Player player = (Player) sender;

            if (!plugin.getPermissionsManager().has(player, "simpleclans.mod.place"))
            {
                ChatBlock.sendMessage(sender, ChatColor.RED + plugin.getLang("insufficient.permissions"));
                return;
            }
        }

        if (arg.length == 2)
        {
            Player player = Helper.getPlayer(arg[0]);

            if (player != null)
            {
                Clan newClan = plugin.getClanManager().getClan(arg[1]);

                if (newClan != null)
                {
                    ClanPlayer oldCp = plugin.getClanManager().getClanPlayer(player);

                    if (oldCp != null)
                    {
                        Clan oldClan = oldCp.getClan();

                        if (oldClan.isLeader(player) && oldClan.getLeaders().size() <= 1)
                        {
                            oldClan.clanAnnounce(player.getName(), ChatColor.AQUA + MessageFormat.format(plugin.getLang("clan.has.been.disbanded"), oldClan.getName()));
                            oldClan.disband();
                        }
                        else
                        {
                            oldClan.addBb(player.getName(), ChatColor.AQUA + MessageFormat.format(plugin.getLang("0.has.resigned"), Helper.capitalize(player.getName())));

                            if (SimpleClans.getInstance().hasUUID())
                            {
                                oldClan.removePlayerFromClan(player.getUniqueId());
                            }
                            else
                            {
                                oldClan.removePlayerFromClan(player.getName());
                            }
                        }
                    }

                    ClanPlayer cp = plugin.getClanManager().getCreateClanPlayerUUID(player.getName());

                    if (cp == null)
                    {
                        return;
                    }

                    newClan.addBb(ChatColor.AQUA + MessageFormat.format(plugin.getLang("joined.the.clan"), player.getName()));
                    plugin.getClanManager().serverAnnounce(MessageFormat.format(plugin.getLang("has.joined"), player.getName(), newClan.getName()));
                    newClan.addPlayerToClan(cp);
                }
                else
                {
                    ChatBlock.sendMessage(sender, ChatColor.RED + plugin.getLang("the.clan.does.not.exist"));
                }
            }
            else
            {
                ChatBlock.sendMessage(sender, ChatColor.RED + plugin.getLang("no.player.matched"));
            }
        }
        else
        {
            ChatBlock.sendMessage(sender, ChatColor.RED + MessageFormat.format(plugin.getLang("usage.0.place"), plugin.getSettingsManager().getCommandClan()));
        }
    }
}
