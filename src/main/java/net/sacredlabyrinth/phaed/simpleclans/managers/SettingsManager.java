package net.sacredlabyrinth.phaed.simpleclans.managers;

import net.sacredlabyrinth.phaed.simpleclans.Helper;
import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * @author phaed
 */
public final class SettingsManager
{
    private boolean onlineMode;
    private String clanChatRankColor;
    private boolean tagBasedClanChat;
    private boolean teleportOnSpawn;
    private boolean dropOnHome;
    private boolean keepOnHome;
    private boolean debugging;
    private SimpleClans plugin;
    private boolean useColorCodeFromPrefix;
    private boolean confirmationForPromote;
    private boolean confirmationForDemote;
    private boolean globalff;
    private boolean showUnverifiedOnList;
    private boolean requireVerification;
    private List<Integer> itemsList;
    private List<String> blacklistedWorlds;
    private List<String> disallowedWords;
    private List<String> disallowedColors;
    private List<String> unRivableClans;
    private int rivalLimitPercent;
    private String serverAccount;
    private boolean ePurchaseCreation;
    private boolean ePurchaseVerification;
    private boolean ePurchaseInvite;
    private boolean ePurchaseHomeTeleport;
    private boolean ePurchaseHomeTeleportSet;
    private double eCreationPrice;
    private double eVerificationPrice;
    private double eInvitePrice;
    private double eHomeTeleportPrice;
    private double eHomeTeleportPriceSet;
    private String serverName;
    private boolean chatTags;
    private int purgeClan;
    private int purgeUnverified;
    private int purgePlayers;
    private int requestFreqencySecs;
    private String requestMessageColor;
    private int pageSize;
    private String pageSep;
    private String pageHeadingsColor;
    private String pageSubTitleColor;
    private String pageLeaderColor;
    private String pageTrustedColor;
    private String pageUnTrustedColor;
    private boolean bbShowOnLogin;
    private int bbSize;
    private String bbColor;
    private String bbAccentColor;
    private String commandClan;
    private String commandAlly;
    private String commandGlobal;
    private String commandMore;
    private String commandDeny;
    private String commandAccept;
    private int clanMinSizeToAlly;
    private int clanMinSizeToRival;
    private int clanMinLength;
    private int clanMaxLength;
    private String pageClanNameColor;
    private int tagMinLength;
    private int tagMaxLength;
    private String tagDefaultColor;
    private String tagSeparator;
    private String tagSeparatorColor;
    private String tagSeparatorLeaderColor;
    private String tagBracketLeft;
    private String tagBracketRight;
    private String tagBracketColor;
    private String tagBracketLeaderColor;
    private boolean clanTrustByDefault;
    private boolean allyChatEnable;
    private boolean allyChatFilter;
    private String allyChatMessageColor;
    private String allyChatNameColor;
    private String allyChatTagColor;
    private String allyChatTagBracketLeft;
    private String allyChatTagBracketRight;
    private String allyChatBracketColor;
    private String allyChatPlayerBracketLeft;
    private String allyChatPlayerBracketRight;
    private boolean clanChatEnable;
    private boolean clanChatFilter;
    private String clanChatAnnouncementColor;
    private String clanChatMessageColor;
    private String clanChatNameColor;
    private String clanChatTagBracketLeft;
    private String clanChatTagBracketRight;
    private String clanChatBracketColor;
    private String clanChatPlayerBracketLeft;
    private String clanChatPlayerBracketRight;
    private boolean clanFFOnByDefault;
    private double kwRival;
    private double kwNeutral;
    private double kwCivilian;
    private boolean useMysql;
    private String host;
    private int port;
    private String database;
    private String username;
    private String password;
    private boolean safeCivilians;
    private File main;
    private FileConfiguration config;
    private boolean homebaseSetOnce;
    private int waitSecs;
    private boolean moneyperkill;
    private double KDRMultipliesPerKillRival;
    private double KDRMultipliesPerKillCivilian;
    private double KDRMultipliesPerKillNeutral;
    private double moneyPerKillPercent;
    private boolean AutoGroupGroupName;
    private boolean tamableMobsSharing;
    private boolean allowReGroupCommand;
    private boolean useThreads;
    private boolean useBungeeCord;
    private boolean forceCommandPriority;
    private int maxAsksPerRequest;
    private int maxMembers;
    private int killCooldown;
    private boolean cooldownClanWide;
    private boolean blockIPCamping;

    /**
     *
     */
    public SettingsManager()
    {
        plugin = SimpleClans.getInstance();
        config = plugin.getConfig();
        main = new File(plugin.getDataFolder() + File.separator + "config.yml");
        load();
    }

    /**
     * Load the configuration
     */
    public void load()
    {
        boolean exists = (main).exists();

        if (exists)
        {
            try
            {
                getConfig().options().copyDefaults(true);
                getConfig().load(main);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            getConfig().options().copyDefaults(true);
        }

        onlineMode = getConfig().getBoolean("settings.online-mode");
        teleportOnSpawn = getConfig().getBoolean("settings.teleport-home-on-spawn");
        dropOnHome = getConfig().getBoolean("settings.drop-items-on-clan-home");
        keepOnHome = getConfig().getBoolean("settings.keep-items-on-clan-home");
        itemsList = getConfig().getIntegerList("settings.item-list");
        debugging = getConfig().getBoolean("settings.show-debug-info");
        useColorCodeFromPrefix = getConfig().getBoolean("settings.use-colorcode-from-prefix-for-name");
        disallowedColors = getConfig().getStringList("settings.disallowed-tag-colors");
        blacklistedWorlds = getConfig().getStringList("settings.blacklisted-worlds");
        disallowedWords = getConfig().getStringList("settings.disallowed-tags");
        unRivableClans = getConfig().getStringList("settings.unrivable-clans");
        showUnverifiedOnList = getConfig().getBoolean("settings.show-unverified-on-list");
        requireVerification = getConfig().getBoolean("settings.new-clan-verification-required");
        serverName = getConfig().getString("settings.server-name");
        chatTags = getConfig().getBoolean("settings.display-chat-tags");
        rivalLimitPercent = getConfig().getInt("settings.rival-limit-percent");
        killCooldown = getConfig().getInt("settings.killcamping.cooldown");
        cooldownClanWide = getConfig().getBoolean("settings.killcamping.clanwide");
        blockIPCamping = getConfig().getBoolean("settings.killcamping.blockSameIP");
        serverAccount = getConfig().getString("economy.server-account");
        ePurchaseCreation = getConfig().getBoolean("economy.purchase-clan-create");
        ePurchaseVerification = getConfig().getBoolean("economy.purchase-clan-verify");
        ePurchaseInvite = getConfig().getBoolean("economy.purchase-clan-invite");
        ePurchaseHomeTeleport = getConfig().getBoolean("economy.purchase-home-teleport");
        ePurchaseHomeTeleportSet = getConfig().getBoolean("economy.purchase-home-teleport-set");
        eCreationPrice = getConfig().getDouble("economy.creation-price");
        eVerificationPrice = getConfig().getDouble("economy.verification-price");
        eInvitePrice = getConfig().getDouble("economy.invite-price");
        eHomeTeleportPrice = getConfig().getDouble("economy.home-teleport-price");
        eHomeTeleportPriceSet = getConfig().getDouble("economy.home-teleport-set-price");
        purgeClan = getConfig().getInt("purge.inactive-clan-days");
        purgeUnverified = getConfig().getInt("purge.unverified-clan-days");
        purgePlayers = getConfig().getInt("purge.inactive-player-data-days");
        requestFreqencySecs = getConfig().getInt("request.ask-frequency-secs");
        requestMessageColor = getConfig().getString("request.message-color");
        setMaxAsksPerRequest(getConfig().getInt("request.max-asks-per-request"));
        pageSize = getConfig().getInt("page.size");
        pageSep = getConfig().getString("page.separator");
        pageSubTitleColor = getConfig().getString("page.subtitle-color");
        pageHeadingsColor = getConfig().getString("page.headings-color");
        pageLeaderColor = getConfig().getString("page.leader-color");
        pageTrustedColor = getConfig().getString("page.trusted-color");
        pageUnTrustedColor = getConfig().getString("page.untrusted-color");
        pageClanNameColor = getConfig().getString("page.clan-name-color");
        bbShowOnLogin = getConfig().getBoolean("bb.show-on-login");
        bbSize = getConfig().getInt("bb.size");
        bbColor = getConfig().getString("bb.color");
        bbAccentColor = getConfig().getString("bb.accent-color");
        commandClan = getConfig().getString("commands.clan");
        commandAlly = getConfig().getString("commands.ally");
        commandGlobal = getConfig().getString("commands.global");
        commandMore = getConfig().getString("commands.more");
        commandDeny = getConfig().getString("commands.deny");
        commandAccept = getConfig().getString("commands.accept");
        forceCommandPriority = getConfig().getBoolean("commands.force-priority");
        homebaseSetOnce = getConfig().getBoolean("clan.homebase-can-be-set-only-once");
        waitSecs = getConfig().getInt("clan.homebase-teleport-wait-secs");
        confirmationForPromote = getConfig().getBoolean("clan.confirmation-for-demote");
        confirmationForDemote = getConfig().getBoolean("clan.confirmation-for-promote");
        clanTrustByDefault = getConfig().getBoolean("clan.trust-members-by-default");
        clanMinSizeToAlly = getConfig().getInt("clan.min-size-to-set-ally");
        clanMinSizeToRival = getConfig().getInt("clan.min-size-to-set-rival");
        clanMinLength = getConfig().getInt("clan.min-length");
        clanMaxLength = getConfig().getInt("clan.max-length");
        clanFFOnByDefault = getConfig().getBoolean("clan.ff-on-by-default");
        tagMinLength = getConfig().getInt("tag.min-length");
        tagMaxLength = getConfig().getInt("tag.max-length");
        tagDefaultColor = getConfig().getString("tag.default-color");
        tagSeparator = getConfig().getString("tag.separator.char");
        tagSeparatorColor = getConfig().getString("tag.separator.color");
        tagSeparatorLeaderColor = getConfig().getString("tag.separator.leader-color");
        tagBracketColor = getConfig().getString("tag.bracket.color");
        tagBracketLeaderColor = getConfig().getString("tag.bracket.leader-color");
        tagBracketLeft = getConfig().getString("tag.bracket.left");
        tagBracketRight = getConfig().getString("tag.bracket.right");
        allyChatEnable = getConfig().getBoolean("allychat.enable");
        allyChatFilter = getConfig().getBoolean("allychat.filter");
        allyChatMessageColor = getConfig().getString("allychat.message-color");
        allyChatTagColor = getConfig().getString("allychat.tag-color");
        allyChatNameColor = getConfig().getString("allychat.name-color");
        allyChatBracketColor = getConfig().getString("allychat.tag-bracket.color");
        allyChatTagBracketLeft = getConfig().getString("allychat.tag-bracket.left");
        allyChatTagBracketRight = getConfig().getString("allychat.tag-bracket.right");
        allyChatPlayerBracketLeft = getConfig().getString("allychat.player-bracket.left");
        allyChatPlayerBracketRight = getConfig().getString("allychat.player-bracket.right");
        clanChatEnable = getConfig().getBoolean("clanchat.enable");
        clanChatFilter = getConfig().getBoolean("clanchat.filter");
        tagBasedClanChat = getConfig().getBoolean("clanchat.tag-based-clan-chat");
        clanChatAnnouncementColor = getConfig().getString("clanchat.announcement-color");
        clanChatMessageColor = getConfig().getString("clanchat.message-color");
        clanChatNameColor = getConfig().getString("clanchat.name-color");
        clanChatRankColor = getConfig().getString("clanchat.rank.color");
        clanChatBracketColor = getConfig().getString("clanchat.tag-bracket.color");
        clanChatTagBracketLeft = getConfig().getString("clanchat.tag-bracket.left");
        clanChatTagBracketRight = getConfig().getString("clanchat.tag-bracket.right");
        clanChatPlayerBracketLeft = getConfig().getString("clanchat.player-bracket.left");
        clanChatPlayerBracketRight = getConfig().getString("clanchat.player-bracket.right");
        kwRival = getConfig().getDouble("kill-weights.rival");
        kwNeutral = getConfig().getDouble("kill-weights.neutral");
        kwCivilian = getConfig().getDouble("kill-weights.civilian");
        useMysql = getConfig().getBoolean("mysql.enable");
        host = getConfig().getString("mysql.host");
        port = getConfig().getInt("mysql.port");
        database = getConfig().getString("mysql.database");
        username = getConfig().getString("mysql.username");
        password = getConfig().getString("mysql.password");
        port = getConfig().getInt("mysql.port");
        safeCivilians = getConfig().getBoolean("safe-civilians");
        moneyperkill = getConfig().getBoolean("economy.money-per-kill");
        KDRMultipliesPerKillRival = getConfig().getDouble("economy.money-per-kill-victim-kdr-multiplier.rival");
        KDRMultipliesPerKillCivilian = getConfig().getDouble("economy.money-per-kill-victim-kdr-multiplier.civilian");
        KDRMultipliesPerKillNeutral = getConfig().getDouble("economy.money-per-kill-victim-kdr-multiplier.neutral");
        moneyPerKillPercent = getConfig().getDouble("economy.money-per-kill-percent");
        AutoGroupGroupName = getConfig().getBoolean("permissions.auto-group-groupname");
        tamableMobsSharing = getConfig().getBoolean("settings.tameable-mobs-sharing");
        allowReGroupCommand = getConfig().getBoolean("settings.allow-regroup-command");
        useThreads = getConfig().getBoolean("performance.use-threads");
        useBungeeCord = getConfig().getBoolean("performance.use-bungeecord");
        maxMembers = getConfig().getInt("clan.max-members");

        // migrate from old way of adding ports
        if (database.contains(":")) {
            String[] strings = database.split(":");
            database = strings[0];
            port = Integer.valueOf(strings[1]);
        }

        save();
    }

    public void save()
    {
        try
        {
            getConfig().save(main);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Check whether an item is in the list
     *
     * @param typeId the type
     * @return whether the world is blacklisted
     */
    public boolean isItemInList(int typeId)
    {
        return itemsList.contains(typeId);
    }


    /**
     * Check whether a worlds is blacklisted
     *
     * @param world the world
     * @return whether the world is blacklisted
     */
    public boolean isBlacklistedWorld(String world)
    {
        for (Object w : blacklistedWorlds)
        {
            if (((String) w).equalsIgnoreCase(world))
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Check whether a word is disallowed
     *
     * @param word the world
     * @return whether its a disallowed word
     */
    public boolean isDisallowedWord(String word)
    {
        for (Object w : disallowedWords)
        {
            if (((String) w).equalsIgnoreCase(word))
            {
                return true;
            }
        }

        return word.equalsIgnoreCase("clan") || word.equalsIgnoreCase(commandMore) || word.equalsIgnoreCase(commandDeny) || word.equalsIgnoreCase(commandAccept);

    }

    /**
     * Check whether a string has a disallowed color
     *
     * @param str the string
     * @return whether the string contains the color code
     */
    public boolean hasDisallowedColor(String str)
    {
        for (Object c : getDisallowedColors())
        {
            if (str.contains("&" + c))
            {
                return true;
            }
        }

        return false;
    }

    /**
     * @return a comma delimited string with all disallowed colors
     */
    public String getDisallowedColorString()
    {
        String out = "";

        for (Object c : getDisallowedColors())
        {
            out += c + ", ";
        }

        return Helper.stripTrailing(out, ", ");
    }

    /**
     * Check whether a clan is un-rivable
     *
     * @param tag the tag
     * @return whether the clan is unrivable
     */
    public boolean isUnrivable(String tag)
    {
        for (Object t : getunRivableClans())
        {
            if (((String) t).equalsIgnoreCase(tag))
            {
                return true;
            }
        }

        return false;
    }

    /**
     * @return the plugin
     */
    public SimpleClans getPlugin()
    {
        return plugin;
    }

    /**
     * @return the requireVerification
     */
    public boolean isRequireVerification()
    {
        return requireVerification;
    }

    /**
     * @return the disallowedColors
     */
    public List<String> getDisallowedColors()
    {
        return Collections.unmodifiableList(disallowedColors);
    }

    /**
     * @return the unRivableClans
     */
    public List<String> getunRivableClans()
    {
        return Collections.unmodifiableList(unRivableClans);
    }

    /**
     * @return the rivalLimitPercent
     */
    public int getRivalLimitPercent()
    {
        return rivalLimitPercent;
    }
    
    /**
     * @return the killCooldown
     */
    public int getKillCooldown() 
    {
    	return killCooldown;
    }
    
    /**
     * @return cooldownClanWide
     */
    public boolean getCooldownClanWide() 
    {
    	return cooldownClanWide;
    }

    /**
     * @return blockIPCamping
     */
    public boolean isBlockIPCamping()
    {
        return blockIPCamping;
    }

    /**
     * @return the serverName
     */
    public String getServerName()
    {
        return Helper.parseColors(serverName);
    }

    /**
     * @return the chatTags
     */
    public boolean isChatTags()
    {
        return chatTags;
    }

    /**
     * @return the purgeClan
     */
    public int getPurgeClan()
    {
        return purgeClan;
    }

    /**
     * @return the purgeUnverified
     */
    public int getPurgeUnverified()
    {
        return purgeUnverified;
    }

    /**
     * @return the purgePlayers
     */
    public int getPurgePlayers()
    {
        return purgePlayers;
    }

    /**
     * @return the requestFreqencySecs
     */
    public int getRequestFreqencySecs()
    {
        return requestFreqencySecs;
    }

    /**
     * @return the requestMessageColor
     */
    public String getRequestMessageColor()
    {
        return Helper.toColor(requestMessageColor);
    }

    /**
     * @return the pageSize
     */
    public int getPageSize()
    {
        return pageSize;
    }

    /**
     * @return the pageSep
     */
    public String getPageSep()
    {
        return pageSep;
    }

    /**
     * @return the pageHeadingsColor
     */
    public String getPageHeadingsColor()
    {
        return Helper.toColor(pageHeadingsColor);
    }

    /**
     * @return the pageSubTitleColor
     */
    public String getPageSubTitleColor()
    {
        return Helper.toColor(pageSubTitleColor);
    }

    /**
     * @return the pageLeaderColor
     */
    public String getPageLeaderColor()
    {
        return Helper.toColor(pageLeaderColor);
    }

    /**
     * @return the bbSize
     */
    public int getBbSize()
    {
        return bbSize;
    }

    /**
     * @return the bbColor
     */
    public String getBbColor()
    {
        return Helper.toColor(bbColor);
    }

    /**
     * @return the bbAccentColor
     */
    public String getBbAccentColor()
    {
        return Helper.toColor(bbAccentColor);
    }

    /**
     * @return the commandClan
     */
    public String getCommandClan()
    {
        return commandClan;
    }

    /**
     * @return the commandMore
     */
    public String getCommandMore()
    {
        return commandMore;
    }

    /**
     * @return the commandDeny
     */
    public String getCommandDeny()
    {
        return commandDeny;
    }

    /**
     * @return the commandAccept
     */
    public String getCommandAccept()
    {
        return commandAccept;
    }

    /**
     * @return the clanMinSizeToAlly
     */
    public int getClanMinSizeToAlly()
    {
        return clanMinSizeToAlly;
    }

    /**
     * @return the clanMinSizeToRival
     */
    public int getClanMinSizeToRival()
    {
        return clanMinSizeToRival;
    }

    /**
     * @return the clanMinLength
     */
    public int getClanMinLength()
    {
        return clanMinLength;
    }

    /**
     * @return the clanMaxLength
     */
    public int getClanMaxLength()
    {
        return clanMaxLength;
    }

    /**
     * @return the pageClanNameColor
     */
    public String getPageClanNameColor()
    {
        return Helper.toColor(pageClanNameColor);
    }

    /**
     * @return the tagMinLength
     */
    public int getTagMinLength()
    {
        return tagMinLength;
    }

    /**
     * @return the tagMaxLength
     */
    public int getTagMaxLength()
    {
        return tagMaxLength;
    }

    /**
     * @return the tagDefaultColor
     */
    public String getTagDefaultColor()
    {
        return Helper.toColor(tagDefaultColor);
    }

    /**
     * @return the tagSeparator
     */
    public String getTagSeparator()
    {
        if (tagSeparator.equals(" ."))
        {
            return ".";
        }

        if (tagSeparator == null)
        {
            return "";
        }

        return tagSeparator;
    }

    /**
     * @return the tagSeparatorColor
     */
    public String getTagSeparatorColor()
    {
        return Helper.toColor(tagSeparatorColor);
    }

    /**
     * @return the clanChatAnnouncementColor
     */
    public String getClanChatAnnouncementColor()
    {
        return Helper.toColor(clanChatAnnouncementColor);
    }

    /**
     * @return the clanChatMessageColor
     */
    public String getClanChatMessageColor()
    {
        return Helper.toColor(clanChatMessageColor);
    }

    /**
     * @return the clanChatNameColor
     */
    public String getClanChatNameColor()
    {
        return Helper.toColor(clanChatNameColor);
    }

    /**
     * @return the clanChatTagBracketLeft
     */
    public String getClanChatTagBracketLeft()
    {
        return clanChatTagBracketLeft;
    }

    /**
     * @return the clanChatTagBracketRight
     */
    public String getClanChatTagBracketRight()
    {
        return clanChatTagBracketRight;
    }

    /**
     * @return the clanChatBracketColor
     */
    public String getClanChatBracketColor()
    {
        return Helper.toColor(clanChatBracketColor);
    }

    /**
     * @return the clanChatPlayerBracketLeft
     */
    public String getClanChatPlayerBracketLeft()
    {
        return clanChatPlayerBracketLeft;
    }

    /**
     * @return the clanChatPlayerBracketRight
     */
    public String getClanChatPlayerBracketRight()
    {
        return clanChatPlayerBracketRight;
    }

    /**
     * @return the kwRival
     */
    public double getKwRival()
    {
        return kwRival;
    }

    /**
     * @return the kwNeutral
     */
    public double getKwNeutral()
    {
        return kwNeutral;
    }

    /**
     * @return the kwCivilian
     */
    public double getKwCivilian()
    {
        return kwCivilian;
    }

    /**
     * @return the useMysql
     */
    public boolean isUseMysql()
    {
        return useMysql;
    }

    /**
     * @return the host
     */
    public String getHost()
    {
        return host;
    }
    
    /**
     * @return the port
     */
    public int getPort()
    {
        return port;
    }

    /**
     * @return the database
     */
    public String getDatabase()
    {
        return database;
    }

    /**
     * @return the username
     */
    public String getUsername()
    {
        return username;
    }

    /**
     * @return the password
     */
    public String getPassword()
    {
        return password;
    }

    /**
     * @return the showUnverifiedOnList
     */
    public boolean isShowUnverifiedOnList()
    {
        return showUnverifiedOnList;
    }

    /**
     * @return the clanTrustByDefault
     */
    public boolean isClanTrustByDefault()
    {
        return clanTrustByDefault;
    }

    /**
     * @return the pageTrustedColor
     */
    public String getPageTrustedColor()
    {
        return Helper.toColor(pageTrustedColor);
    }

    /**
     * @return the pageUnTrustedColor
     */
    public String getPageUnTrustedColor()
    {
        return Helper.toColor(pageUnTrustedColor);
    }

    /**
     * @return the globalff
     */
    public boolean isGlobalff()
    {
        return globalff;
    }

    /**
     * @param globalff the globalff to set
     */
    public void setGlobalff(boolean globalff)
    {
        this.globalff = globalff;
    }

    /**
     * @return the clanChatEnable
     */
    public boolean getClanChatEnable()
    {
        return clanChatEnable;
    }

    /**
     * @return the clanChatFilter
     */
    public boolean getClanChatFilter()
    {
        return clanChatFilter;
    }

    /**
     * @return the tagBracketLeft
     */
    public String getTagBracketLeft()
    {
        return tagBracketLeft;
    }

    /**
     * @return the tagBracketRight
     */
    public String getTagBracketRight()
    {
        return tagBracketRight;
    }

    /**
     * @return the tagBracketColor
     */
    public String getTagBracketColor()
    {
        return Helper.toColor(tagBracketColor);
    }

    public boolean isServerAccount() {
        if (serverAccount == null || serverAccount == "") {
            return false;
        }
        return true;
    }

    public String getServerAccount() {
        return serverAccount;
    }

    /**
     * @return the ePurchaseCreation
     */
    public boolean isePurchaseCreation()
    {
        return ePurchaseCreation;
    }

    /**
     * @return the ePurchaseVerification
     */
    public boolean isePurchaseVerification()
    {
        return ePurchaseVerification;
    }

    /**
     * @return the ePurchaseInvite
     */
    public boolean isePurchaseInvite()
    {
        return ePurchaseInvite;
    }

    /**
     * @return the eCreationPrice
     */
    public double getCreationPrice()
    {
        return eCreationPrice;
    }

    /**
     * @return the eVerificationPrice
     */
    public double getVerificationPrice()
    {
        return eVerificationPrice;
    }

    /**
     * @return the eInvitePrice
     */
    public double getInvitePrice()
    {
        return eInvitePrice;
    }

    public boolean isBbShowOnLogin()
    {
        return bbShowOnLogin;
    }

    public boolean getSafeCivilians()
    {
        return safeCivilians;
    }

    public boolean isConfirmationForPromote()
    {
        return confirmationForPromote;
    }

    public boolean isConfirmationForDemote()
    {
        return confirmationForDemote;
    }

    public boolean isUseColorCodeFromPrefix()
    {
        return useColorCodeFromPrefix;
    }

    public String getCommandAlly()
    {
        return commandAlly;
    }

    public boolean isAllyChatEnable()
    {
        return allyChatEnable;
    }

    public boolean isAllyChatFilter()
    {
        return allyChatFilter;
    }

    public String getAllyChatMessageColor()
    {
        return Helper.toColor(allyChatMessageColor);
    }

    public String getAllyChatNameColor()
    {
        return Helper.toColor(allyChatNameColor);
    }

    public String getAllyChatTagBracketLeft()
    {
        return allyChatTagBracketLeft;
    }

    public String getAllyChatTagBracketRight()
    {
        return allyChatTagBracketRight;
    }

    public String getAllyChatBracketColor()
    {
        return Helper.toColor(allyChatBracketColor);
    }

    public String getAllyChatPlayerBracketLeft()
    {
        return allyChatPlayerBracketLeft;
    }

    public String getAllyChatPlayerBracketRight()
    {
        return allyChatPlayerBracketRight;
    }

    public String getCommandGlobal()
    {
        return commandGlobal;
    }

    public String getAllyChatTagColor()
    {
        return Helper.toColor(allyChatTagColor);
    }

    public boolean isClanFFOnByDefault()
    {
        return clanFFOnByDefault;
    }

    public boolean isHomebaseSetOnce()
    {
        return homebaseSetOnce;
    }

    public int getWaitSecs()
    {
        return waitSecs;
    }

    public boolean isDebugging()
    {
        return debugging;
    }

    public boolean isKeepOnHome()
    {
        return keepOnHome;
    }

    public boolean isDropOnHome()
    {
        return dropOnHome;
    }

    public List<Integer> getItemsList()
    {
        return Collections.unmodifiableList(itemsList);
    }

    public boolean isTeleportOnSpawn()
    {
        return teleportOnSpawn;
    }

    public boolean isTagBasedClanChat()
    {
        return tagBasedClanChat;
    }

    public String getClanChatRankColor()
    {
        return Helper.toColor(clanChatRankColor);
    }

    /**
     * @return the ePurchaseHomeTeleport
     */
    public boolean isePurchaseHomeTeleport() {
        return ePurchaseHomeTeleport;
    }

    /**
     * @return the HomeTeleportPrice
     */
    public double getHomeTeleportPrice() {
        return eHomeTeleportPrice;
    }

    /**
     * @return the ePurchaseHomeTeleportSet
     */
    public boolean isePurchaseHomeTeleportSet() {
        return ePurchaseHomeTeleportSet;
    }

    /**
     * @return the HomeTeleportPriceSet
     */
    public double getHomeTeleportPriceSet() {
        return eHomeTeleportPriceSet;
    }

    /**
     * @return the config
     */
    public FileConfiguration getConfig() {
        return config;
    }

    /**
     * @return the moneyperkill
     */
    public boolean isMoneyPerKill() {
        return moneyperkill;
    }

    /**
     * @return the KDRMultipliesPerKillRival
     */
    public double getKDRMultipliesPerKillRival() {
        return KDRMultipliesPerKillRival;
    }

    /**
     * @return the KDRMultipliesPerKillCivlian
     */
    public double getKDRMultipliesPerKillCivilian() {
        return KDRMultipliesPerKillCivilian;
    }

    /**
     * @return the KDRMultipliesPerKillNeutral
     */
    public double getKDRMultipliesPerKillNeutral() {
        return KDRMultipliesPerKillNeutral;
    }

    /**
     * @return the moneyPerKillPercent
     */
    public double getMoneyPerKillPercent() {
        return moneyPerKillPercent;
    }

    /**
     * @return the AutoGroupGroupName
     */
    public boolean isAutoGroupGroupName() {
        return AutoGroupGroupName;
    }

    /**
     * @return the tamableMobsSharing
     */
    public boolean isTamableMobsSharing() {
        return tamableMobsSharing;
    }

    public boolean isOnlineMode()
    {
        return onlineMode;
    }

    /**
     * @return the allowReGroupCommand
     */
    public boolean getAllowReGroupCommand()
    {
        return allowReGroupCommand;
    }

    /**
     * @return the useThreads
     */
    public boolean getUseThreads()
    {
        return useThreads;
    }

    /**
     * @return the useBungeeCord
     */
    public boolean getUseBungeeCord()
    {
        return useBungeeCord;
    }

    public String getTagSeparatorLeaderColor()
    {
        return Helper.toColor(tagSeparatorLeaderColor);
    }

    public String getTagBracketLeaderColor()
    {
        return Helper.toColor(tagBracketLeaderColor);
    }

    public int getMaxAsksPerRequest()
    {
        return maxAsksPerRequest;
    }

    public void setMaxAsksPerRequest(int maxAsksPerRequest)
    {
        this.maxAsksPerRequest = maxAsksPerRequest;
    }

    public boolean isForceCommandPriority()
    {
        return forceCommandPriority;
    }

    public int getMaxMembers()
    {
        return this.maxMembers;
    }
}
