package net.sacredlabyrinth.phaed.simpleclans.listeners;

import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import net.sacredlabyrinth.phaed.simpleclans.Helper;
import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;
import net.sacredlabyrinth.phaed.simpleclans.executors.*;
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
    private SimpleClans plugin;

    /**
     *
     */
    public SCPlayerListener()
    {
        plugin = SimpleClans.getInstance();
    }

    /**
     * @param event
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
     * @param event
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent event)
    {
        if (event.getPlayer() == null)
        {
            return;
        }

       plugin.getClanManager().updateDisplayName(event.getPlayer());
    }

    /**
     * @param event
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        final Player player = event.getPlayer();

        if (SimpleClans.getInstance().getSettingsManager().isBlacklistedWorld(player.getLocation().getWorld().getName()))
        {
            return;
        }

        ClanPlayer cp;
        if (SimpleClans.getInstance().getSettingsManager().getUseBungeeCord())
        {
            cp = SimpleClans.getInstance().getClanManager().getClanPlayerJoinEvent(player);
        }
        else
        {
            cp = SimpleClans.getInstance().getClanManager().getClanPlayer(player);
        }

        if (cp == null)
        {
            return;
        }
        cp.setName(player.getName());
        SimpleClans.getInstance().getClanManager().updateLastSeen(player);
        SimpleClans.getInstance().getClanManager().updateDisplayName(player);

        SimpleClans.getInstance().getPermissionsManager().addPlayerPermissions(cp);

        if (plugin.getSettingsManager().isBbShowOnLogin() && cp.isBbEnabled())
        {
        	cp.getClan().displayBb(player);
        }

        if (event.getPlayer().isOp())
        {
            for (String message : SimpleClans.getInstance().getMessages())
            {
                event.getPlayer().sendMessage(ChatColor.YELLOW + message);
            }
        }
    }

    /**
     * @param event
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
     * @param event
     */
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        if (plugin.getSettingsManager().isBlacklistedWorld(event.getPlayer().getLocation().getWorld().getName()))
        {
            return;
        }

        ClanPlayer cp = plugin.getClanManager().getClanPlayer(event.getPlayer());

        SimpleClans.getInstance().getPermissionsManager().removeClanPlayerPermissions(cp);
        plugin.getClanManager().updateLastSeen(event.getPlayer());
        plugin.getRequestManager().endPendingRequest(event.getPlayer().getName());
    }

    /**
     * @param event
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

    /**
     * @param event
     */
    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event)
    {
        if (event.isCancelled())
        {
            return;
        }

        if (plugin.getSettingsManager().isBlacklistedWorld(event.getPlayer().getLocation().getWorld().getName()))
        {
            return;
        }

    }

}