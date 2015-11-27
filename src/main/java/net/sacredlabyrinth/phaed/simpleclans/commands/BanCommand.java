package net.sacredlabyrinth.phaed.simpleclans.commands;

import net.sacredlabyrinth.phaed.simpleclans.ChatBlock;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.MessageFormat;
import java.util.UUID;
import net.sacredlabyrinth.phaed.simpleclans.uuid.UUIDUtil;

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
     * @param player
     * @param arg
     */
    public void execute(Player player, String[] arg)
    {
        SimpleClans plugin = SimpleClans.getInstance();

        if (plugin.getPermissionsManager().has(player, "simpleclans.mod.ban"))
        {
            if (arg.length == 1)
            {
                String banned = arg[0];
                
                ClanPlayer cp = plugin.getClanManager().getCreateClanPlayerUUID(banned);
                
                if (!cp.isBanned())
                {
                    UUID PlayerUniqueId = UUIDUtil.nameToUUID(banned);
                    Player pl = SimpleClans.getInstance().getServer().getPlayer(PlayerUniqueId);

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
