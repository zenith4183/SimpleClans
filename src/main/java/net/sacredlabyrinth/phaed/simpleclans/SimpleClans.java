package net.sacredlabyrinth.phaed.simpleclans;

import net.sacredlabyrinth.phaed.simpleclans.executors.*;
import net.sacredlabyrinth.phaed.simpleclans.listeners.SCEntityListener;
import net.sacredlabyrinth.phaed.simpleclans.listeners.SCPlayerListener;
import net.sacredlabyrinth.phaed.simpleclans.managers.*;
import net.sacredlabyrinth.phaed.simpleclans.uuid.UUIDMigration;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Phaed
 */
public class SimpleClans extends JavaPlugin {

    private ArrayList<String> messages = new ArrayList<String>();
    private static SimpleClans instance;
    private static final Logger logger = Logger.getLogger("Minecraft");
    private ClanManager clanManager;
    private RequestManager requestManager;
    private StorageManager storageManager;
    private SettingsManager settingsManager;
    private PermissionsManager permissionsManager;
    private TeleportManager teleportManager;
    private LanguageManager languageManager;
    private boolean hasUUID;

    /**
     * @return the logger
     */
    public static Logger getLog() {
        return logger;
    }

    /**
     * @param msg
     */
    public static void debug(String msg)
    {
        if (getInstance().getSettingsManager().isDebugging()) {
            logger.log(Level.INFO, msg);
        }
    }

    /**
     * @return the instance
     */
    public static SimpleClans getInstance()
    {
        return instance;
    }

    public static void log(String msg, Object... arg)
    {
        if (arg == null || arg.length == 0) {
            logger.log(Level.INFO, msg);
        } else {
            logger.log(Level.INFO, MessageFormat.format(msg, arg));
        }
    }

    @Override
    public void onEnable()
    {
        instance = this;
        this.hasUUID = UUIDMigration.canReturnUUID();
        languageManager = new LanguageManager();
        settingsManager = new SettingsManager();

        permissionsManager = new PermissionsManager();
        requestManager = new RequestManager();
        clanManager = new ClanManager();
        storageManager = new StorageManager();
        teleportManager = new TeleportManager();

        logger.info(MessageFormat.format(getLang("version.loaded"), getDescription().getName(), getDescription().getVersion()));

        getServer().getPluginManager().registerEvents(new SCEntityListener(), this);
        getServer().getPluginManager().registerEvents(new SCPlayerListener(), this);

        permissionsManager.loadPermissions();

        CommandHelper.registerCommand(getSettingsManager().getCommandClan());
        CommandHelper.registerCommand(getSettingsManager().getCommandAccept());
        CommandHelper.registerCommand(getSettingsManager().getCommandDeny());
        CommandHelper.registerCommand(getSettingsManager().getCommandMore());
        CommandHelper.registerCommand(getSettingsManager().getCommandAlly());
        CommandHelper.registerCommand(getSettingsManager().getCommandGlobal());

        getCommand(getSettingsManager().getCommandClan()).setExecutor(new ClanCommandExecutor());
        getCommand(getSettingsManager().getCommandAccept()).setExecutor(new AcceptCommandExecutor());
        getCommand(getSettingsManager().getCommandDeny()).setExecutor(new DenyCommandExecutor());
        getCommand(getSettingsManager().getCommandMore()).setExecutor(new MoreCommandExecutor());
        getCommand(getSettingsManager().getCommandAlly()).setExecutor(new AllyCommandExecutor());
        getCommand(getSettingsManager().getCommandGlobal()).setExecutor(new GlobalCommandExecutor());

        getCommand(getSettingsManager().getCommandClan()).setTabCompleter(new PlayerNameTabCompleter());

        pullMessages();
        logger.info("[SimpleClans] Modo Multithreading: " + SimpleClans.getInstance().getSettingsManager().getUseThreads());
        logger.info("[SimpleClans] Modo BungeeCord: " + SimpleClans.getInstance().getSettingsManager().getUseBungeeCord());
    }

    @Override
    public void onDisable()
    {
        getServer().getScheduler().cancelTasks(this);
        getStorageManager().closeConnection();
        getPermissionsManager().savePermissions();
    }

    public void pullMessages()
    {
        if (getSettingsManager().isDisableMessages())
        {
            return;
        }

        try
        {
            BufferedReader in = new BufferedReader(new InputStreamReader(new URL("https://minecraftcubed.net/pluginmessage/").openStream()));

            String message;
            while ((message = in.readLine()) != null)
            {
                messages.add(message);
                getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + message);
            }
            in.close();

        }
        catch (IOException e)
        {
            // do nothing
        }
    }

    /**
     * @return the clanManager
     */
    public ClanManager getClanManager()
    {
        return clanManager;
    }

    /**
     * @return the requestManager
     */
    public RequestManager getRequestManager()
    {
        return requestManager;
    }

    /**
     * @return the storageManager
     */
    public StorageManager getStorageManager()
    {
        return storageManager;
    }

    /**
     * @return the settingsManager
     */
    public SettingsManager getSettingsManager()
    {
        return settingsManager;
    }

    /**
     * @return the permissionsManager
     */
    public PermissionsManager getPermissionsManager()
    {
        return permissionsManager;
    }

    /**
     * @return the lang
     */
    public String getLang(String msg) {
        return languageManager.get(msg);
    }

    public TeleportManager getTeleportManager() {
        return teleportManager;
    }

    public ArrayList<String> getMessages()
    {
        return messages;
    }

    /**
     * @return the hasUUID
     */
    public boolean hasUUID()
    {
        return this.hasUUID;
    }

    /**
     * @param trueOrFalse
     */
    public void setUUID(boolean trueOrFalse)
    {
        this.hasUUID = trueOrFalse;
    }

    public LanguageManager getLanguageManager()
    {
        return languageManager;
    }
}
