package org.bitbucket.zenith4183.zclans.managers;

import org.bitbucket.zenith4183.zclans.ChatBlock;
import org.bitbucket.zenith4183.zclans.Helper;
import org.bitbucket.zenith4183.zclans.zClans;
import org.bitbucket.zenith4183.zclans.TeleportState;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public final class TeleportManager
{
    private zClans plugin;
    private HashMap<String, TeleportState> waitingPlayers = new HashMap<>();

    /**
     *
     */
    public TeleportManager()
    {
        plugin = zClans.getInstance();
        startCounter();
    }

    /**
     * Add player to teleport waiting queue
     *
     * @param player
     * @param dest
     * @param clanName
     */
    public void addPlayer(Player player, Location dest, String clanName)
    {
        int secs = zClans.getInstance().getSettingsManager().getWaitSecs();


        waitingPlayers.put(player.getUniqueId().toString(), new TeleportState(player, dest, clanName));

        if (secs > 0)
        {
            ChatBlock.sendMessage(player, ChatColor.AQUA + MessageFormat.format(plugin.getLang("waiting.for.teleport.stand.still.for.0.seconds"), secs));
        }
    }

    private void dropItems(Player player)
    {
        if (plugin.getSettingsManager().isDropOnHome())
        {
            PlayerInventory inv = player.getInventory();
            ItemStack[] contents = inv.getContents();

            for (ItemStack item : contents)
            {
                if (item == null)
                {
                    continue;
                }

                List<Integer> itemsList = plugin.getSettingsManager().getItemsList();

                if (itemsList.contains(item.getTypeId()))
                {
                    player.getWorld().dropItemNaturally(player.getLocation(), item);
                    inv.remove(item);
                }
            }
        }
        else if (plugin.getSettingsManager().isKeepOnHome())
        {
            try
            {
                PlayerInventory inv = player.getInventory();
                ItemStack[] contents = inv.getContents().clone();

                for (ItemStack item : contents)
                {
                    if (item == null)
                    {
                        continue;
                    }

                    List<Integer> itemsList = plugin.getSettingsManager().getItemsList();

                    if (!itemsList.contains(item.getTypeId()))
                    {
                        player.getWorld().dropItemNaturally(player.getLocation(), item);
                        inv.remove(item);
                    }
                }
            }
            catch (Exception ex)
            {
                Helper.dumpStackTrace();
            }
        }
    }

    private void startCounter()
    {
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable()
        {
            @Override
            public void run()
            {
                for (Iterator iter = waitingPlayers.values().iterator(); iter.hasNext(); )
                {
                    TeleportState state = (TeleportState) iter.next();

                    if (state.isProcessing())
                    {
                        continue;
                    }
                    state.setProcessing(true);

                    Player player = state.getPlayer();

                    if (player != null)
                    {
                        if (state.isTeleportTime())
                        {
                            if (Helper.isSameBlock(player.getLocation(), state.getLocation()))
                            {
                                Location loc = state.getDestination();

                                int x = loc.getBlockX();
                                int z = loc.getBlockZ();

                                if (!plugin.getPermissionsManager().has(player, "zclans.mod.keep-items"))
                                {
                                    dropItems(player);
                                }

                                zClans.debug("teleporting");

                                player.teleport(new Location(loc.getWorld(), loc.getBlockX() + .5, loc.getBlockY(), loc.getBlockZ() + .5));

                                ChatBlock.sendMessage(player, ChatColor.AQUA + MessageFormat.format(plugin.getLang("now.at.homebase"), state.getClanName()));
                            }
                            else
                            {
                                ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("you.moved.teleport.cancelled"));
                            }

                            iter.remove();
                        }
                        else
                        {
                            if (!Helper.isSameBlock(player.getLocation(), state.getLocation()))
                            {
                                ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("you.moved.teleport.cancelled"));
                                iter.remove();
                            }
                            else
                            {
                                ChatBlock.sendMessage(player, ChatColor.AQUA + "" + state.getCounter());
                            }
                        }
                    }
                    else
                    {
                        iter.remove();
                    }

                    state.setProcessing(false);
                }
            }
        }, 0, 20L);
    }
}