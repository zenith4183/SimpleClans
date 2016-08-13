package org.bitbucket.zenith4183.zclans.listeners;

import org.bitbucket.zenith4183.zclans.Clan;
import org.bitbucket.zenith4183.zclans.ClanPlayer;
import org.bitbucket.zenith4183.zclans.zClans;
import org.bukkit.ChatColor;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.text.MessageFormat;

/**
 * @author phaed
 */
public class SCEntityListener implements Listener
{

    private zClans plugin;

    /**
     *
     */
    public SCEntityListener()
    {
        plugin = zClans.getInstance();
    }

    /**
     * @param event
     */
    @EventHandler(priority = EventPriority.LOW)
    public void onEntityDeath(EntityDeathEvent event)
    {
        if (event.getEntity() instanceof Player)
        {
            Player victim = (Player) event.getEntity();

            if (plugin.getSettingsManager().isBlacklistedWorld(victim.getLocation().getWorld().getName()))
            {
                return;
            }

            Player attacker = null;

            // find attacker

            EntityDamageEvent lastDamageCause = victim.getLastDamageCause();

            if (lastDamageCause instanceof EntityDamageByEntityEvent)
            {
                EntityDamageByEntityEvent entityEvent = (EntityDamageByEntityEvent) lastDamageCause;

                if (entityEvent.getDamager() instanceof Player)
                {
                    attacker = (Player) entityEvent.getDamager();
                } else if (entityEvent.getDamager() instanceof Arrow)
                {
                    Arrow arrow = (Arrow) entityEvent.getDamager();

                    if (arrow.getShooter() instanceof Player)
                    {
                        attacker = (Player) arrow.getShooter();
                    }
                }
            }

            if (attacker != null && victim != null)
            {
                ClanPlayer acp;
                ClanPlayer vcp;

                acp = plugin.getClanManager().getCreateClanPlayer(attacker.getUniqueId());
                vcp = plugin.getClanManager().getCreateClanPlayer(victim.getUniqueId());

                double rewardPercent = 0;
                double multiplier;
                double percent = plugin.getSettingsManager().getMoneyPerKillPercent() / 100;
                float kdr = vcp.getKDR();

                if (vcp.getClan() == null || acp.getClan() == null || !vcp.getClan().isVerified() || !acp.getClan().isVerified())
                {
                    multiplier = plugin.getSettingsManager().getKDRMultipliesPerKillCivilian();
                    acp.addCivilianKill();
                    plugin.getStorageManager().insertKill(attacker, acp.getTag(), victim, "", "c");
                } else if (acp.getClan().isRival(vcp.getTag()))
                {
                    multiplier = plugin.getSettingsManager().getKDRMultipliesPerKillRival();
                    acp.addRivalKill();
                    plugin.getStorageManager().insertKill(attacker, acp.getTag(), victim, vcp.getTag(), "r");
                } else if (acp.getClan().isAlly(vcp.getTag()))
                {
                    multiplier = plugin.getSettingsManager().getKDRMultipliesPerKillRival();
                } else
                {
                    multiplier = plugin.getSettingsManager().getKDRMultipliesPerKillNeutral();
                    acp.addNeutralKill();
                    plugin.getStorageManager().insertKill(attacker, acp.getTag(), victim, vcp.getTag(), "n");
                }

                if (vcp.getClan() != null && acp.getClan() != null && acp.getClan().isAlly(vcp.getTag())) {
                    rewardPercent = (double) kdr * multiplier * percent * -1;
                } else {
                    rewardPercent = (double) kdr * multiplier * percent;
                }

                plugin.getKillCampingManager().addPlayerKill(attacker.getUniqueId().toString(), victim.getUniqueId().toString());
                
                if (plugin.getSettingsManager().getCooldownClanWide()) {
                	plugin.getKillCampingManager().addClanKill(acp.getTag(), victim.getUniqueId().toString());
                }

                if (rewardPercent != 0 && plugin.getSettingsManager().isMoneyPerKill())
                {
                    double victimMoney = plugin.getPermissionsManager().playerGetMoney(vcp.toPlayer());
                    double rewardMoney = Math.round(victimMoney * rewardPercent * 100D) / 100D;
                    plugin.getPermissionsManager().playerChargeMoney(vcp.toPlayer(), rewardMoney);

                    double splitReward = Math.round((rewardMoney / acp.getClan().getOnlineMembers().size()) * 100D) / 100D ;

                    for (ClanPlayer cp : acp.getClan().getOnlineMembers())
                    {
                        if (cp.getName() == acp.getName()) {
                            cp.toPlayer().sendMessage(ChatColor.AQUA + MessageFormat.format(plugin.getLang("player.got.money"), splitReward, victim.getName(), kdr));
                        } else {
                            cp.toPlayer().sendMessage(ChatColor.AQUA + MessageFormat.format(plugin.getLang("clanmate.got.money"), splitReward, attacker.getName(), victim.getName()));
                        }
                        plugin.getPermissionsManager().playerGrantMoney(cp.toPlayer(), splitReward);
                    }
                }

                // record death for victim
                vcp.addDeath();
                plugin.getStorageManager().updateClanPlayerAsync(vcp);
            }
        }
    }

    /**
     * @param event
     */
    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerInteract(PlayerInteractEntityEvent event)
    {
        if (event.isCancelled())
        {
            return;
        }

        if (plugin.getSettingsManager().isTamableMobsSharing() && event.getRightClicked() instanceof Tameable)
        {
        	Entity entity = event.getRightClicked();
            Player player = event.getPlayer();
            ClanPlayer cp = plugin.getClanManager().getClanPlayer(player);
            Tameable tamed = (Tameable) entity;

            if (tamed.isTamed())
            {
                if(entity instanceof Wolf && !((Wolf) entity).isSitting())
                {
                	return;
                }
                if (cp.getClan().isMember((Player) tamed.getOwner()))
                {
                    tamed.setOwner(player);
                }
            }
        }
    }

    /**
     * @param event
     */
    @EventHandler(priority = EventPriority.LOW)
    public void onEntityTarget(EntityTargetLivingEntityEvent event)
    {
        if (plugin.getSettingsManager().isTamableMobsSharing())
        {
            if (event.getEntity() instanceof Tameable && event.getTarget() instanceof Player)
            {
                ClanPlayer cp = plugin.getClanManager().getClanPlayer((Player) event.getTarget());
                Tameable wolf = (Tameable) event.getEntity();

                if (wolf.isTamed() && cp.getClan().isMember((Player) wolf.getOwner()))
                {
                	// cancels the event if the attacker is one out of his clan
                    event.setCancelled(true);
                }
            }
        }
    }

    /**
     * @param event
     */
    @EventHandler(priority = EventPriority.LOW)
    public void onEntityDamage(EntityDamageEvent event)
    {
        if (event.isCancelled())
        {
            return;
        }

        Player attacker = null;
        Player victim = null;


        if (event instanceof EntityDamageByEntityEvent)
        {
            EntityDamageByEntityEvent sub = (EntityDamageByEntityEvent) event;

            if (sub.getEntity() instanceof Player && sub.getDamager() instanceof Player)
            {
                attacker = (Player) sub.getDamager();
                victim = (Player) sub.getEntity();
            }
            
            if (plugin.getSettingsManager().isTamableMobsSharing())
            {
                if (sub.getEntity() instanceof Wolf && sub.getDamager() instanceof Player)
                {
                    attacker = (Player) sub.getDamager();
                    Wolf wolf = (Wolf) sub.getEntity();
                    ClanPlayer cp = plugin.getClanManager().getClanPlayer(attacker);
                    if (wolf.isTamed() && cp.getClan().isMember((Player) wolf.getOwner()))
                    {
                    	// Sets the wolf to friendly if the attacker is one out of his clan
                        wolf.setAngry(false);
                    }
                }
            }

            if (sub.getEntity() instanceof Player && sub.getDamager() instanceof Arrow)
            {
                Arrow arrow = (Arrow) sub.getDamager();

                if (arrow.getShooter() instanceof Player)
                {
                    attacker = (Player) arrow.getShooter();
                    victim = (Player) sub.getEntity();
                }
            }
        }

        if (victim != null && plugin.getSettingsManager().isBlacklistedWorld(victim.getLocation().getWorld().getName()))
        {
        	return;
        }

        if (attacker != null && victim != null)
        {
            if (plugin.getKillCampingManager().isPlayerCamping(attacker, victim)) {
            	event.setCancelled(true);
            }

            ClanPlayer acp = plugin.getClanManager().getClanPlayer(attacker);
            ClanPlayer vcp = plugin.getClanManager().getClanPlayer(victim);


            Clan vclan = vcp == null ? null : vcp.getClan();
            Clan aclan = acp == null ? null : acp.getClan();

            if (vclan != null)
            {
                if (aclan != null)
                {
                    // personal ff enabled, allow damage

                    if (vcp.isFriendlyFire())
                    {
                        return;
                    }

                    // clan ff enabled, allow damage

                    if (vclan.isFriendlyFire())
                    {
                        return;
                    }

                    // same clan, deny damage

                    if (vclan.equals(aclan))
                    {
                        event.setCancelled(true);
                        return;
                    }

                    // ally clan, deny damage

                    if (vclan.isAlly(aclan.getTag()))
                    {
                        event.setCancelled(true);
                    }
                } else
                {
                    // not part of a clan - check if safeCivilians is set

                    if (plugin.getSettingsManager().getSafeCivilians())
                    {
                        event.setCancelled(true);
                    }
                }
            } else
            {
                // not part of a clan - check if safeCivilians is set

                if (plugin.getSettingsManager().getSafeCivilians())
                {
                    event.setCancelled(true);
                }
            }
        }
    }
}
