package org.bitbucket.zenith4183.zclans.commands;

import org.bitbucket.zenith4183.zclans.ChatBlock;
import org.bitbucket.zenith4183.zclans.ClanPlayer;
import org.bitbucket.zenith4183.zclans.Helper;
import org.bitbucket.zenith4183.zclans.zClans;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.List;

/**
 * @author phaed
 */
public class LeaderboardCommand
{
    public LeaderboardCommand()
    {
    }

    /**
     * Execute the command
     *
     * @param player    player executing command
     * @param arg       command arguments
     */
    public void execute(Player player, String[] arg)
    {
        zClans plugin = zClans.getInstance();
        String headColor = plugin.getSettingsManager().getPageHeadingsColor();
        String subColor = plugin.getSettingsManager().getPageSubTitleColor();
        NumberFormat formatter = new DecimalFormat("#.#");

        if (arg.length == 0)
        {
            if (plugin.getPermissionsManager().has(player, "zclans.anyone.leaderboard"))
            {
                List<ClanPlayer> clanPlayers = plugin.getClanManager().getAllClanPlayers();
                plugin.getClanManager().sortClanPlayersByKDR(clanPlayers);

                ChatBlock chatBlock = new ChatBlock();

                ChatBlock.sendBlank(player);
                ChatBlock.saySingle(player, plugin.getSettingsManager().getServerName() + subColor + " " + plugin.getLang("leaderboard.command") + " " + headColor + Helper.generatePageSeparator(plugin.getSettingsManager().getPageSep()));
                ChatBlock.sendBlank(player);
                ChatBlock.sendMessage(player, headColor + MessageFormat.format(plugin.getLang("total.clan.players.0"), subColor + clanPlayers.size()));
                ChatBlock.sendBlank(player);

                chatBlock.setAlignment("c", "l", "c", "c", "c", "c");
                chatBlock.addRow("  " + headColor + plugin.getLang("rank"), plugin.getLang("player"), plugin.getLang("kdr"), plugin.getLang("clan"), plugin.getLang("seen"));

                int rank = 1;

                for (ClanPlayer cp : clanPlayers)
                {
                    Player p = cp.toPlayer();

                    boolean isOnline = false;

                    if (p != null)
                    {
                        isOnline = true;
                    }


                    String name = (cp.isLeader() ? plugin.getSettingsManager().getPageLeaderColor() : (cp.isTrusted() ? plugin.getSettingsManager().getPageTrustedColor() : plugin.getSettingsManager().getPageUnTrustedColor())) + cp.getName();
                    String lastSeen = isOnline ? ChatColor.GREEN + plugin.getLang("online") : ChatColor.WHITE + cp.getLastSeenDaysString();

                    String clanTag = ChatColor.WHITE + plugin.getLang("free.agent");

                    if (cp.getClan() != null)
                    {
                        clanTag = cp.getClan().getColorTag();
                    }

                    chatBlock.addRow("  " + rank, name, ChatColor.YELLOW + "" + formatter.format(cp.getKDR()), ChatColor.WHITE + clanTag, lastSeen);
                    rank++;
                }

                boolean more = chatBlock.sendBlock(player, plugin.getSettingsManager().getPageSize());

                if (more)
                {
                    plugin.getStorageManager().addChatBlock(player, chatBlock);
                    ChatBlock.sendBlank(player);
                    ChatBlock.sendMessage(player, headColor + MessageFormat.format(plugin.getLang("view.next.page"), plugin.getSettingsManager().getCommandMore()));
                }

                ChatBlock.sendBlank(player);
            }
            else
            {
                ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("insufficient.permissions"));
            }
        }
        else
        {
            ChatBlock.sendMessage(player, ChatColor.RED + MessageFormat.format(plugin.getLang("usage.0.leaderboard"), plugin.getSettingsManager().getCommandClan()));
        }
    }
}
