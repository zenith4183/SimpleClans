package org.bitbucket.zenith4183.zclans.listeners;

import org.bitbucket.zenith4183.zclans.ClanPlayer;
import org.bitbucket.zenith4183.zclans.Helper;
import org.bitbucket.zenith4183.zclans.zClans;
import org.bitbucket.zenith4183.zclans.executors.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

import java.util.Iterator;

/**
 * @author phaed
 */
public class SCPlayerListener implements Listener
{
    private zClans plugin;

    /**
     *
     */
    public SCPlayerListener()
    {
        plugin = zClans.getInstance();
    }

    /**
     * @param event     player command event object
     */
    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event)
    {
        if (event.isCancelled())
        {
            return;
        }

        Player player = event.getPlayer();

        if (player == null)
        {
            return;
        }

        if (plugin.getSettingsManager().isBlacklistedWorld(player.getLocation().getWorld().getName()))
        {
            return;
        }

        if (event.getMessage().length() == 0)
        {
            return;
        }

        String[] split = event.getMessage().substring(1).split(" ");

        if (split.length == 0)
        {
            return;
        }

        String command = split[0];

        if (plugin.getSettingsManager().isTagBasedClanChat() && plugin.getClanManager().isClan(command))
        {
            if (!plugin.getSettingsManager().getClanChatEnable())
            {
                return;
            }

            ClanPlayer cp = plugin.getClanManager().getClanPlayer(player);

            if (cp == null)
            {
                return;
            }

            if (cp.getTag().equalsIgnoreCase(command))
            {
                event.setCancelled(true);

                if (split.length > 1)
                {
                    plugin.getClanManager().processClanChat(player, cp.getTag(), Helper.toMessage(Helper.removeFirst(split)));
                }
            }
        }
        if (command.equals("."))
        {
            if (!plugin.getSettingsManager().getClanChatEnable())
            {
                return;
            }

            ClanPlayer cp = plugin.getClanManager().getClanPlayer(player);

            if (cp == null)
            {
                return;
            }

            event.setCancelled(true);

            if (split.length > 1)
            {
                plugin.getClanManager().processClanChat(player, cp.getTag(), Helper.toMessage(Helper.removeFirst(split)));
            }
        }

        if (plugin.getSettingsManager().isForceCommandPriority())
        {
            if (command.equalsIgnoreCase(plugin.getSettingsManager().getCommandAlly()))
            {
                if (!plugin.getServer().getPluginCommand(plugin.getSettingsManager().getCommandAlly()).equals(plugin.getCommand(plugin.getSettingsManager().getCommandAlly())))
                {
                    new AllyCommandExecutor().onCommand(player, null, null, Helper.removeFirst(split));
                    event.setCancelled(true);
                }
            }
            else if (command.equalsIgnoreCase(plugin.getSettingsManager().getCommandClan()))
            {
                if (!plugin.getServer().getPluginCommand(plugin.getSettingsManager().getCommandClan()).equals(plugin.getCommand(plugin.getSettingsManager().getCommandClan())))
                {
                    new ClanCommandExecutor().onCommand(player, null, null, Helper.removeFirst(split));
                    event.setCancelled(true);
                }
            }
            else if (command.equalsIgnoreCase(plugin.getSettingsManager().getCommandAccept()))
            {
                if (!plugin.getServer().getPluginCommand(plugin.getSettingsManager().getCommandAccept()).equals(plugin.getCommand(plugin.getSettingsManager().getCommandAccept())))
                {
                    new AcceptCommandExecutor().onCommand(player, null, null, Helper.removeFirst(split));
                    event.setCancelled(true);
                }
            }
            else if (command.equalsIgnoreCase(plugin.getSettingsManager().getCommandDeny()))
            {
                if (!plugin.getServer().getPluginCommand(plugin.getSettingsManager().getCommandDeny()).equals(plugin.getCommand(plugin.getSettingsManager().getCommandDeny())))
                {
                    new DenyCommandExecutor().onCommand(player, null, null, Helper.removeFirst(split));
                    event.setCancelled(true);
                }
            }
            else if (command.equalsIgnoreCase(plugin.getSettingsManager().getCommandMore()))
            {
                //if (!pluginCommand.equals(cmd))
                //{
                    new MoreCommandExecutor().onCommand(player, null, null, Helper.removeFirst(split));
                    event.setCancelled(true);
                //}
            }
        }
    }

    /**
     * @param event         player chat event object
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerChat(AsyncPlayerChatEvent event)
    {
        if (plugin.getSettingsManager().isBlacklistedWorld(event.getPlayer().getLocation().getWorld().getName()))
        {
            return;
        }

        if (event.getPlayer() == null)
        {
            return;
        }

        String message = event.getMessage();
        ClanPlayer cp = plugin.getClanManager().getClanPlayer(event.getPlayer());

        if (cp != null)
        {
            if (cp.getChannel().equals(ClanPlayer.Channel.CLAN))
            {
                plugin.getClanManager().processClanChat(event.getPlayer(), message);
                event.setCancelled(true);
            }
            else if (cp.getChannel().equals(ClanPlayer.Channel.ALLY))
            {
                plugin.getClanManager().processAllyChat(event.getPlayer(), message);
                event.setCancelled(true);
            }
        }

        if (!plugin.getPermissionsManager().has(event.getPlayer(), "simpleclans.mod.nohide"))
        {
            boolean isClanChat = event.getMessage().contains("" + ChatColor.RED + ChatColor.WHITE + ChatColor.RED + ChatColor.BLACK);
            boolean isAllyChat = event.getMessage().contains("" + ChatColor.AQUA + ChatColor.WHITE + ChatColor.AQUA + ChatColor.BLACK);

            for (Iterator iter = event.getRecipients().iterator(); iter.hasNext(); )
            {
                Player player = (Player) iter.next();

                ClanPlayer rcp = plugin.getClanManager().getClanPlayer(player);

                if (rcp != null)
                {
                    if (!rcp.isClanChat() && isClanChat)
                    {
                        iter.remove();
                        continue;
                    }

                    if (!rcp.isAllyChat() && isAllyChat)
                    {
                        iter.remove();
                        continue;
                    }

                    if (!rcp.isGlobalChat() && !isAllyChat && !isClanChat)
                    {
                        iter.remove();
                    }
                }
            }
        }


        plugin.getClanManager().updateDisplayName(event.getPlayer());
    }

    /**
     * @param event         player join event object
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        final Player player = event.getPlayer();

        if (zClans.getInstance().getSettingsManager().isBlacklistedWorld(player.getLocation().getWorld().getName()))
        {
            return;
        }

        ClanPlayer cp;
        if (zClans.getInstance().getSettingsManager().getUseBungeeCord())
        {
            cp = zClans.getInstance().getClanManager().getClanPlayerJoinEvent(player);
        }
        else
        {
            cp = zClans.getInstance().getClanManager().getClanPlayer(player);
        }

        if (cp == null)
        {
            return;
        }
        cp.setName(player.getName());
        zClans.getInstance().getClanManager().updateLastSeen(player);
        zClans.getInstance().getClanManager().updateDisplayName(player);

        if (plugin.getSettingsManager().isBbShowOnLogin() && cp.isBbEnabled())
        {
        	cp.getClan().displayBb(player);
        }

        if (event.getPlayer().isOp())
        {
            for (String message : zClans.getInstance().getMessages())
            {
                event.getPlayer().sendMessage(ChatColor.YELLOW + message);
            }
        }
    }

    /**
     * @param event         player respawn event object
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerRespawn(PlayerRespawnEvent event)
    {
        if (plugin.getSettingsManager().isBlacklistedWorld(event.getPlayer().getLocation().getWorld().getName()))
        {
            return;
        }

        if (plugin.getSettingsManager().isTeleportOnSpawn())
        {
            Player player = event.getPlayer();

            ClanPlayer cp = plugin.getClanManager().getClanPlayer(player);

            if (cp != null)
            {
                Location loc = cp.getClan().getHomeLocation();

                if (loc != null)
                {
                    event.setRespawnLocation(loc);
                }
            }
        }
    }

    /**
     * @param event         player quit event object
     */
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        if (plugin.getSettingsManager().isBlacklistedWorld(event.getPlayer().getLocation().getWorld().getName()))
        {
            return;
        }

        ClanPlayer cp = plugin.getClanManager().getClanPlayer(event.getPlayer());

        plugin.getClanManager().updateLastSeen(event.getPlayer());
        plugin.getRequestManager().endPendingRequest(event.getPlayer().getName());
    }

    /**
     * @param event         player kick event object
     */
    @EventHandler
    public void onPlayerKick(PlayerKickEvent event)
    {
        if (plugin.getSettingsManager().isBlacklistedWorld(event.getPlayer().getLocation().getWorld().getName()))
        {
            return;
        }

        plugin.getClanManager().updateLastSeen(event.getPlayer());
    }
}