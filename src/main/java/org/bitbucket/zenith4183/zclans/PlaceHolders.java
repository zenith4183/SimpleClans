package org.bitbucket.zenith4183.zclans;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

/**
 * Created by zenith4183 on 8/13/16.
 */
public class PlaceHolders extends PlaceholderExpansion {
    private zClans plugin;

    public PlaceHolders(zClans plugin) {
        this.plugin = plugin;
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        ClanPlayer cp = plugin.getClanManager().getClanPlayer(player);
        if (cp == null) {
            return "";
        }

        if (identifier.equals("tag")) {
            return cp.getTag();
        }

        if (identifier.equals("tag_label")) {
            return cp.getTagLabel();
        }

        if (identifier.equals("rank")) {
            return cp.getRank();
        }

        if (identifier.equals("civilian_kills")) {
            return Integer.toString(cp.getCivilianKills());
        }

        if (identifier.equals("neutral_kills")) {
            return Integer.toString(cp.getNeutralKills());
        }

        if (identifier.equals("rival_kills")) {
            return Integer.toString(cp.getRivalKills());
        }

        if (identifier.equals("weighted_kills")) {
            return Double.toString(cp.getWeightedKills());
        }

        if (identifier.equals("deaths")) {
            return Integer.toString(cp.getDeaths());
        }

        if (identifier.equals("kdr")) {
            return Float.toString(cp.getKDR());
        }

        if (identifier.equals("is_leader")) {
            return Boolean.toString(cp.isLeader());
        }

        Clan clan = cp.getClan();
        if (clan == null) {
            return "";
        }

        if (identifier.equals("clan_tag")) {
            return clan.getTag();
        }

        if (identifier.equals("clan_tag_label")) {
            return clan.getTagLabel(cp.isLeader());
        }

        if (identifier.equals("clan_color_tag")) {
            return clan.getColorTag();
        }

        if (identifier.equals("clan_founded_string")) {
            return clan.getFoundedString();
        }

        if (identifier.equals("clan_name")) {
            return clan.getName();
        }

        if (identifier.equals("clan_average_wk")) {
            return Integer.toString(clan.getAverageWK());
        }

        if (identifier.equals("clan_size")) {
            return Integer.toString(clan.getSize());
        }

        if (identifier.equals("clan_total_civilian")) {
            return Integer.toString(clan.getTotalCivilian());
        }

        if (identifier.equals("clan_total_neutral")) {
            return Integer.toString(clan.getTotalNeutral());
        }

        if (identifier.equals("clan_total_rival")) {
            return Integer.toString(clan.getTotalRival());
        }

        if (identifier.equals("clan_total_deaths")) {
            return Integer.toString(clan.getTotalDeaths());
        }

        if (identifier.equals("clan_total_kdr")) {
            return Float.toString(clan.getTotalKDR());
        }

        return null;
    }

    @Override
    public boolean persist(){
        return true;
    }

    @Override
    public boolean canRegister(){
        return true;
    }

    @Override
    public String getIdentifier() {
        return plugin.getDescription().getName();
    }

    @Override
    public String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }
}
