package doglover.minigameswithfriends.wouldyourather.effects.detrimentaleffects;

import doglover.minigameswithfriends.MinigamesWithFriends;
import doglover.minigameswithfriends.utils.EntityUtils;
import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.metadata.MetadataValueAdapter;
import org.bukkit.util.Vector;

import java.util.*;


public class HauntingMobs extends WYREffect {


    static {
        WYREffectHandler.registerDetrimentalWYREffect(HauntingMobs.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "Mobs haunt you when killed";
    }

    public HauntingMobs(Player player) {
        super(player);
        setRepeatable(false);
        subscribeToEvent(EntityDeathEvent.class);
    }

    List<HauntingMob> hauntingMobs = new ArrayList<>();

    @Override
    public void onTick() {
        World world = getPlayer().getWorld();
        Particle.DustOptions dustOptions = new Particle.DustOptions(Color.PURPLE, 1);
        for (HauntingMob mob : hauntingMobs) {
            if (mob.isHostile()) {
                dustOptions = new Particle.DustOptions(Color.BLACK, 1);
            }
            world.spawnParticle(Particle.DUST, mob.getLocation().clone().add(0, 1, 0), 10, dustOptions);
            if (!mob.isInMovementPhase()) {
                continue;
            }
            Location playerLoc = getPlayer().getLocation();
            Location mobLoc = mob.getLocation();
            Vector direction = playerLoc.toVector().subtract(mobLoc.toVector()).normalize().multiply(0.25);
            mobLoc.add(direction);
            if (mobLoc.distanceSquared(playerLoc) < 1) {
                doHauntAttack(mob);
            }
        }
        hauntingMobs.removeIf(HauntingMob::isMarkedForRemoval);
    }

    Set<UUID> reincarnatedMobs = new HashSet<>();

    private void doHauntAttack(HauntingMob mob) {
        if (mob.isHostile()) {
            LivingEntity entity = EntityUtils.spawnEntityWithAnimation(mob.getLocation(), mob.getType().getEntityClass().asSubclass(LivingEntity.class));
            entity.getAttribute(Attribute.MAX_HEALTH).setBaseValue(entity.getHealth() / 2);
            reincarnatedMobs.add(entity.getUniqueId());
        } else {
            getPlayer().damage(4);
        }
        getPlayer().playSound(getPlayer().getLocation(), Sound.ENTITY_WITHER_AMBIENT, 1.0f, 1f);
        mob.markForRemoval();
    }

    @Override
    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.getKiller() == null || !entity.getKiller().equals(getPlayer())) {
            return;
        }
        if (entity.getType() == EntityType.PLAYER) {
            return;
        }
        if (reincarnatedMobs.contains(entity.getUniqueId())) {
            reincarnatedMobs.remove(entity.getUniqueId());
            getPlayer().playSound(getPlayer().getLocation(), Sound.ENTITY_WITHER_HURT, 0.5f, 1f);
            return;
        }
        hauntingMobs.add(new HauntingMob(entity.getType(), entity.getLocation()));
    }

    @Override
    public void onEffectInitiate() {
        super.onEffectInitiate();
    }

    @Override
    public void onEffectDecompose() {
        super.onEffectDecompose();
    }

    private static final Random random = new Random();

    private class HauntingMob {
        private final EntityType type;
        private final Location location;
        private int counter;
        private int counterGoal;
        private boolean markForRemoval;
        private boolean isHostile;

        public HauntingMob(EntityType type, Location location) {
            this.type = type;
            this.location = location;
            counter = 0;
            counterGoal = random.nextInt(40, 100);
            markForRemoval = false;
            isHostile = Monster.class.isAssignableFrom(type.getEntityClass());

        }

        public boolean isHostile() {
            return isHostile;
        }

        public void markForRemoval() {
            markForRemoval = true;
        }

        public boolean isMarkedForRemoval() {
            return markForRemoval;
        }

        public boolean isInMovementPhase() {
            return counter++ >= counterGoal;
        }

        public EntityType getType() {
            return type;
        }

        public Location getLocation() {
            return location;
        }
    }
}
