package org.bitbucket.zenith4183.zclans;

import org.bitbucket.zenith4183.zclans.events.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.*;

/**
 * @author phaed
 */
public class Clan implements Serializable, Comparable<Clan>
{

    private static final long serialVersionUID = 1L;
    private boolean verified;
    private String tag;
    private String colorTag;
    private String name;
    private boolean friendlyFire;
    private long founded;
    private long lastUsed;
    private List<String> allies = new ArrayList<>();
    private List<String> rivals = new ArrayList<>();
    private List<String> bb = new ArrayList<>();
    private List<String> members = new ArrayList<>();
    private int homeX = 0;
    private int homeY = 0;
    private int homeZ = 0;
    private String homeWorld = "";
    private boolean allowWithdraw = false;
    private boolean allowDeposit = true;

    /**
     *
     */
    public Clan()
    {
        this.tag = "";
    }

    /**
     * @param tag
     * @param name
     * @param verified
     */
    public Clan(String tag, String name, boolean verified)
    {
        this.tag = Helper.cleanTag(tag);
        this.colorTag = Helper.parseColors(tag);
        this.name = name;
        this.founded = (new Date()).getTime();
        this.lastUsed = (new Date()).getTime();
        this.verified = verified;

        if (zClans.getInstance().getSettingsManager().isClanFFOnByDefault())
        {
            friendlyFire = true;
        }
    }

    @Override
    public int hashCode()
    {
        return getTag().hashCode() >> 13;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof Clan))
        {
            return false;
        }

        Clan other = (Clan) obj;
        return other.getTag().equals(this.getTag());
    }

    @Override
    public int compareTo(Clan other)
    {
        return this.getTag().compareToIgnoreCase(other.getTag());
    }

    @Override
    public String toString()
    {
        return tag;
    }

    /**
     * Returns the clan's name
     *
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * (used internally)
     *
     * @param name the name to set
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Returns the clan's tag clean (no colors)
     *
     * @return the tag
     */
    public String getTag()
    {
        return tag;
    }

    /**
     * (used internally)
     *
     * @param tag the tag to set
     */
    public void setTag(String tag)
    {
        this.tag = tag;
    }

    /**
     * Returns the last used date in milliseconds
     *
     * @return the lastUsed
     */
    public long getLastUsed()
    {
        return lastUsed;
    }

    /**
     * Updates last used date to today (does not update clan on db)
     */
    public void updateLastUsed()
    {
        setLastUsed((new Date()).getTime());
    }

    /**
     * Returns the number of days the clan has been inactive
     *
     * @return
     */
    public int getInactiveDays()
    {
        Timestamp now = new Timestamp((new Date()).getTime());
        return (int) Math.floor(Dates.differenceInDays(new Timestamp(getLastUsed()), now));
    }

    /**
     * (used internally)
     *
     * @param lastUsed the lastUsed to set
     */
    public void setLastUsed(long lastUsed)
    {
        this.lastUsed = lastUsed;
    }

    /**
     * Check whether this clan allows friendly fire
     *
     * @return the friendlyFire
     */
    public boolean isFriendlyFire()
    {
        return friendlyFire;
    }

    /**
     * Sets the friendly fire status of this clan (does not update clan on db)
     *
     * @param friendlyFire the friendlyFire to set
     */
    public void setFriendlyFire(boolean friendlyFire)
    {
        this.friendlyFire = friendlyFire;
    }

    /**
     * Check if the player is a member of this clan
     *
     * @param player
     * @return confirmation
     */
    public boolean isMember(Player player)
    {
    	return this.members.contains(player.getUniqueId().toString());
    }

    /**
     * Check if the player is a member of this clan
     *
     * @param playerUniqueId
     * @return confirmation
     */
    public boolean isMember(UUID playerUniqueId)
    {
        return this.members.contains(playerUniqueId.toString());
    }


    /**
     * Returns a list with the contents of the bulletin board
     *
     * @return the bb
     */
    public List<String> getBb()
    {
        return Collections.unmodifiableList(bb);
    }

    /**
     * Return a list of all the allies' tags clean (no colors)
     *
     * @return the allies
     */
    public List<String> getAllies()
    {
        return Collections.unmodifiableList(allies);
    }

    private void addAlly(String tag)
    {
        allies.add(tag);
    }

    private boolean removeAlly(String ally)
    {
        if (!allies.contains(ally))
        {
            return false;
        }

        allies.remove(ally);
        return true;
    }

    /**
     * The founded date in milliseconds
     *
     * @return the founded
     */
    public long getFounded()
    {
        return founded;
    }

    /**
     * The string representation of the founded date
     *
     * @return
     */
    public String getFoundedString()
    {
        return new java.text.SimpleDateFormat("MMM dd, ''yy h:mm a").format(new Date(this.founded));
    }

    /**
     * (used internally)
     *
     * @param founded the founded to set
     */
    public void setFounded(long founded)
    {
        this.founded = founded;
    }

    /**
     * Returns the color tag for this clan
     *
     * @return the colorTag
     */
    public String getColorTag()
    {
        return colorTag;
    }

    /**
     * (used internally)
     *
     * @param colorTag the colorTag to set
     */
    public void setColorTag(String colorTag)
    {
        this.colorTag = Helper.parseColors(colorTag);
    }

    /**
     * Adds a bulletin board message without announcer
     *
     * @param msg
     */
    public void addBb(String msg)
    {
        while (bb.size() > zClans.getInstance().getSettingsManager().getBbSize())
        {
            bb.remove(0);
        }

        bb.add(msg);
        zClans.getInstance().getStorageManager().updateClan(this);
    }

    /**
     * Clears the bulletin board
     */
    public void clearBb()
    {
        bb.clear();
        zClans.getInstance().getStorageManager().updateClan(this);
    }

    /**
     * (used internally)
     *
     * @param cp
     */
    public void importMember(ClanPlayer cp)
    {
        String NameOrUUID;

        if (cp.getUniqueId() != null)
        {
            NameOrUUID = cp.getUniqueId().toString();
        }
        else
        {
            return;
        }

        if (!this.members.contains(NameOrUUID))
        {
            this.members.add(NameOrUUID);
        }
    }

    /**
     * (used internally)
     *
     * @param playerUniqueId
     */
    public void removeMember(UUID playerUniqueId)
    {
        this.members.remove(playerUniqueId.toString());
    }

    /**
     * Get total clan size
     *
     * @return
     */
    public int getSize()
    {
        return this.members.size();
    }

    /**
     * Returns a list of all rival tags clean (no colors)
     *
     * @return the rivals
     */
    public List<String> getRivals()
    {
        return Collections.unmodifiableList(rivals);
    }

    private void addRival(String tag)
    {
        rivals.add(tag);
    }

    private boolean removeRival(String rival)
    {
        if (!rivals.contains(rival))
        {
            return false;
        }

        rivals.remove(rival);
        return true;
    }

    /**
     * Check if the tag is a rival
     *
     * @param tag
     * @return
     */
    public boolean isRival(String tag)
    {
        return rivals.contains(tag);
    }

    /**
     * Check if the tag is an ally
     *
     * @param tag
     * @return
     */
    public boolean isAlly(String tag)
    {
        return allies.contains(tag);
    }

    /**
     * Tells you if the clan is verified, always returns true if no verification
     * is required
     *
     * @return
     */
    public boolean isVerified()
    {
        return !zClans.getInstance().getSettingsManager().isRequireVerification() || verified;

    }

    /**
     * (used internally)
     *
     * @param verified the verified to set
     */
    public void setVerified(boolean verified)
    {
        this.verified = verified;
    }


    /**
     * (used internally)
     *
     * @return the packedBb
     */
    public String getPackedBb()
    {
        return Helper.toMessage(bb, "|");
    }

    /**
     * (used internally)
     *
     * @param packedBb the packedBb to set
     */
    public void setPackedBb(String packedBb)
    {
        this.bb = Helper.fromArray(packedBb.split("[|]"));
    }

    /**
     * (used internally)
     *
     * @return the packedAllies
     */
    public String getPackedAllies()
    {
        return Helper.toMessage(allies, "|");
    }

    /**
     * (used internally)
     *
     * @param packedAllies the packedAllies to set
     */
    public void setPackedAllies(String packedAllies)
    {
        this.allies = Helper.fromArray(packedAllies.split("[|]"));
    }

    /**
     * (used internally)
     *
     * @return the packedRivals
     */
    public String getPackedRivals()
    {
        return Helper.toMessage(rivals, "|");
    }

    /**
     * (used internally)
     *
     * @param packedRivals the packedRivals to set
     */
    public void setPackedRivals(String packedRivals)
    {
        this.rivals = Helper.fromArray(packedRivals.split("[|]"));
    }

    /**
     * Returns a separator delimited string with all the ally clan's colored
     * tags
     *
     * @param sep
     * @return
     */
    public String getAllyString(String sep)
    {
        String out = "";

        for (String allyTag : getAllies())
        {
            Clan ally = zClans.getInstance().getClanManager().getClan(allyTag);

            if (ally != null)
            {
                out += ally.getColorTag() + sep;
            }
        }

        out = Helper.stripTrailing(out, sep);

        if (out.trim().isEmpty())
        {
            return ChatColor.BLACK + "None";
        }

        return Helper.parseColors(out);
    }

    /**
     * Returns a separator delimited string with all the rival clan's colored
     * tags
     *
     * @param sep
     * @return
     */
    public String getRivalString(String sep)
    {
        String out = "";

        for (String rivalTag : getRivals())
        {
            Clan rival = zClans.getInstance().getClanManager().getClan(rivalTag);

            if (rival != null)
            {
                out += rival.getColorTag() + sep;
            }
        }

        out = Helper.stripTrailing(out, sep);

        if (out.trim().isEmpty())
        {
            return ChatColor.BLACK + "None";
        }

        return Helper.parseColors(out);
    }

    /**
     * Returns a separator delimited string with all the leaders
     *
     * @param prefix
     * @param sep
     * @return the formatted leaders string
     */
    public String getLeadersString(String prefix, String sep)
    {
        String out = "";

        for (String member : members)
        {
            ClanPlayer cp = zClans.getInstance().getClanManager().getClanPlayer(UUID.fromString(member));

            if (cp == null)
            {
                continue;
            }

            if (cp.isLeader())
            {
                out += prefix + cp.getName() + sep;
            }
        }

        return Helper.stripTrailing(out, sep);
    }

    /**
     * Check if a player is a leader of a clan
     *
     * @param player
     * @return the leaders
     */
    public boolean isLeader(Player player)
    {
        return isLeader(player.getUniqueId());
    }

    /**
     * Check if a player is a leader of a clan
     *
     * @param playerUniqueId
     * @return the leaders
     */
    public boolean isLeader(UUID playerUniqueId)
    {
        if (isMember(playerUniqueId))
        {
            ClanPlayer cp = zClans.getInstance().getClanManager().getClanPlayer(playerUniqueId);

            if (cp != null && cp.isLeader())
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Get all members (leaders, and non-leaders) in the clan
     *
     * @return the members
     */
    public List<ClanPlayer> getMembers()
    {
        List<ClanPlayer> out = new ArrayList<>();

        for (String member : members)
        {
            ClanPlayer cp = zClans.getInstance().getClanManager().getClanPlayer(UUID.fromString(member));

            if (cp == null)
            {
                continue;
            }
            out.add(cp);
        }

        return out;
    }

    /**
     * Get all online members (leaders, and non-leaders) in the clan
     *
     * @return the members
     */
    public List<ClanPlayer> getOnlineMembers()
    {
        List<ClanPlayer> out = new ArrayList<>();

        for (String member : members)
        {
            ClanPlayer cp = zClans.getInstance().getClanManager().getClanPlayer(UUID.fromString(member));

            if (cp == null)
            {
                continue;
            }
            if (cp.toPlayer() != null && cp.toPlayer().isOnline())
            {
            	out.add(cp);
            }
        }

        return out;
    }

    /**
     * Get all leaders in the clan
     *
     * @return the leaders
     */
    public List<ClanPlayer> getLeaders()
    {
        List<ClanPlayer> out = new ArrayList<>();

        for (String member : members)
        {
            ClanPlayer cp = zClans.getInstance().getClanManager().getClanPlayer(UUID.fromString(member));

            if (cp == null)
            {
                continue;
            }

            if (cp.isLeader())
            {
                out.add(cp);
            }
        }

        return out;
    }

    /**
     * Get all non-leader players in the clan
     *
     * @return non leaders
     */
    public List<ClanPlayer> getNonLeaders()
    {
        List<ClanPlayer> out = new ArrayList<>();

        for (String member : members)
        {
            ClanPlayer cp = zClans.getInstance().getClanManager().getClanPlayer(UUID.fromString(member));

            if (cp == null)
            {
                continue;
            }

            if (!cp.isLeader())
            {
                out.add(cp);
            }
        }

        Collections.sort(out);

        return out;
    }

    /**
     * Get all clan's members
     *
     * @return
     */
    public List<ClanPlayer> getAllMembers()
    {
        List<ClanPlayer> out = new ArrayList<>();

        for (String member : members)
        {
            ClanPlayer cp = zClans.getInstance().getClanManager().getClanPlayer(UUID.fromString(member));

            if (cp == null)
            {
                continue;
            }

            out.add(cp);
        }

        Collections.sort(out);

        return out;
    }

    /**
     * Get all the ally clan's members
     *
     * @return
     */
    public Set<ClanPlayer> getAllAllyMembers()
    {
        Set<ClanPlayer> out = new HashSet<>();

        for (String tag : allies)
        {
            Clan ally = zClans.getInstance().getClanManager().getClan(tag);

            if (ally != null)
            {
                out.addAll(ally.getMembers());
            }
        }

        return out;
    }

    /**
     * Gets the clan's total KDR
     *
     * @return
     */
    public float getTotalKDR()
    {
        if (members.isEmpty())
        {
            return 0;
        }

        double totalWeightedKills = 0;
        int totalDeaths = 0;

        for (String member : members)
        {
            ClanPlayer cp = zClans.getInstance().getClanManager().getClanPlayer(UUID.fromString(member));

            if (cp == null)
            {
                continue;
            }

            totalWeightedKills += cp.getWeightedKills();
            totalDeaths += cp.getDeaths();
        }

        if (totalDeaths == 0)
        {
            totalDeaths = 1;
        }

        return ((float) totalWeightedKills) / ((float) totalDeaths);
    }

    /**
     * Gets the clan's total KDR
     *
     * @return
     */
    public int getTotalDeaths()
    {
        int totalDeaths = 0;

        if (members.isEmpty())
        {
            return totalDeaths;
        }

        for (String member : members)
        {
            ClanPlayer cp = zClans.getInstance().getClanManager().getClanPlayer(UUID.fromString(member));

            if (cp == null)
            {
                continue;
            }

            totalDeaths += cp.getDeaths();
        }

        return totalDeaths;
    }

    /**
     * Gets average weighted kills for the clan
     *
     * @return
     */
    public int getAverageWK()
    {
        int total = 0;

        if (members.isEmpty())
        {
            return total;
        }

        for (String member : members)
        {
            ClanPlayer cp;
            
            cp = zClans.getInstance().getClanManager().getClanPlayer(UUID.fromString(member));

            if (cp == null)
            {
                continue;
            }

            total += cp.getWeightedKills();
        }

        return total / getSize();
    }

    /**
     * Gets total rival kills for the clan
     *
     * @return
     */
    public int getTotalRival()
    {
        int total = 0;

        if (members.isEmpty())
        {
            return total;
        }

        for (String member : members)
        {
            ClanPlayer cp = zClans.getInstance().getClanManager().getClanPlayer(UUID.fromString(member));

            if (cp == null)
            {
                continue;
            }

            total += cp.getRivalKills();
        }

        return total;
    }

    /**
     * Gets total neutral kills for the clan
     *
     * @return
     */
    public int getTotalNeutral()
    {
        int total = 0;

        if (members.isEmpty())
        {
            return total;
        }

        for (String member : members)
        {
            ClanPlayer cp = zClans.getInstance().getClanManager().getClanPlayer(UUID.fromString(member));

            if (cp == null)
            {
                continue;
            }

            total += cp.getNeutralKills();
        }

        return total;
    }

    /**
     * Gets total civilian kills for the clan
     *
     * @return
     */
    public int getTotalCivilian()
    {
        int total = 0;

        if (members.isEmpty())
        {
            return total;
        }

        for (String member : members)
        {
            ClanPlayer cp = zClans.getInstance().getClanManager().getClanPlayer(UUID.fromString(member));

            if (cp == null)
            {
                continue;
            }

            total += cp.getCivilianKills();
        }

        return total;
    }

    /**
     * Check whether the clan has crossed the rival limit
     *
     * @return
     */
    public boolean reachedRivalLimit()
    {
        int rivalCount = rivals.size();
        int clanCount = zClans.getInstance().getClanManager().getRivableClanCount() - 1;
        int rivalPercent = zClans.getInstance().getSettingsManager().getRivalLimitPercent();

        double limit = ((double) clanCount) * (((double) rivalPercent) / ((double) 100));

        return rivalCount > limit;
    }

    /**
     * Add a new player to the clan
     *
     * @param cp
     */
    public void addPlayerToClan(ClanPlayer cp)
    {
        cp.removePastClan(getColorTag());
        cp.setClan(this);
        cp.setLeader(false);
        cp.setTrusted(zClans.getInstance().getSettingsManager().isClanTrustByDefault());

        importMember(cp);

        zClans.getInstance().getStorageManager().updateClanPlayer(cp);
        zClans.getInstance().getStorageManager().updateClan(this);

        Player player = zClans.getInstance().getServer().getPlayer(cp.getUniqueId());

        if (player != null)
        {
            zClans.getInstance().getClanManager().updateDisplayName(player);
        }
        zClans.getInstance().getServer().getPluginManager().callEvent(new PlayerJoinedClanEvent(this, cp));
    }

    /**
     * Remove a player from a clan
     *
     * @param playerUniqueId
     */
    public void removePlayerFromClan(UUID playerUniqueId)
    {
        ClanPlayer cp = zClans.getInstance().getClanManager().getClanPlayer(playerUniqueId);

        cp.setClan(null);
        cp.addPastClan(getColorTag() + (cp.isLeader() ? ChatColor.DARK_RED + "*" : ""));
        cp.setLeader(false);
        cp.setTrusted(false);
        cp.setJoinDate(0);
        removeMember(playerUniqueId);

        zClans.getInstance().getStorageManager().updateClanPlayer(cp);
        zClans.getInstance().getStorageManager().updateClan(this);

        Player matched = zClans.getInstance().getServer().getPlayer(playerUniqueId);

        if (matched != null)
        {
            zClans.getInstance().getClanManager().updateDisplayName(matched);
        }
        zClans.getInstance().getServer().getPluginManager().callEvent(new PlayerKickedClanEvent(this, cp));
    }

    /**
     * Promote a member to a leader of a clan
     *
     * @param playerUniqueId
     */
    public void promote(UUID playerUniqueId)
    {
        ClanPlayer cp = zClans.getInstance().getClanManager().getClanPlayer(playerUniqueId);

        cp.setLeader(true);
        cp.setTrusted(true);

        zClans.getInstance().getStorageManager().updateClanPlayer(cp);
        zClans.getInstance().getStorageManager().updateClan(this);

        // add clan permission
        zClans.getInstance().getServer().getPluginManager().callEvent(new PlayerPromoteEvent(this, cp));
    }

    /**
     * Demote a leader back to a member of a clan
     *
     * @param playerUniqueId
     */
    public void demote(UUID playerUniqueId)
    {
        ClanPlayer cp = zClans.getInstance().getClanManager().getClanPlayer(playerUniqueId);

        cp.setLeader(false);

        zClans.getInstance().getStorageManager().updateClanPlayer(cp);
        zClans.getInstance().getStorageManager().updateClan(this);

        // add clan permission
        zClans.getInstance().getServer().getPluginManager().callEvent(new PlayerDemoteEvent(this, cp));
    }

    /**
     * Add an ally to a clan, and the clan to the ally
     *
     * @param ally
     */
    public void addAlly(Clan ally)
    {
        removeRival(ally.getTag());
        addAlly(ally.getTag());

        ally.removeRival(getTag());
        ally.addAlly(getTag());

        zClans.getInstance().getStorageManager().updateClan(this);
        zClans.getInstance().getStorageManager().updateClan(ally);
        zClans.getInstance().getServer().getPluginManager().callEvent(new AllyClanAddEvent(this, ally));
    }

    /**
     * Remove an ally form the clan, and the clan from the ally
     *
     * @param ally
     */
    public void removeAlly(Clan ally)
    {
        removeAlly(ally.getTag());
        ally.removeAlly(getTag());

        zClans.getInstance().getStorageManager().updateClan(this);
        zClans.getInstance().getStorageManager().updateClan(ally);
        zClans.getInstance().getServer().getPluginManager().callEvent(new AllyClanRemoveEvent(this, ally));
    }

    /**
     * Add a rival to the clan, and the clan to the rival
     *
     * @param rival
     */
    public void addRival(Clan rival)
    {
        removeAlly(rival.getTag());
        addRival(rival.getTag());

        rival.removeAlly(getTag());
        rival.addRival(getTag());

        zClans.getInstance().getStorageManager().updateClan(this);
        zClans.getInstance().getStorageManager().updateClan(rival);
        zClans.getInstance().getServer().getPluginManager().callEvent(new RivalClanAddEvent(this, rival));
    }

    /**
     * Removes a rival from the clan, the clan from the rival
     *
     * @param rival
     */
    public void removeRival(Clan rival)
    {
        removeRival(rival.getTag());
        rival.removeRival(getTag());

        zClans.getInstance().getStorageManager().updateClan(this);
        zClans.getInstance().getStorageManager().updateClan(rival);
        zClans.getInstance().getServer().getPluginManager().callEvent(new RivalClanRemoveEvent(this, rival));
    }

    /**
     * Verify a clan
     */
    public void verifyClan()
    {
        setVerified(true);
        zClans.getInstance().getStorageManager().updateClan(this);
    }

    /**
     * Check whether any clan member is online
     *
     * @return
     */
    public boolean isAnyOnline()
    {
        for (String member : members)
        {
            if (Helper.isOnline(UUID.fromString(member)))
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Check whether all leaders of a clan are online
     *
     * @return
     */
    public boolean allLeadersOnline()
    {
        List<ClanPlayer> leaders = getLeaders();

        for (ClanPlayer leader : leaders)
        {
            if (!Helper.isOnline(leader.getUniqueId()))
            {
                return false;
            }
        }

        return true;
    }

    /**
     * Check whether all leaders, except for the one passed in, are online
     *
     * @param playerUniqueId
     * @return
     */
    public boolean allOtherLeadersOnline(UUID playerUniqueId)
    {
        List<ClanPlayer> leaders = getLeaders();

        for (ClanPlayer leader : leaders)
        {
            if (leader.getUniqueId().equals(playerUniqueId))
            {
                continue;
            }

            if (!Helper.isOnline(leader.getUniqueId()))
            {
                return false;
            }
        }

        return true;
    }

    /**
     * Change a clan's tag
     *
     * @param tag
     */
    public void changeClanTag(String tag)
    {
        setColorTag(tag);
        zClans.getInstance().getStorageManager().updateClan(this);
    }

    /**
     * Announce message to a whole clan
     *
     * @param playerName
     * @param msg
     */
    public void clanAnnounce(String playerName, String msg)
    {
        String message = zClans.getInstance().getSettingsManager().getClanChatAnnouncementColor() + msg;

        for (ClanPlayer cp : getMembers())
        {
            Player pl = cp.toPlayer();

            if (pl != null)
            {
                ChatBlock.sendMessage(pl, message);
            }
        }

        zClans.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "[" + zClans.getInstance().getLang("clan.announce") + ChatColor.AQUA + "] " + ChatColor.AQUA + "[" + Helper.getColorName(playerName) + ChatColor.WHITE + "] " + message);
    }

    /**
     * Announce message to a all the leaders of a clan
     *
     * @param msg
     */
    public void leaderAnnounce(String msg)
    {
        String message = zClans.getInstance().getSettingsManager().getClanChatAnnouncementColor() + msg;

        List<ClanPlayer> leaders = getLeaders();

        for (ClanPlayer cp : leaders)
        {
            Player pl = cp.toPlayer();

            if (pl != null)
            {
                ChatBlock.sendMessage(pl, message);
            }
        }
        zClans.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "[" + zClans.getInstance().getLang("leader.announce") + ChatColor.AQUA + "] " + ChatColor.WHITE + message);
    }

    /**
     * Announce message to a whole clan plus audio alert
     *
     * @param playerName
     * @param msg
     */
    public void audioAnnounce(String playerName, String msg)
    {
        clanAnnounce(playerName, msg);

        for (String member : members)
        {
            Player pl = zClans.getInstance().getServer().getPlayer(UUID.fromString(member));

            if (pl == null)
            {
                continue;
            }

        }
    }

    /**
     * Add a new bb message and announce it to all online members of a clan
     *
     * @param announcerName
     * @param msg
     */
    public void addBb(String announcerName, String msg)
    {
        if (isVerified())
        {
            addBb(zClans.getInstance().getSettingsManager().getBbColor() + msg);
            clanAnnounce(announcerName, zClans.getInstance().getSettingsManager().getBbAccentColor() + "* " + zClans.getInstance().getSettingsManager().getBbColor() + Helper.parseColors(msg));
        }
    }

    /**
     * Displays bb to a player
     *
     * @param player
     */
    public void displayBb(Player player)
    {
        if (isVerified())
        {
            ChatBlock.sendBlank(player);
            ChatBlock.saySingle(player, MessageFormat.format(zClans.getInstance().getLang("bulletin.board.header"), zClans.getInstance().getSettingsManager().getBbAccentColor(), zClans.getInstance().getSettingsManager().getPageHeadingsColor(), Helper.capitalize(getName())));

            int maxSize = zClans.getInstance().getSettingsManager().getBbSize();

            while (bb.size() > maxSize)
            {
                bb.remove(0);
            }

            for (String msg : bb)
            {
                ChatBlock.sendMessage(player, zClans.getInstance().getSettingsManager().getBbAccentColor() + "* " + zClans.getInstance().getSettingsManager().getBbColor() + Helper.parseColors(msg));
            }
            ChatBlock.sendBlank(player);
        }
    }

    /**
     * Disband a clan
     */
    public void disband()
    {
        zClans.getInstance().getServer().getPluginManager().callEvent(new DisbandClanEvent(this));
        Collection<ClanPlayer> clanPlayers = zClans.getInstance().getClanManager().getAllClanPlayers();
        List<Clan> clans = zClans.getInstance().getClanManager().getClans();

        for (ClanPlayer cp : clanPlayers)
        {
            if (cp.getTag().equals(getTag()))
            {
                cp.setClan(null);

                if (isVerified())
                {
                    cp.addPastClan(getColorTag() + (cp.isLeader() ? ChatColor.DARK_RED + "*" : ""));
                }

                cp.setLeader(false);

                zClans.getInstance().getStorageManager().updateClanPlayer(cp);

            }
        }

        clans.remove(this);

        for (Clan c : clans)
        {
            String disbanded = zClans.getInstance().getLang("clan.disbanded");

            if (c.removeRival(getTag()))
            {
                c.addBb(disbanded, ChatColor.AQUA + MessageFormat.format(zClans.getInstance().getLang("has.been.disbanded.rivalry.ended"), Helper.capitalize(getName())));
            }

            if (c.removeAlly(getTag()))
            {
                c.addBb(disbanded, ChatColor.AQUA + MessageFormat.format(zClans.getInstance().getLang("has.been.disbanded.alliance.ended"), Helper.capitalize(getName())));
            }
        }

        final Clan thisOne = this;

        zClans.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(zClans.getInstance(), new Runnable()
        {

            @Override
            public void run()
            {
                zClans.getInstance().getClanManager().removeClan(thisOne.getTag());
                zClans.getInstance().getStorageManager().deleteClan(thisOne);
            }
        }, 1);
    }

    /**
     * Whether this clan can be rivaled
     *
     * @return
     */
    public boolean isUnrivable()
    {
        return zClans.getInstance().getSettingsManager().isUnrivable(getTag());
    }

    /**
     * Return the list of flags and their data as a json string
     *
     * @return the flags
     */
    public String getFlags()
    {
        JSONObject json = new JSONObject();

        // writing the list of flags to json

        json.put("homeX", homeX);
        json.put("homeY", homeY);
        json.put("homeZ", homeZ);
        json.put("homeWorld", homeWorld == null ? "" : homeWorld);

        return json.toString();
    }

    /**
     * Read the list of flags in from a json string
     *
     * @param flagString the flags to set
     */
    public void setFlags(String flagString)
    {
        if (flagString != null && !flagString.isEmpty())
        {
            JSONObject flags = (JSONObject) JSONValue.parse(flagString);

            if (flags != null)
            {
                for (Object flag : flags.keySet())
                {
                    // reading the list of flags from json

                    try
                    {
                        
                        if (flag.equals("homeX"))
                        {
                            homeX = ((Long) flags.get(flag)).intValue();
                        }

                        if (flag.equals("homeY"))
                        {
                            homeY = ((Long) flags.get(flag)).intValue();
                        }

                        if (flag.equals("homeZ"))
                        {
                            homeZ = ((Long) flags.get(flag)).intValue();
                        }

                        if (flag.equals("homeWorld"))
                        {
                            homeWorld = (String) flags.get(flag);
                        }
                    }
                    catch (Exception ex)
                    {
                        for (StackTraceElement el : ex.getStackTrace())
                        {
                            System.out.print("Failed reading flag: " + flag);
                            System.out.print("Value: " + flags.get(flag));
                            System.out.print(el.toString());
                        }
                    }
                }
            }
        }
    }

    public void setHomeLocation(Location home)
    {
        if (home == null)
        {
            homeY = 0;
            homeX = 0;
            homeZ = 0;
            homeWorld = null;
        }
        else
        {
            home.setY(home.getBlockY() + 1);

            homeX = home.getBlockX();
            homeY = home.getBlockY();
            homeZ = home.getBlockZ();
            homeWorld = home.getWorld().getName();
        }
        zClans.getInstance().getStorageManager().updateClan(this);
    }

    public Location getHomeLocation()
    {
        World world = zClans.getInstance().getServer().getWorld(homeWorld);

        if (world != null)
        {
            if (!(world.getBlockAt(homeX, homeY, homeZ).getType().equals(Material.AIR)) || !(world.getBlockAt(homeX, homeY + 1, homeZ).getType().equals(Material.AIR)) || homeY == 0)
            {
                return new Location(world, homeX, world.getHighestBlockYAt(homeX, homeZ), homeZ);
            }
            else
            {
                return new Location(world, homeX, homeY, homeZ);
            }

        }

        return null;
    }

    public String getTagLabel(boolean isLeader)
    {
        zClans plugin = zClans.getInstance();

        if (isLeader)
        {
            return plugin.getSettingsManager().getTagBracketLeaderColor() +
                    plugin.getSettingsManager().getTagBracketLeft() +
                    plugin.getSettingsManager().getTagDefaultColor() +
                    getColorTag() +
                    plugin.getSettingsManager().getTagBracketLeaderColor() +
                    plugin.getSettingsManager().getTagBracketRight() +
                    plugin.getSettingsManager().getTagSeparatorLeaderColor() +
                    plugin.getSettingsManager().getTagSeparator();
        }
        else
        {
            return plugin.getSettingsManager().getTagBracketColor() +
                    plugin.getSettingsManager().getTagBracketLeft() +
                    plugin.getSettingsManager().getTagDefaultColor() +
                    getColorTag() +
                    plugin.getSettingsManager().getTagBracketColor() +
                    plugin.getSettingsManager().getTagBracketRight() +
                    plugin.getSettingsManager().getTagSeparatorColor() +
                    plugin.getSettingsManager().getTagSeparator();
        }
    }

    /**
     * @return the allowWithdraw
     */
    public boolean isAllowWithdraw()
    {
        return allowWithdraw;
    }

    /**
     * @param allowWithdraw the allowWithdraw to set
     */
    public void setAllowWithdraw(boolean allowWithdraw)
    {
        this.allowWithdraw = allowWithdraw;
    }

    /**
     * @return the allowDeposit
     */
    public boolean isAllowDeposit()
    {
        return allowDeposit;
    }

    /**
     * @param allowDeposit the allowDeposit to set
     */
    public void setAllowDeposit(boolean allowDeposit)
    {
        this.allowDeposit = allowDeposit;
    }
}
