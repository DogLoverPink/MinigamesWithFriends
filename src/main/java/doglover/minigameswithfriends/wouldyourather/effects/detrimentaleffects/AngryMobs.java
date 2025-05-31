package doglover.minigameswithfriends.wouldyourather.effects.detrimentaleffects;

import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.*;

public class AngryMobs extends WYREffect {


    static {
        WYREffectHandler.registerDetrimentalWYREffect(AngryMobs.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "Everyone just seems angry at you";
    }

    public AngryMobs(Player player) {
        super(player);
        setRepeatable(true);
        subscribeToEvent(PlayerDeathEvent.class);
    }

    Set<UUID> alreadyDamaged = new HashSet<>();

    @Override
    public void on4HertzTick() {
        List<Entity> nearbyEntities = getPlayer().getNearbyEntities(15, 8, 15);
        for (Entity entity : nearbyEntities) {
            if (entity instanceof Player || (!(entity instanceof LivingEntity mob)) || alreadyDamaged.contains(entity.getUniqueId())) {
                continue;
            }
            mob.damage(0.0, getPlayer());
            alreadyDamaged.add(entity.getUniqueId());
        }
    }

    @Override
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (!event.getPlayer().equals(getPlayer())) {
            return;
        }
        alreadyDamaged.clear();
    }

    @Override
    public void onEffectInitiate() {
        super.onEffectInitiate();
    }

    @Override
    public void onEffectDecompose() {
        super.onEffectDecompose();
    }
}
