package org.bitbucket.zenith4183.zclans.commands;

import org.bitbucket.zenith4183.zclans.ChatBlock;
import org.bitbucket.zenith4183.zclans.Clan;
import org.bitbucket.zenith4183.zclans.ClanPlayer;
import org.bitbucket.zenith4183.zclans.zClans;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.MessageFormat;

/**
 * @author phaed
 */
public class VerifyCommand
{
    public VerifyCommand()
    {
    }

    /**
     * Execute the command
     * @param sender    player executing command
     * @param arg       command arguments
     */
    public void execute(CommandSender sender, String[] arg)
    {
        zClans plugin = zClans.getInstance();

        if (sender instanceof Player)
        {
            Player player = (Player) sender;

            ClanPlayer cp = plugin.getClanManager().getClanPlayer(player);
            Clan clan = cp == null ? null : cp.getClan();

            boolean isNonVerified = clan != null && !clan.isVerified();
            boolean isBuyer = isNonVerified && plugin.getSettingsManager().isRequireVerification() && plugin.getSettingsManager().isePurchaseVerification();

            if (isBuyer)
            {
                if (arg.length == 0 && plugin.getClanManager().purchaseVerification(player))
                {
                	clan.verifyClan();
                    clan.addBb(player.getName(), ChatColor.AQUA + MessageFormat.format(plugin.getLang("clan.0.has.been.verified"), clan.getName()));
                    ChatBlock.sendMessage(player, ChatColor.AQUA + plugin.getLang("the.clan.has.been.verified"));
                }
            }
            else if (plugin.getPermissionsManager().has(player, "zclans.mod.verify"))
            {
                if (arg.length == 1)
                {
                    Clan cclan = plugin.getClanManager().getClan(arg[0]);

                    if (cclan != null)
                    {
                        if (!cclan.isVerified())
                        {
                            cclan.verifyClan();
                            cclan.addBb(player.getName(), ChatColor.AQUA + MessageFormat.format(plugin.getLang("clan.0.has.been.verified"), cclan.getName()));
                            ChatBlock.sendMessage(player, ChatColor.AQUA + plugin.getLang("the.clan.has.been.verified"));
                        }
                        else
                        {
                            ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("the.clan.is.already.verified"));
                        }
                    }
                    else
                    {
                        ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("the.clan.does.not.exist"));
                    }
                }
                else
                {
                    ChatBlock.sendMessage(player, ChatColor.RED + MessageFormat.format(plugin.getLang("usage.0.verify.tag"), plugin.getSettingsManager().getCommandClan()));
                }
            }
            else
            {
                ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("insufficient.permissions"));
            }
        }
        else
        {
            if (arg.length == 1)
            {
                Clan cclan = plugin.getClanManager().getClan(arg[0]);

                if (cclan != null)
                {
                    if (!cclan.isVerified())
                    {
                        cclan.verifyClan();
                        cclan.addBb(sender.getName(), ChatColor.AQUA + MessageFormat.format(plugin.getLang("clan.0.has.been.verified"), cclan.getName()));
                        ChatBlock.sendMessage(sender, ChatColor.AQUA + plugin.getLang("the.clan.has.been.verified"));
                    }
                    else
                    {
                        ChatBlock.sendMessage(sender, ChatColor.RED + plugin.getLang("the.clan.is.already.verified"));
                    }
                }
                else
                {
                    ChatBlock.sendMessage(sender, ChatColor.RED + plugin.getLang("the.clan.does.not.exist"));
                }
            }
            else
            {
                ChatBlock.sendMessage(sender, ChatColor.RED + MessageFormat.format(plugin.getLang("usage.0.verify.tag"), plugin.getSettingsManager().getCommandClan()));
            }
        }
    }
}
