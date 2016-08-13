package org.bitbucket.zenith4183.zclans.commands;

import org.bitbucket.zenith4183.zclans.*;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;

/**
 * @author phaed
 */
public class MenuCommand
{
    private List<String> menuItems = new LinkedList<>();

    public MenuCommand()
    {
    }

    /**
     * Execute the command
     *
     * @param player    player executing command
     */
    public void execute(Player player)
    {
        zClans plugin = zClans.getInstance();

        String headColor = plugin.getSettingsManager().getPageHeadingsColor();
        String subColor = plugin.getSettingsManager().getPageSubTitleColor();

        ClanPlayer cp = plugin.getClanManager().getClanPlayer(player);
        Clan clan = cp == null ? null : cp.getClan();

        boolean isLeader = cp != null && cp.isLeader();
        boolean isTrusted = cp != null && cp.isTrusted();
        boolean isVerified = clan != null && clan.isVerified();
        boolean isNonVerified = clan != null && !clan.isVerified();

        String clanCommand = plugin.getSettingsManager().getCommandClan();

        ChatBlock chatBlock = new ChatBlock();

        if (clan == null && plugin.getPermissionsManager().has(player, "zclans.leader.create"))
        {
            if (plugin.getSettingsManager().isePurchaseCreation())
            {
                chatBlock.addRow(ChatColor.AQUA + "  " + MessageFormat.format(plugin.getLang("0.create.tag.name.1.purchase.a.new.clan"), clanCommand, ChatColor.WHITE));
            }
            else
            {
                chatBlock.addRow(ChatColor.AQUA + "  " + MessageFormat.format(plugin.getLang("0.create.tag.name.1.create.a.new.clan"), clanCommand, ChatColor.WHITE));
            }
        }
        if (isNonVerified && plugin.getSettingsManager().isRequireVerification() && plugin.getSettingsManager().isePurchaseVerification())
        {
            chatBlock.addRow(ChatColor.DARK_RED + "  " + MessageFormat.format(plugin.getLang("0.verify.1.purchase.verification.of.your.clan"), clanCommand, ChatColor.WHITE));
        }
        if (plugin.getPermissionsManager().has(player, "zclans.anyone.list"))
        {
            chatBlock.addRow(ChatColor.AQUA + "  " + MessageFormat.format(plugin.getLang("0.list.1.lists.all.clans"), clanCommand, ChatColor.WHITE));
        }
        if (isVerified && plugin.getPermissionsManager().has(player, "zclans.member.profile"))
        {
            chatBlock.addRow(ChatColor.AQUA + "  " + MessageFormat.format(plugin.getLang("0.profile.1.view.your.clan.s.profile"), clanCommand, ChatColor.WHITE));
        }
        if (plugin.getPermissionsManager().has(player, "zclans.anyone.profile"))
        {
            chatBlock.addRow(ChatColor.AQUA + "  " + MessageFormat.format(plugin.getLang("0.profile.tag.1.view.a.clan.s.profile"), clanCommand, ChatColor.WHITE));
        }
        if (plugin.getPermissionsManager().has(player, "zclans.member.lookup"))
        {
            chatBlock.addRow(ChatColor.AQUA + "  " + MessageFormat.format(plugin.getLang("0.lookup.1.lookup.your.info"), clanCommand, ChatColor.WHITE));
        }
        if (plugin.getPermissionsManager().has(player, "zclans.anyone.lookup"))
        {
            chatBlock.addRow(ChatColor.AQUA + "  " + MessageFormat.format(plugin.getLang("0.lookup.player.1.lookup.a.player.s.info"), clanCommand, ChatColor.WHITE));
        }
        if (plugin.getPermissionsManager().has(player, "zclans.anyone.leaderboard"))
        {
            chatBlock.addRow(ChatColor.AQUA + "  " + MessageFormat.format(plugin.getLang("0.leaderboard.1.view.leaderboard"), clanCommand, ChatColor.WHITE));
        }
        if (plugin.getPermissionsManager().has(player, "zclans.anyone.alliances"))
        {
            chatBlock.addRow(ChatColor.AQUA + "  " + MessageFormat.format(plugin.getLang("0.alliances.1.view.all.clan.alliances"), clanCommand, ChatColor.WHITE));
        }
        if (plugin.getPermissionsManager().has(player, "zclans.anyone.rivalries"))
        {
            chatBlock.addRow(ChatColor.AQUA + "  " + MessageFormat.format(plugin.getLang("0.rivalries.1.view.all.clan.rivalries"), clanCommand, ChatColor.WHITE));
        }
        if (isVerified && plugin.getPermissionsManager().has(player, "zclans.member.roster"))
        {
            chatBlock.addRow(ChatColor.AQUA + "  " + MessageFormat.format(plugin.getLang("0.roster.1.view.your.clan.s.member.list"), clanCommand, ChatColor.WHITE));
        }
        if (plugin.getPermissionsManager().has(player, "zclans.anyone.roster"))
        {
            chatBlock.addRow(ChatColor.AQUA + "  " + MessageFormat.format(plugin.getLang("0.roster.tag.1.view.a.clan.s.member.list"), clanCommand, ChatColor.WHITE));
        }
        if (isVerified && isTrusted && plugin.getPermissionsManager().has(player, "zclans.member.vitals"))
        {
            chatBlock.addRow(ChatColor.AQUA + "  " + MessageFormat.format(plugin.getLang("0.vitals.1.view.your.clan.member.s.vitals"), clanCommand, ChatColor.WHITE));
        }
        if (isVerified && isTrusted && plugin.getPermissionsManager().has(player, "zclans.member.coords"))
        {
            chatBlock.addRow(ChatColor.AQUA + "  " + MessageFormat.format(plugin.getLang("0.coords.1.view.your.clan.member.s.coordinates"), clanCommand, ChatColor.WHITE));
        }
        if (isVerified && isTrusted && plugin.getPermissionsManager().has(player, "zclans.member.stats"))
        {
            chatBlock.addRow(ChatColor.AQUA + "  " + MessageFormat.format(plugin.getLang("0.stats.1.view.your.clan.member.s.stats"), clanCommand, ChatColor.WHITE));
        }
        if (isVerified && isTrusted && plugin.getPermissionsManager().has(player, "zclans.member.kills"))
        {
            chatBlock.addRow(ChatColor.AQUA + "  " + MessageFormat.format(plugin.getLang("0.kills"), clanCommand, ChatColor.WHITE));
        }
        if (isVerified && isTrusted && plugin.getPermissionsManager().has(player, "zclans.member.kills"))
        {
            chatBlock.addRow(ChatColor.AQUA + "  " + MessageFormat.format(plugin.getLang("0.killsplayer"), clanCommand, ChatColor.WHITE));
        }
        if (isVerified && isLeader && plugin.getPermissionsManager().has(player, "zclans.leader.ally"))
        {
            chatBlock.addRow(ChatColor.AQUA + "  " + MessageFormat.format(plugin.getLang("0.ally.add.remove.tag.1.add.remove.an.ally.clan"), clanCommand, ChatColor.WHITE));
        }
        if (isVerified && isLeader && plugin.getPermissionsManager().has(player, "zclans.leader.rival"))
        {
            chatBlock.addRow(ChatColor.AQUA + "  " + MessageFormat.format(plugin.getLang("0.rival.add.remove.tag.1.add.remove.a.rival.clan"), clanCommand, ChatColor.WHITE));
        }
        if (isVerified && plugin.getPermissionsManager().has(player, "zclans.member.home"))
        {
            chatBlock.addRow(ChatColor.AQUA + "  " + MessageFormat.format(plugin.getLang("home-menu"), clanCommand, ChatColor.WHITE));
        }
        if (isVerified && isLeader && plugin.getPermissionsManager().has(player, "zclans.leader.home-set"))
        {
            chatBlock.addRow(ChatColor.AQUA + "  " + MessageFormat.format(plugin.getLang("home-set-menu"), clanCommand, ChatColor.WHITE));
        }
        if (isVerified && isLeader && plugin.getPermissionsManager().has(player, "zclans.leader.home-set"))
        {
            chatBlock.addRow(ChatColor.AQUA + "  " + MessageFormat.format(plugin.getLang("home-clear-menu"), clanCommand, ChatColor.WHITE));
        }
        if (isVerified && plugin.getPermissionsManager().has(player, "zclans.member.bb"))
        {
            chatBlock.addRow(ChatColor.AQUA + "  " + MessageFormat.format(plugin.getLang("0.bb.1.display.bulletin.board"), clanCommand, ChatColor.WHITE));
        }
        if (isVerified && isTrusted && plugin.getPermissionsManager().has(player, "zclans.member.bb-add"))
        {
            chatBlock.addRow(ChatColor.AQUA + "  " + MessageFormat.format(plugin.getLang("0.bb.msg.1.add.a.message.to.the.bulletin.board"), clanCommand, ChatColor.WHITE));
        }
        if (isVerified && isLeader && plugin.getPermissionsManager().has(player, "zclans.leader.modtag"))
        {
            chatBlock.addRow(ChatColor.AQUA + "  " + MessageFormat.format(plugin.getLang("0.modtag.tag.1.modify.the.clan.s.tag"), clanCommand, ChatColor.WHITE));
        }

        String toggles = "";

        if (isVerified && isTrusted && plugin.getPermissionsManager().has(player, "zclans.member.bb-toggle"))
        {
            toggles += "bb/";
        }

        if (isVerified && isTrusted && plugin.getPermissionsManager().has(player, "zclans.member.tag-toggle"))
        {
            toggles += "tag/";
        }

        if (!toggles.isEmpty())
        {
            chatBlock.addRow(ChatColor.AQUA + "  " + MessageFormat.format(plugin.getLang("0.toggle.command"), clanCommand, ChatColor.WHITE, Helper.stripTrailing(toggles, "/")));
        }

        if (isLeader && plugin.getPermissionsManager().has(player, "zclans.leader.invite"))
        {
            chatBlock.addRow(ChatColor.AQUA + "  " + MessageFormat.format(plugin.getLang("0.invite.player.1.invite.a.player"), clanCommand, ChatColor.WHITE));
        }
        if (isLeader && plugin.getPermissionsManager().has(player, "zclans.leader.kick"))
        {
            chatBlock.addRow(ChatColor.AQUA + "  " + MessageFormat.format(plugin.getLang("0.kick.player.1.kick.a.player.from.the.clan"), clanCommand, ChatColor.WHITE));
        }
        if (isVerified && isLeader && plugin.getPermissionsManager().has(player, "zclans.leader.setrank"))
        {
            chatBlock.addRow(ChatColor.AQUA + "  " + MessageFormat.format(plugin.getLang("0.trust.setrank"), clanCommand, ChatColor.WHITE));
        }
        if (isVerified && isLeader && plugin.getPermissionsManager().has(player, "zclans.leader.settrust"))
        {
            chatBlock.addRow(ChatColor.AQUA + "  " + MessageFormat.format(plugin.getLang("0.trust.untrust.player.1.set.trust.level1"), clanCommand, ChatColor.WHITE));
        }
        if (isVerified && isLeader && plugin.getPermissionsManager().has(player, "zclans.leader.settrust"))
        {
            chatBlock.addRow(ChatColor.AQUA + "  " + MessageFormat.format(plugin.getLang("0.trust.untrust.player.1.set.trust.level2"), clanCommand, ChatColor.WHITE));
        }
        if (isLeader && plugin.getPermissionsManager().has(player, "zclans.leader.promote"))
        {
            chatBlock.addRow(ChatColor.AQUA + "  " + MessageFormat.format(plugin.getLang("0.promote.member.1.promote.a.member.to.leader"), clanCommand, ChatColor.WHITE));
        }
        if (isLeader && plugin.getPermissionsManager().has(player, "zclans.leader.demote"))
        {
            chatBlock.addRow(ChatColor.AQUA + "  " + MessageFormat.format(plugin.getLang("0.demote.leader.1.demote.a.leader.to.member"), clanCommand, ChatColor.WHITE));
        }
        if (isLeader && plugin.getPermissionsManager().has(player, "zclans.leader.ff"))
        {
            chatBlock.addRow(ChatColor.AQUA + "  " + MessageFormat.format(plugin.getLang("0.clanff.allow.block.1.toggle.clan.s.friendly.fire"), clanCommand, ChatColor.WHITE));
        }
        if (isLeader && plugin.getPermissionsManager().has(player, "zclans.leader.disband"))
        {
            chatBlock.addRow(ChatColor.AQUA + "  " + MessageFormat.format(plugin.getLang("0.disband.1.disband.your.clan"), clanCommand, ChatColor.WHITE));
        }
        if (plugin.getPermissionsManager().has(player, "zclans.member.ff"))
        {
            chatBlock.addRow(ChatColor.AQUA + "  " + MessageFormat.format(plugin.getLang("0.ff.allow.auto.1.toggle.personal.friendly.fire"), clanCommand, ChatColor.WHITE));
        }
        if (plugin.getPermissionsManager().has(player, "zclans.member.resign"))
        {
            chatBlock.addRow(ChatColor.AQUA + "  " + MessageFormat.format(plugin.getLang("0.resign.1.resign.from.the.clan"), clanCommand, ChatColor.WHITE));
        }

        for (String item : menuItems)
        {
            chatBlock.addRow(ChatColor.AQUA + "  " + item);
        }

        if (plugin.getPermissionsManager().has(player, "zclans.mod.verify") && plugin.getSettingsManager().isRequireVerification())
        {
            chatBlock.addRow(ChatColor.DARK_RED + "  " + MessageFormat.format(plugin.getLang("0.verify.tag.1.verify.an.unverified.clan"), clanCommand, ChatColor.WHITE));
        }
        if (plugin.getPermissionsManager().has(player, "zclans.mod.place"))
        {
            chatBlock.addRow(ChatColor.DARK_RED + "  " + MessageFormat.format(plugin.getLang("0.place"), clanCommand, ChatColor.WHITE));
        }
        if (isVerified && isTrusted && plugin.getPermissionsManager().has(player, "zclans.mod.mostkilled"))
        {
            chatBlock.addRow(ChatColor.DARK_RED + "  " + MessageFormat.format(plugin.getLang("0.mostkilled"), clanCommand, ChatColor.WHITE));
        }
        if (plugin.getPermissionsManager().has(player, "zclans.mod.disband"))
        {
            chatBlock.addRow(ChatColor.DARK_RED + "  " + MessageFormat.format(plugin.getLang("0.disband.tag.1.disband.a.clan"), clanCommand, ChatColor.WHITE));
        }
        if (plugin.getPermissionsManager().has(player, "zclans.mod.ban"))
        {
            chatBlock.addRow(ChatColor.DARK_RED + "  " + MessageFormat.format(plugin.getLang("0.ban.unban.player.1.ban.unban.a.player"), clanCommand, ChatColor.WHITE));
        }
        if (plugin.getPermissionsManager().has(player, "zclans.mod.hometp"))
        {
            chatBlock.addRow(ChatColor.DARK_RED + "  " + MessageFormat.format(plugin.getLang("0.hometp.clan.1.tp.home.a.clan"), clanCommand, ChatColor.WHITE));
        }
        if (plugin.getPermissionsManager().has(player, "zclans.mod.globalff"))
        {
            chatBlock.addRow(ChatColor.DARK_RED + "  " + MessageFormat.format(plugin.getLang("0.globalff.allow.auto.1.set.global.friendly.fire"), clanCommand, ChatColor.WHITE));
        }
        if (plugin.getPermissionsManager().has(player, "zclans.admin.reload"))
        {
            chatBlock.addRow(ChatColor.DARK_RED + "  " + MessageFormat.format(plugin.getLang("0.reload.1.reload.configuration"), clanCommand, ChatColor.WHITE));
        }
        if (chatBlock.isEmpty())
        {
            ChatBlock.sendMessage(player, ChatColor.RED + plugin.getLang("insufficient.permissions"));
            return;
        }

        ChatBlock.sendBlank(player);
        ChatBlock.saySingle(player, plugin.getSettingsManager().getServerName() + subColor + " " + plugin.getLang("clan.commands") + " " + headColor + Helper.generatePageSeparator(plugin.getSettingsManager().getPageSep()));
        ChatBlock.sendBlank(player);

        boolean more = chatBlock.sendBlock(player, plugin.getSettingsManager().getPageSize());

        if (more)
        {
            plugin.getStorageManager().addChatBlock(player, chatBlock);
            ChatBlock.sendBlank(player);
            ChatBlock.sendMessage(player, headColor + MessageFormat.format(plugin.getLang("view.next.page"), plugin.getSettingsManager().getCommandMore()));
        }

        ChatBlock.sendBlank(player);
    }

    /**
     * Execute the command
     *
     * @param sender
     */
    public void executeSender(CommandSender sender)
    {
        zClans plugin = zClans.getInstance();

        String headColor = plugin.getSettingsManager().getPageHeadingsColor();
        String subColor = plugin.getSettingsManager().getPageSubTitleColor();

        String clanCommand = plugin.getSettingsManager().getCommandClan();

        ChatBlock chatBlock = new ChatBlock();

        chatBlock.addRow(ChatColor.DARK_RED + "  " + MessageFormat.format(plugin.getLang("0.verify.tag.1.verify.an.unverified.clan"), clanCommand, ChatColor.WHITE));
        chatBlock.addRow(ChatColor.DARK_RED + "  " + MessageFormat.format(plugin.getLang("0.reload.1.reload.configuration"), clanCommand, ChatColor.WHITE));

        ChatBlock.sendBlank(sender);
        ChatBlock.saySingle(sender, plugin.getSettingsManager().getServerName() + subColor + " " + plugin.getLang("clan.commands") + " " + headColor + Helper.generatePageSeparator(plugin.getSettingsManager().getPageSep()));
        ChatBlock.sendBlank(sender);
        chatBlock.sendBlock(sender, plugin.getSettingsManager().getPageSize());
        ChatBlock.sendBlank(sender);
    }

    /**
     * Adds a menu item to the /clan menu
     *
     * @param syntax
     * @param description
     */
    public void addMenuItem(String syntax, String description)
    {
        addMenuItem(syntax, description, ChatColor.AQUA);
    }

    /**
     * Adds a menu item to the /clan menu, specifying syntax color
     * [color] /[syntax] - [description]
     *
     * @param syntax
     * @param description
     * @param color
     */
    public void addMenuItem(String syntax, String description, ChatColor color)
    {
        menuItems.add(color + "/" + syntax + ChatColor.WHITE + " - " + description);
    }
}
