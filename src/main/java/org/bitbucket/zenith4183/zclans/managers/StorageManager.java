package org.bitbucket.zenith4183.zclans.managers;

import org.bitbucket.zenith4183.zclans.*;
import org.bitbucket.zenith4183.zclans.*;
import org.bitbucket.zenith4183.zclans.storage.DBCore;
import org.bitbucket.zenith4183.zclans.storage.MySQLCore;
import org.bitbucket.zenith4183.zclans.storage.SQLiteCore;
import org.bitbucket.zenith4183.zclans.uuid.UUIDUtil;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.*;
import java.util.logging.Level;

/**
 * @author phaed
 */
public final class StorageManager
{

    private zClans plugin;
    private DBCore core;
    private HashMap<String, ChatBlock> chatBlocks = new HashMap<>();

    /**
     *
     */
    {
        plugin = zClans.getInstance();
        initiateDB();
        if(zClans.getInstance().getSettingsManager().isOnlineMode())
            updateDatabase();
        importFromDatabase();
    }

    /**
     * Retrieve a player's pending chat lines
     *
     * @param player
     * @return
     */
    public ChatBlock getChatBlock(Player player)
    {
        return chatBlocks.get(player.getUniqueId().toString());
    }

    /**
     * Store pending chat lines for a player
     *
     * @param player
     * @param cb
     */
    public void addChatBlock(Player player, ChatBlock cb)
    {

        UUID uuid = player.getUniqueId();

        if (uuid == null)
        {
            return;
        }

        chatBlocks.put(uuid.toString(), cb);
    }

    /**
     * Initiates the db
     */
    public void initiateDB()
    {
        if (plugin.getSettingsManager().isUseMysql())
        {
            core = new MySQLCore(plugin.getSettingsManager().getHost(), plugin.getSettingsManager().getPort(), plugin.getSettingsManager().getDatabase(), plugin.getSettingsManager().getUsername(), plugin.getSettingsManager().getPassword());

            if (core.checkConnection())
            {
                zClans.log("[zClans] " + plugin.getLang("mysql.connection.successful"));

                if (!core.existsTable("sc_clans"))
                {
                    zClans.log("Creating table: sc_clans");

                    String query = "CREATE TABLE IF NOT EXISTS `sc_clans` ( `id` bigint(20) NOT NULL auto_increment, `verified` tinyint(1) default '0', `tag` varchar(25) NOT NULL, `color_tag` varchar(25) NOT NULL, `name` varchar(100) NOT NULL, `friendly_fire` tinyint(1) default '0', `founded` bigint NOT NULL, `last_used` bigint NOT NULL, `packed_allies` text NOT NULL, `packed_rivals` text NOT NULL, `packed_bb` mediumtext NOT NULL, `flags` text NOT NULL, PRIMARY KEY  (`id`), UNIQUE KEY `uq_zclans_1` (`tag`));";
                    core.execute(query);
                }

                if (!core.existsTable("sc_players"))
                {
                    zClans.log("Creating table: sc_players");

                    String query = "CREATE TABLE IF NOT EXISTS `sc_players` ( `id` bigint(20) NOT NULL auto_increment, `uuid` VARCHAR(255) DEFAULT NULL, `leader` tinyint(1) default '0', `tag` varchar(25) NOT NULL, `friendly_fire` tinyint(1) default '0', `neutral_kills` int(11) default NULL, `rival_kills` int(11) default NULL, `civilian_kills` int(11) default NULL, `deaths` int(11) default NULL, `last_seen` bigint NOT NULL, `join_date` bigint NOT NULL, `trusted` tinyint(1) default '0', `flags` text NOT NULL, `packed_past_clans` text, `banned` tinyint(1) default '0', PRIMARY KEY  (`id`), UNIQUE KEY `uq_sc_players_1` (`uuid`));";
                    core.execute(query);
                }

                if (!core.existsTable("sc_kills"))
                {
                    zClans.log("Creating table: sc_kills");

                    String query = "CREATE TABLE IF NOT EXISTS `sc_kills` ( `kill_id` bigint(20) NOT NULL auto_increment, `attacker_uuid` VARCHAR(255) DEFAULT NULL, `attacker_tag` varchar(16) NOT NULL, `victim_uuid` VARCHAR(255) DEFAULT NULL, `victim_tag` varchar(16) NOT NULL, `kill_type` varchar(1) NOT NULL, PRIMARY KEY  (`kill_id`));";
                    core.execute(query);
                }
            }
            else
            {
                zClans.getInstance().getServer().getConsoleSender().sendMessage("[zClans] " + ChatColor.RED + plugin.getLang("mysql.connection.failed"));
            }
        }
        else
        {
            core = new SQLiteCore(plugin.getDataFolder().getPath());

            if (core.checkConnection())
            {
                zClans.log("[zClans] " + plugin.getLang("sqlite.connection.successful"));

                if (!core.existsTable("sc_clans"))
                {
                    zClans.log("Creating table: sc_clans");

                    String query = "CREATE TABLE IF NOT EXISTS `sc_clans` ( `id` bigint(20), `verified` tinyint(1) default '0', `tag` varchar(25) NOT NULL, `color_tag` varchar(25) NOT NULL, `name` varchar(100) NOT NULL, `friendly_fire` tinyint(1) default '0', `founded` bigint NOT NULL, `last_used` bigint NOT NULL, `packed_allies` text NOT NULL, `packed_rivals` text NOT NULL, `packed_bb` mediumtext NOT NULL, `flags` text NOT NULL, PRIMARY KEY  (`id`), UNIQUE (`tag`));";
                    core.execute(query);
                }

                if (!core.existsTable("sc_players"))
                {
                    zClans.log("Creating table: sc_players");

                    String query = "CREATE TABLE IF NOT EXISTS `sc_players` ( `id` bigint(20), `uuid` VARCHAR(255) DEFAULT NULL, `leader` tinyint(1) default '0', `tag` varchar(25) NOT NULL, `friendly_fire` tinyint(1) default '0', `neutral_kills` int(11) default NULL, `rival_kills` int(11) default NULL, `civilian_kills` int(11) default NULL, `deaths` int(11) default NULL, `last_seen` bigint NOT NULL, `join_date` bigint NOT NULL, `trusted` tinyint(1) default '0', `flags` text NOT NULL, `packed_past_clans` text, `banned` tinyint(1) default '0', PRIMARY KEY  (`id`), UNIQUE (`uuid`));";
                    core.execute(query);
                }

                if (!core.existsTable("sc_kills"))
                {
                    zClans.log("Creating table: sc_kills");

                    String query = "CREATE TABLE IF NOT EXISTS `sc_kills` ( `kill_id` bigint(20), `attacker_uuid` VARCHAR(255) DEFAULT NULL, `attacker_tag` varchar(16) NOT NULL, `victim_uuid` VARCHAR(255) DEFAULT NULL, `victim_tag` varchar(16) NOT NULL, `kill_type` varchar(1) NOT NULL, PRIMARY KEY  (`kill_id`));";
                    core.execute(query);
                }
            }
            else
            {
                zClans.getInstance().getServer().getConsoleSender().sendMessage("[zClans] " + ChatColor.RED + plugin.getLang("sqlite.connection.failed"));
            }
        }
    }

    /**
     * Closes DB connection
     */
    public void closeConnection()
    {
        core.close();
    }

    /**
     * Import all data from database to memory
     */
    public void importFromDatabase()
    {
        plugin.getClanManager().cleanData();

        List<Clan> clans = retrieveClans();
        purgeClans(clans);

        for (Clan clan : clans)
        {
            plugin.getClanManager().importClan(clan);
        }

        if (!clans.isEmpty())
        {
            zClans.log(MessageFormat.format("[zClans] " + plugin.getLang("clans"), clans.size()));
        }

        List<ClanPlayer> cps = retrieveClanPlayers();
        purgeClanPlayers(cps);

        for (ClanPlayer cp : cps)
        {
            Clan tm = cp.getClan();

            if (tm != null)
            {
                tm.importMember(cp);
            }
            plugin.getClanManager().importClanPlayer(cp);
        }

        if (!cps.isEmpty())
        {
            zClans.log(MessageFormat.format("[zClans] " + plugin.getLang("clan.players"), cps.size()));
        }
    }

    /**
     * Import one ClanPlayer data from database to memory
     * Used for BungeeCord Reload ClanPlayer and your Clan
     *
     * @param player
     */
    public void importFromDatabaseOnePlayer(Player player)
    {
        plugin.getClanManager().deleteClanPlayerFromMemory(player.getUniqueId());

        ClanPlayer cp = retrieveOneClanPlayer(player.getUniqueId());

        if (cp != null)
        {
            Clan tm = cp.getClan();

            if (tm != null)
            {
                tm.importMember(cp);
            }
            plugin.getClanManager().importClanPlayer(cp);

            zClans.log("[zClans] ClanPlayer Reloaded: " + player.getName() + ", UUID: " + player.getUniqueId().toString());
        }
    }

    private void purgeClans(List<Clan> clans)
    {
        List<Clan> purge = new ArrayList<>();

        for (Clan clan : clans)
        {
            if (clan.isVerified())
            {
                if (clan.getInactiveDays() > plugin.getSettingsManager().getPurgeClan())
                {
                    purge.add(clan);
                }
            }
            else
            {
                if (clan.getInactiveDays() > plugin.getSettingsManager().getPurgeUnverified())
                {
                    purge.add(clan);
                }
            }
        }

        for (Clan clan : purge)
        {
            zClans.log("[zClans] " + MessageFormat.format(plugin.getLang("purging.clan"), clan.getName()));
            deleteClan(clan);
            clans.remove(clan);
        }
    }

    private void purgeClanPlayers(List<ClanPlayer> cps)
    {
        List<ClanPlayer> purge = new ArrayList<>();

        for (ClanPlayer cp : cps)
        {
            if (cp.getInactiveDays() > plugin.getSettingsManager().getPurgePlayers() && !cp.isLeader())
            {
            	purge.add(cp);
            }
        }

        for (ClanPlayer cp : purge)
        {
            zClans.log("[zClans] " + MessageFormat.format(plugin.getLang("purging.player.data"), cp.getName()));
            deleteClanPlayer(cp);
            cps.remove(cp);
        }
    }

    /**
     * Retrieves all simple clans from the database
     *
     * @return
     */
    public List<Clan> retrieveClans()
    {
        List<Clan> out = new ArrayList<>();

        String query = "SELECT * FROM  `sc_clans`;";
        ResultSet res = core.select(query);

        if (res != null)
        {
            try
            {
                while (res.next())
                {
                    try
                    {
                        boolean verified = res.getBoolean("verified");
                        boolean friendly_fire = res.getBoolean("friendly_fire");
                        String tag = res.getString("tag");
                        String color_tag = Helper.parseColors(res.getString("color_tag"));
                        String name = res.getString("name");
                        String packed_allies = res.getString("packed_allies");
                        String packed_rivals = res.getString("packed_rivals");
                        String packed_bb = res.getString("packed_bb");
                        String flags = res.getString("flags");
                        long founded = res.getLong("founded");
                        long last_used = res.getLong("last_used");

                        if (founded == 0)
                        {
                            founded = (new Date()).getTime();
                        }

                        if (last_used == 0)
                        {
                            last_used = (new Date()).getTime();
                        }

                        Clan clan = new Clan();
                        clan.setFlags(flags);
                        clan.setVerified(verified);
                        clan.setFriendlyFire(friendly_fire);
                        clan.setTag(tag);
                        clan.setColorTag(color_tag);
                        clan.setName(name);
                        clan.setPackedAllies(packed_allies);
                        clan.setPackedRivals(packed_rivals);
                        clan.setPackedBb(packed_bb);
                        clan.setFounded(founded);
                        clan.setLastUsed(last_used);

                        out.add(clan);
                    }
                    catch (Exception ex)
                    {
                        for (StackTraceElement el : ex.getStackTrace())
                        {
                            System.out.print(el.toString());
                        }
                    }
                }
            }
            catch (SQLException ex)
            {
                zClans.getLog().severe(String.format("An Error occurred: %s", ex.getErrorCode()));
                zClans.getLog().log(Level.SEVERE, null, ex);
            }
        }

        return out;
    }

    /**
     * Retrieves one Clan from the database
     * Used for BungeeCord Reload ClanPlayer and your Clan
     *
     * @param tagClan
     * @return
     */
    public Clan retrieveOneClan(String tagClan)
    {
        Clan out = null;

        String query = "SELECT * FROM  `sc_clans` WHERE `tag` = '" + tagClan + "';";
        ResultSet res = core.select(query);

        if (res != null)
        {
            try
            {
                while (res.next())
                {
                    try
                    {
                        boolean verified = res.getBoolean("verified");
                        boolean friendly_fire = res.getBoolean("friendly_fire");
                        String tag = res.getString("tag");
                        String color_tag = Helper.parseColors(res.getString("color_tag"));
                        String name = res.getString("name");
                        String packed_allies = res.getString("packed_allies");
                        String packed_rivals = res.getString("packed_rivals");
                        String packed_bb = res.getString("packed_bb");
                        String flags = res.getString("flags");
                        long founded = res.getLong("founded");
                        long last_used = res.getLong("last_used");

                        if (founded == 0)
                        {
                            founded = (new Date()).getTime();
                        }

                        if (last_used == 0)
                        {
                            last_used = (new Date()).getTime();
                        }

                        Clan clan = new Clan();
                        clan.setFlags(flags);
                        clan.setVerified(verified);
                        clan.setFriendlyFire(friendly_fire);
                        clan.setTag(tag);
                        clan.setColorTag(color_tag);
                        clan.setName(name);
                        clan.setPackedAllies(packed_allies);
                        clan.setPackedRivals(packed_rivals);
                        clan.setPackedBb(packed_bb);
                        clan.setFounded(founded);
                        clan.setLastUsed(last_used);

                        out = clan;
                    }
                    catch (Exception ex)
                    {
                        for (StackTraceElement el : ex.getStackTrace())
                        {
                            System.out.print(el.toString());
                        }
                    }
                }
            }
            catch (SQLException ex)
            {
                zClans.getLog().severe(String.format("An Error occurred: %s", ex.getErrorCode()));
                zClans.getLog().log(Level.SEVERE, null, ex);
            }
        }

        return out;
    }

    /**
     * Retrieves all clan players from the database
     *
     * @return
     */
    public List<ClanPlayer> retrieveClanPlayers()
    {
        List<ClanPlayer> out = new ArrayList<>();

        String query = "SELECT * FROM  `sc_players`;";
        ResultSet res = core.select(query);

        if (res != null)
        {
            try
            {
                while (res.next())
                {
                    try
                    {
                        String uuid = res.getString("uuid");
                        String tag = res.getString("tag");
                        boolean leader = res.getBoolean("leader");
                        boolean friendly_fire = res.getBoolean("friendly_fire");
                        boolean trusted = res.getBoolean("trusted");
                        int neutral_kills = res.getInt("neutral_kills");
                        int rival_kills = res.getInt("rival_kills");
                        int civilian_kills = res.getInt("civilian_kills");
                        int deaths = res.getInt("deaths");
                        long last_seen = res.getLong("last_seen");
                        long join_date = res.getLong("join_date");
                        String flags = res.getString("flags");
                        String packed_past_clans = Helper.parseColors(res.getString("packed_past_clans"));
                        int banned = res.getInt("banned");

                        if (last_seen == 0)
                        {
                            last_seen = (new Date()).getTime();
                        }

                        if (join_date == 0)
                        {
                            join_date = (new Date()).getTime();
                        }

                        ClanPlayer cp = new ClanPlayer();
                        if (uuid != null)
                        {
                            cp.setUniqueId(UUID.fromString(uuid));
                        }
                        cp.setFlags(flags);
                        cp.setName(UUIDUtil.stringUUIDToName(uuid));
                        cp.setLeader(leader);
                        cp.setFriendlyFire(friendly_fire);
                        cp.setNeutralKills(neutral_kills);
                        cp.setRivalKills(rival_kills);
                        cp.setCivilianKills(civilian_kills);
                        cp.setDeaths(deaths);
                        cp.setLastSeen(last_seen);
                        cp.setJoinDate(join_date);
                        cp.setPackedPastClans(packed_past_clans);
                        cp.setTrusted(leader || trusted);
                        cp.setBanned(banned);

                        if (!tag.isEmpty())
                        {
                            Clan clan = zClans.getInstance().getClanManager().getClan(tag);

                            if (clan != null)
                            {
                                cp.setClan(clan);
                            }
                        }

                        out.add(cp);
                    }
                    catch (Exception ex)
                    {
                        for (StackTraceElement el : ex.getStackTrace())
                        {
                            System.out.print(el.toString());
                        }
                    }
                }
            }
            catch (SQLException ex)
            {
                zClans.getLog().severe(String.format("An Error occurred: %s", ex.getErrorCode()));
                zClans.getLog().log(Level.SEVERE, null, ex);
            }
        }

        return out;
    }

    /**
     * Retrieves one clan player from the database
     * Used for BungeeCord Reload ClanPlayer and your Clan
     *
     * @param playerUniqueId
     * @return
     */
    public ClanPlayer retrieveOneClanPlayer(UUID playerUniqueId)
    {
        ClanPlayer out = null;

        String query = "SELECT * FROM `sc_players` WHERE `uuid` = '" + playerUniqueId.toString() + "';";
        ResultSet res = core.select(query);

        if (res != null)
        {
            try
            {
                while (res.next())
                {
                    try
                    {
                        String uuid = res.getString("uuid");
                        String tag = res.getString("tag");
                        boolean leader = res.getBoolean("leader");
                        boolean friendly_fire = res.getBoolean("friendly_fire");
                        boolean trusted = res.getBoolean("trusted");
                        int neutral_kills = res.getInt("neutral_kills");
                        int rival_kills = res.getInt("rival_kills");
                        int civilian_kills = res.getInt("civilian_kills");
                        int deaths = res.getInt("deaths");
                        long last_seen = res.getLong("last_seen");
                        long join_date = res.getLong("join_date");
                        String flags = res.getString("flags");
                        String packed_past_clans = Helper.parseColors(res.getString("packed_past_clans"));

                        if (last_seen == 0)
                        {
                            last_seen = (new Date()).getTime();
                        }

                        if (join_date == 0)
                        {
                            join_date = (new Date()).getTime();
                        }

                        ClanPlayer cp = new ClanPlayer();
                        if (uuid != null)
                        {
                            cp.setUniqueId(UUID.fromString(uuid));
                        }
                        cp.setFlags(flags);
                        cp.setName(UUIDUtil.stringUUIDToName(uuid));
                        cp.setLeader(leader);
                        cp.setFriendlyFire(friendly_fire);
                        cp.setNeutralKills(neutral_kills);
                        cp.setRivalKills(rival_kills);
                        cp.setCivilianKills(civilian_kills);
                        cp.setDeaths(deaths);
                        cp.setLastSeen(last_seen);
                        cp.setJoinDate(join_date);
                        cp.setPackedPastClans(packed_past_clans);
                        cp.setTrusted(leader || trusted);

                        if (!tag.isEmpty())
                        {
                            Clan clanDB = retrieveOneClan(tag);
                            Clan clan = zClans.getInstance().getClanManager().getClan(tag);

                            if (clan != null)
                            {
                                Clan clanReSync = zClans.getInstance().getClanManager().getClan(tag);
                                clanReSync.setFlags(clanDB.getFlags());
                                clanReSync.setVerified(clanDB.isVerified());
                                clanReSync.setFriendlyFire(clanDB.isFriendlyFire());
                                clanReSync.setTag(clanDB.getTag());
                                clanReSync.setColorTag(clanDB.getColorTag());
                                clanReSync.setName(clanDB.getName());
                                clanReSync.setPackedAllies(clanDB.getPackedAllies());
                                clanReSync.setPackedRivals(clanDB.getPackedRivals());
                                clanReSync.setPackedBb(clanDB.getPackedBb());
                                clanReSync.setFounded(clanDB.getFounded());
                                clanReSync.setLastUsed(clanDB.getLastUsed());
                                cp.setClan(clanReSync);
                            }
                            else
                            {
                                plugin.getClanManager().importClan(clanDB);
                                Clan newclan = zClans.getInstance().getClanManager().getClan(clanDB.getTag());
                                cp.setClan(newclan);
                            }
                        }

                        out = cp;
                    }
                    catch (Exception ex)
                    {
                        for (StackTraceElement el : ex.getStackTrace())
                        {
                            System.out.print(el.toString());
                        }
                    }
                }
            }
            catch (SQLException ex)
            {
                zClans.getLog().severe(String.format("An Error occurred: %s", ex.getErrorCode()));
                zClans.getLog().log(Level.SEVERE, null, ex);
            }
        }

        return out;
    }

    /**
     * Insert a clan into the database
     *
     * @param clan
     */
    public void insertClan(Clan clan)
    {
        String query = "INSERT INTO `sc_clans` (  `verified`, `tag`, `color_tag`, `name`, `friendly_fire`, `founded`, `last_used`, `packed_allies`, `packed_rivals`, `packed_bb`, `flags`) ";
        String values = "VALUES ( " + (clan.isVerified() ? 1 : 0) + ",'" + Helper.escapeQuotes(clan.getTag()) + "','" + Helper.escapeQuotes(clan.getColorTag()) + "','" + Helper.escapeQuotes(clan.getName()) + "'," + (clan.isFriendlyFire() ? 1 : 0) + ",'" + clan.getFounded() + "','" + clan.getLastUsed() + "','" + Helper.escapeQuotes(clan.getPackedAllies()) + "','" + Helper.escapeQuotes(clan.getPackedRivals()) + "','" + Helper.escapeQuotes(clan.getPackedBb()) + "','" + Helper.escapeQuotes(clan.getFlags()) + "');";
        core.insert(query + values);
    }

    /**
     * Update a clan to the database asynchronously
     *
     * @param clan
     */
    @SuppressWarnings("deprecation")
	public void updateClanAsync(final Clan clan)
    {
        plugin.getServer().getScheduler().scheduleAsyncDelayedTask(plugin, new Runnable()
        {
            @Override
            public void run()
            {
                updateClan(clan);
            }
        });
    }

    /**
     * Update a clan to the database
     *
     * @param clan
     */
    public void updateClan(Clan clan)
    {
        clan.updateLastUsed();
        String query = "UPDATE `sc_clans` SET verified = " + (clan.isVerified() ? 1 : 0) + ", tag = '" + Helper.escapeQuotes(clan.getTag()) + "', color_tag = '" + Helper.escapeQuotes(clan.getColorTag()) + "', friendly_fire = " + (clan.isFriendlyFire() ? 1 : 0) + ", founded = '" + clan.getFounded() + "', last_used = '" + clan.getLastUsed() + "', packed_allies = '" + Helper.escapeQuotes(clan.getPackedAllies()) + "', packed_rivals = '" + Helper.escapeQuotes(clan.getPackedRivals()) + "', packed_bb = '" + Helper.escapeQuotes(clan.getPackedBb()) + "', flags = '" + "', flags = '" + Helper.escapeQuotes(clan.getFlags()) + "' WHERE tag = '" + Helper.escapeQuotes(clan.getTag()) + "';";
        core.update(query);
    }

    /**
     * Delete a clan from the database
     *
     * @param clan
     */
    public void deleteClan(Clan clan)
    {
        String query = "DELETE FROM `sc_clans` WHERE tag = '" + clan.getTag() + "';";
        core.delete(query);
    }

    /**
     * Insert a clan player into the database
     *
     * @param cp
     */
    public void insertClanPlayer(ClanPlayer cp)
    {
        String query = "INSERT INTO `sc_players` ( `uuid`, `leader`, `tag`, `friendly_fire`, `neutral_kills`, `rival_kills`, `civilian_kills`, `deaths`, `last_seen`, `join_date`, `packed_past_clans`, `flags`, `banned`) ";
        String values = "VALUES ( '" + cp.getUniqueId().toString() + "'," + (cp.isLeader() ? 1 : 0) + ",'" + Helper.escapeQuotes(cp.getTag()) + "'," + (cp.isFriendlyFire() ? 1 : 0) + "," + cp.getNeutralKills() + "," + cp.getRivalKills() + "," + cp.getCivilianKills() + "," + cp.getDeaths() + ",'" + cp.getLastSeen() + "',' " + cp.getJoinDate() + "','" + Helper.escapeQuotes(cp.getPackedPastClans()) + "','" + Helper.escapeQuotes(cp.getFlags()) + "'," + cp.getBanned() + ");";
        core.insert(query + values);
    }

    /**
     * Update a clan player to the database asynchronously
     *
     * @param cp
     */
    @SuppressWarnings("deprecation")
	public void updateClanPlayerAsync(final ClanPlayer cp)
    {
        plugin.getServer().getScheduler().scheduleAsyncDelayedTask(plugin, new Runnable()
        {
            @Override
            public void run()
            {
                updateClanPlayer(cp);
            }
        });
    }

    /**
     * Update a clan player to the database
     *
     * @param cp
     */
    public void updateClanPlayer(ClanPlayer cp)
    {
        cp.updateLastSeen();

        String query = "UPDATE `sc_players` SET leader = " + (cp.isLeader() ? 1 : 0) + ", tag = '" + Helper.escapeQuotes(cp.getTag()) + "' , friendly_fire = " + (cp.isFriendlyFire() ? 1 : 0) + ", neutral_kills = " + cp.getNeutralKills() + ", rival_kills = " + cp.getRivalKills() + ", civilian_kills = " + cp.getCivilianKills() + ", deaths = " + cp.getDeaths() + ", last_seen = '" + cp.getLastSeen() + "', packed_past_clans = '" + Helper.escapeQuotes(cp.getPackedPastClans()) + "', trusted = " + (cp.isTrusted() ? 1 : 0) + ", flags = '" + Helper.escapeQuotes(cp.getFlags())+ "', banned = " + cp.getBanned() + " WHERE `uuid` = '" + cp.getUniqueId().toString() + "';";
        core.update(query);
    }

    /**
     * Delete a clan player from the database
     *
     * @param cp
     */
    public void deleteClanPlayer(ClanPlayer cp)
    {
        String query = "DELETE FROM `sc_players` WHERE uuid = '" + cp.getUniqueId() + "';";
        core.delete(query);
        deleteKills(cp.getUniqueId());
    }

    /**
     * Insert a kill into the database
     *
     * @param attacker
     * @param attackerTag
     * @param victim
     * @param victimTag
     * @param type
     */
    public void insertKill(Player attacker, String attackerTag, Player victim, String victimTag, String type)
    {
        String query = "INSERT INTO `sc_kills` (  `attacker_uuid`, `attacker_tag`, `victim_uuid`, `victim_tag`, `kill_type`) ";
        String values = "VALUES ( '" + attacker.getUniqueId() + "','" + attackerTag + "','" + victim.getUniqueId() + "','" + victimTag + "','" + type + "');";
        core.insert(query + values);
    }

    /**
     * Delete a player's kill record form the database
     *
     * @param playerUniqueId
     */
    public void deleteKills(UUID playerUniqueId)
    {
        String query = "DELETE FROM `sc_kills` WHERE `attacker_uuid` = '" + playerUniqueId + "'";
        core.delete(query);
    }

    /**
     * Returns a map of victim->count of all kills that specific player did
     *
     * @param playerName
     * @return
     */
    public Map<String, Integer> getKillsPerPlayer(String playerName)
    {
   
        HashMap<String, Integer> out = new HashMap<>();

        String UUID = UUIDUtil.nameToUUID(playerName).toString();

        String query = "SELECT victim_uuid, count(victim_uuid) AS kills FROM `sc_kills` WHERE attacker_uuid = '" + UUID + "' GROUP BY victim_uuid ORDER BY count(victim_uuid) DESC;";
        ResultSet res = core.select(query);

        if (res != null)
        {
            try
            {
                while (res.next())
                {
                    try
                    {
                        String victim = UUIDUtil.stringUUIDToName(res.getString("victim_uuid"));
                        int kills = res.getInt("kills");
                        out.put(victim, kills);
                    }
                    catch (Exception ex)
                    {
                        zClans.getLog().info(ex.getMessage());


                    }
                }
            }
            catch (SQLException ex)
            {
                zClans.getLog().severe(String.format("An Error occurred: %s", ex.getErrorCode()));
                zClans.getLog().log(Level.SEVERE, null, ex);
            }
        }

        return out;
    }

    /**
     * Returns a map of tag->count of all kills
     *
     * @return
     */
    public Map<String, Integer> getMostKilled()
    {
        HashMap<String, Integer> out = new HashMap<>();

        String query = "SELECT attacker_uuid, victim_uuid, count(victim_uuid) AS kills FROM `sc_kills` GROUP BY attacker_uuid, victim_uuid ORDER BY 3 DESC;";
        ResultSet res = core.select(query);

        if (res != null)
        {
            try
            {
                while (res.next())
                {
                    try
                    {
                        String attacker = UUIDUtil.stringUUIDToName(res.getString("attacker_uuid"));
                        String victim = UUIDUtil.stringUUIDToName(res.getString("victim_uuid"));
                        int kills = res.getInt("kills");
                        out.put(attacker + " " + victim, kills);
                    }
                    catch (Exception ex)
                    {
                        zClans.getLog().info(ex.getMessage());


                    }
                }
            }
            catch (SQLException ex)
            {
                zClans.getLog().severe(String.format("An Error occurred: %s", ex.getErrorCode()));
                zClans.getLog().log(Level.SEVERE, null, ex);
            }
        }

        return out;
    }

    /**
     * Updates the database to the latest version
     *
     * @param
     */
    private void updateDatabase()
    {
        String query = null;

        if (!core.existsColumn("sc_players", "banned")) {
            query = "ALTER TABLE sc_players ADD `banned` tinyint(1) default '0';";
            core.execute(query);
        }
        
        if (core.existsColumn("sc_players", "uuid")
                && !plugin.getSettingsManager().isUseMysql()) {
           query = "CREATE UNIQUE INDEX IF NOT EXISTS `uq_player_uuid` ON `sc_players` (`uuid`);";
           core.execute(query);
        }
    }
}
