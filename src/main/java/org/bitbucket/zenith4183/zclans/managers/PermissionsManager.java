package org.bitbucket.zenith4183.zclans.managers;

import org.bitbucket.zenith4183.zclans.zClans;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bitbucket.zenith4183.zclans.Clan;
import org.bitbucket.zenith4183.zclans.ClanPlayer;
import org.bitbucket.zenith4183.zclans.zClans;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;


/**
 * @author phaed
 */
public final class PermissionsManager
{
    /**
     *
     */
    private zClans plugin;

    private static Permission permission = null;
    private static Economy economy = null;
    private static Chat chat = null;

    /**
     *
     */
    public PermissionsManager()
    {
        plugin = zClans.getInstance();

        try
        {
            Class.forName("net.milkbowl.vault.permission.Permission");

            setupChat();
            setupEconomy();
            setupPermissions();
        }
        catch (ClassNotFoundException e)
        {
            zClans.log("[zClans] Vault not found. No economy or extended Permissions support.");
        }
    }

    /**
     * Whether economy plugin exists and is enabled
     *
     * @return
     */
    public boolean hasEconomy()
    {
        return economy != null && economy.isEnabled();
    }


    /**
     * Charge a player some money
     *
     * @param player
     * @param money
     * @return
     */
    public boolean playerChargeMoney(OfflinePlayer player, double money)
    {
        return economy.withdrawPlayer(player, money).transactionSuccess();
    }

    /**
     * Grants a player some money
     *
     * @param player
     * @param money
     * @return
     */
    public boolean playerGrantMoney(OfflinePlayer player, double money)
    {
        return economy.depositPlayer(player, money).transactionSuccess();
    }

    /**
     * Grants a player some money
     *
     * @param player
     * @param money
     * @return
     */
    public boolean playerGrantMoney(String player, double money)
    {
        return economy.depositPlayer(player, money).transactionSuccess();
    }

    /**
     * Check if a user has the money
     *
     * @param player
     * @param money
     * @return whether he has the money
     */
    public boolean playerHasMoney(OfflinePlayer player, double money)
    {
        return economy.has(player, money);
    }

    /**
     * Returns the players money
     *
     * @param player
     * @return the players money
     */
    public double playerGetMoney(OfflinePlayer player)
    {
        return economy.getBalance(player);
    }

    /**
     * Check if a player has permissions
     *
     * @param player the player
     * @param perm   the permission
     * @return whether he has the permission
     */
    public boolean has(Player player, String perm)
    {
        if (player == null)
        {
            return false;
        }

        if (permission != null)
        {
            return permission.has(player, perm);
        }
        else
        {
            return player.hasPermission(perm);
        }
    }

    private Boolean setupPermissions()
    {
        RegisteredServiceProvider<Permission> permissionProvider = plugin.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null)
        {
            permission = permissionProvider.getProvider();
        }
        return permission != null;
    }

    private Boolean setupChat()
    {
        RegisteredServiceProvider<Chat> chatProvider = plugin.getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
        if (chatProvider != null)
        {
            chat = chatProvider.getProvider();
        }

        return chat != null;
    }

    private Boolean setupEconomy()
    {
        RegisteredServiceProvider<Economy> economyProvider = plugin.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null)
        {
            economy = economyProvider.getProvider();
        }

        return economy != null;
    }

    /**
     * @param p
     * @return
     */
    @SuppressWarnings({"deprecation"})
    public String getPrefix(Player p)
    {
        String out = "";

        try
        {
            if (chat != null)
            {
                out = chat.getPlayerPrefix(p);
            }
        }
        catch (Exception ex)
        {
            // yea vault kinda sucks like that
        }

        if (permission != null && chat != null)
        {
            try
            {
                String world = p.getWorld().getName();
                String name = p.getName();
                String prefix = chat.getPlayerPrefix(name, world);
                if (prefix == null || prefix.isEmpty())
                {
                    String group = permission.getPrimaryGroup(world, name);
                    prefix = chat.getGroupPrefix(world, group);
                    if (prefix == null)
                    {
                        prefix = "";
                    }
                }

                out = prefix.replace("&", "\u00a7").replace(String.valueOf((char) 194), "");
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
            }
        }

        return out;
    }

    /**
     * @param p
     * @return
     */
    @SuppressWarnings({"deprecation"})
    public String getSuffix(Player p)
    {
        try
        {
            if (chat != null)
            {
                return chat.getPlayerSuffix(p);
            }
        }
        catch (Exception ex)
        {
            // yea vault kinda sucks like that
        }

        if (permission != null && chat != null)
        {
            try
            {
                String world = p.getWorld().getName();
                String name = p.getName();
                String suffix = chat.getPlayerSuffix(world, name);
                if (suffix == null || suffix.isEmpty())
                {
                    String group = permission.getPrimaryGroup(world, name);
                    suffix = chat.getPlayerSuffix(world, group);
                    if (suffix == null)
                    {
                        suffix = "";
                    }
                }
                return suffix.replace("&", "\u00a7").replace(String.valueOf((char) 194), "");
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
                return "";
            }
        }
        return "";
    }
}
