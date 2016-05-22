package org.bitbucket.zenith4183.zclans.commands;

import org.bitbucket.zenith4183.zclans.ChatBlock;
import org.bitbucket.zenith4183.zclans.Clan;
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
public class CreateCommand
{
    public CreateCommand()
    {
    }

    /**
     * Execute the command
     * @param player
     * @param arg
     */
    public void execute(Player player, String[] arg)
    {
        zClans plugin = zClans.getInstance();
        
        ClanPlayer cpn = plugin.getClanManager().getCreateClanPlayer(player.getUniqueId());
        
        if (cpn != null && cpn.isBanned())
        {
            ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("banned"));
            return;
        }

        if (plugin.getPermissionsManager().has(player, "zclans.leader.create"))
        {
            if (arg.length >= 2)
            {
                String tag = arg[0];
                String cleanTag = Helper.cleanTag(arg[0]);

                String name = Helper.toMessage(Helper.removeFirst(arg));

                boolean bypass = plugin.getPermissionsManager().has(player, "zclans.mod.bypass");

                if (bypass || cleanTag.length() <= plugin.getSettingsManager().getTagMaxLength())
                {
                    if (bypass || cleanTag.length() > plugin.getSettingsManager().getTagMinLength())
                    {
                        if (bypass || !plugin.getSettingsManager().hasDisallowedColor(tag))
                        {
                            if (bypass || Helper.stripColors(name).length() <= plugin.getSettingsManager().getClanMaxLength())
                            {
                                if (bypass || Helper.stripColors(name).length() > plugin.getSettingsManager().getClanMinLength())
                                {
                                    if (cleanTag.matches("[0-9a-zA-Z]*"))
                                    {
                                        if (!name.contains("&"))
                                        {
                                            if (bypass || !plugin.getSettingsManager().isDisallowedWord(cleanTag.toLowerCase()))
                                            {
                                                ClanPlayer cp = plugin.getClanManager().getClanPlayer(player);

                                                if (cp == null)
                                                {
                                                    if (!plugin.getClanManager().isClan(cleanTag))
                                                    {
                                                        if (plugin.getClanManager().purchaseCreation(player))
                                                        {
                                                            plugin.getClanManager().createClan(player, tag, name);

                                                            Clan clan = plugin.getClanManager().getClan(tag);
                                                            clan.addBb(player.getName(), ChatColor.AQUA + MessageFormat.format(plugin.getLang("clan.created"), name));
                                                            plugin.getStorageManager().updateClan(clan);

                                                            if (plugin.getSettingsManager().isRequireVerification())
                                                            {
                                                                boolean verified = !plugin.getSettingsManager().isRequireVerification() || plugin.getPermissionsManager().has(player, "zclans.mod.verify");

                                                                if (!verified)
                                                                {
                                                                    ChatBlock.sendMessage(player, ChatColor.AQUA + plugin.getLang("get.your.clan.verified.to.access.advanced.features"));
                                                                }
                                                            }
                                                        }
                                                    }
                                                    else
                                                    {
                                                        ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("clan.with.this.tag.already.exists"));
                                                    }
                                                }
                                                else
                                                {
                                                    ChatBlock.sendMessage(player, ChatColor.RED + MessageFormat.format(plugin.getLang("you.must.first.resign"), cp.getClan().getName()));
                                                }
                                            }
                                            else
                                            {
                                                ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("that.tag.name.is.disallowed"));
                                            }
                                        }
                                        else
                                        {
                                            ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("your.clan.name.cannot.contain.color.codes"));
                                        }
                                    }
                                    else
                                    {
                                        ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("your.clan.tag.can.only.contain.letters.numbers.and.color.codes"));
                                    }
                                }
                                else
                                {
                                    ChatBlock.sendMessage(player, ChatColor.RED + MessageFormat.format(plugin.getLang("your.clan.name.must.be.longer.than.characters"), plugin.getSettingsManager().getClanMinLength()));
                                }
                            }
                            else
                            {
                                ChatBlock.sendMessage(player, ChatColor.RED + MessageFormat.format(plugin.getLang("your.clan.name.cannot.be.longer.than.characters"), plugin.getSettingsManager().getClanMaxLength()));
                            }
                        }
                        else
                        {
                            ChatBlock.sendMessage(player, ChatColor.RED + MessageFormat.format(plugin.getLang("your.tag.cannot.contain.the.following.colors"), plugin.getSettingsManager().getDisallowedColorString()));
                        }
                    }
                    else
                    {
                        ChatBlock.sendMessage(player, ChatColor.RED + MessageFormat.format(plugin.getLang("your.clan.tag.must.be.longer.than.characters"), plugin.getSettingsManager().getTagMinLength()));
                    }
                }
                else
                {
                    ChatBlock.sendMessage(player, ChatColor.RED + MessageFormat.format(plugin.getLang("your.clan.tag.cannot.be.longer.than.characters"), plugin.getSettingsManager().getTagMaxLength()));
                }
            }
            else
            {
                ChatBlock.sendMessage(player, ChatColor.RED + MessageFormat.format(plugin.getLang("usage.create.tag"), plugin.getSettingsManager().getCommandClan()));
                ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("example.clan.create"));
            }
        }
        else
        {
            ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("insufficient.permissions"));
        }
    }
}
