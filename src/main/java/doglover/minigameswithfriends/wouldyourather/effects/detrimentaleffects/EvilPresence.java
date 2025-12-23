package doglover.minigameswithfriends.wouldyourather.effects.detrimentaleffects;

import com.destroystokyo.paper.profile.PlayerProfile;
import doglover.minigameswithfriends.MinigamesWithFriends;
import doglover.minigameswithfriends.utils.ParticleUtils;
import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Phantom;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.profile.PlayerTextures;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.UUID;

public class EvilPresence extends WYREffect {


    static {
        WYREffectHandler.registerDetrimentalWYREffect(EvilPresence.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "You feel an evil presence watching you...";
    }

    public EvilPresence(Player player) {
        super(player);
        setRepeatable(true);
        subscribeToEvent(EntityDamageEvent.class);
        subscribeToEvent(EntityDamageByEntityEvent.class);
        subscribeToEvent(EntityDamageByBlockEvent.class);
    }

    @Override
    public void onEntityDamageByBlock(EntityDamageByBlockEvent event) {
        onDamage(event.getEntity(), event.getDamage());
    }

    @Override
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        onDamage(event.getDamager(), event.getDamage());
    }

    @Override
    public void onEntityDamage(EntityDamageEvent event) {
        onDamage(event.getEntity(), event.getDamage());
    }

    private void onDamage(Entity entity, double amount) {
        if (!entity.equals(phantomTop) && !entity.equals(phantomBottom)) {
            return;
        }
        health -= amount;
        float newHealth = (float) (health / 100.0);
        healthBar.progress(Math.max(newHealth, 0.0f));
        entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_WOLF_ANGRY_GROWL, 1, 1);
        if (health <= 50 && health + amount > 50) {
            ItemStack newPhaseItem = getSkull("http://textures.minecraft.net/texture/bd6976a7f24a572cf242a6eddbdf842078ad4ea4d69fd22702865e06ccb944ed");
            display.setItemStack(newPhaseItem);
        }
        if (health <= 0) {
            onEyeDeath();
        }
    }

    double health = 100.0;

    Phantom phantomBottom;
    Phantom phantomTop;
    ItemDisplay display;
    boolean hasSummoned = false;

    private static final NamespacedKey scaleKey = new NamespacedKey(MinigamesWithFriends.getGamePlugin(), "phanty_scaleup");

    private void onEyeDeath() {
        ParticleUtils.createParticleCloud(phantomBottom.getLocation(), 120, Particle.WHITE_SMOKE, 120);
        ItemStack item = getSkull("http://textures.minecraft.net/texture/bd6976a7f24a572cf242a6eddbdf842078ad4ea4d69fd22702865e06ccb944ed");
        item.editMeta(meta ->
                meta.displayName(miniMessage.deserialize("<#9696FF>Eye of Cthulhu Mask").decoration(TextDecoration.ITALIC, false))
                );
        phantomBottom.getWorld().dropItem(phantomBottom.getLocation(), item);
        phantomBottom.remove();
        phantomTop.remove();
        display.remove();
        getPlayer().sendMessage(miniMessage.deserialize("<#AF4BFF>Eye of Cthulhu has been defeated!"));
        getPlayer().playSound(getPlayer(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1, 1);
        healthBar.addViewer(getPlayer());
        for (Entity entity : getPlayer().getNearbyEntities(50, 50, 50)) {
            if (entity instanceof Player plr) {
                plr.sendMessage(miniMessage.deserialize("<#AF4BFF>Eye of Cthulhu has been defeated!"));
                plr.playSound(plr, Sound.ENTITY_ENDER_DRAGON_GROWL, 1, 1);
            }
        }
        this.selfDestruct();
    }

    @Override
    public void onTick() {
        if (isPhantomInvalid()) {
            this.selfDestruct();
            return;
        }
        display.setRotation(phantomBottom.getYaw() + 180, phantomBottom.getPitch());
    }

    private boolean isPhantomInvalid() {
        return hasSummoned && (phantomBottom == null || phantomTop == null || !phantomBottom.isValid() || !phantomTop.isValid());
    }

    @Override
    public void on4HertzTick() {
        if (isPhantomInvalid()) {
            this.selfDestruct();
            return;
        }
        phantomBottom.setTarget(getPlayer());
        phantomTop.setTarget(getPlayer());
    }

    MiniMessage miniMessage = MiniMessage.miniMessage();

    BossBar healthBar = BossBar.bossBar(Component.text("Eye of Cthulhu"), 1.0f, BossBar.Color.RED, BossBar.Overlay.PROGRESS);

    private void summon() {
        getPlayer().sendMessage(miniMessage.deserialize("<#AF4BFF>Eye of Cthulhu has awoken!"));
        getPlayer().playSound(getPlayer(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1, 1);
        healthBar.addViewer(getPlayer());
        for (Entity entity : getPlayer().getNearbyEntities(50, 50, 50)) {
            if (entity instanceof Player plr) {
                plr.sendMessage(miniMessage.deserialize("<#AF4BFF>Eye of Cthulhu has awoken!"));
                plr.playSound(plr, Sound.ENTITY_ENDER_DRAGON_GROWL, 1, 1);
                healthBar.addViewer(getPlayer());
            }
        }
        phantomBottom = getPlayer().getWorld().spawn(getPlayer().getLocation(), Phantom.class);
        phantomBottom.setRotation(0, 0);
        phantomTop = getPlayer().getWorld().spawn(getPlayer().getLocation(), Phantom.class);

        phantomBottom.getAttribute(Attribute.SCALE).addModifier(new AttributeModifier(scaleKey, 2.5, AttributeModifier.Operation.ADD_NUMBER));
        phantomTop.getAttribute(Attribute.SCALE).addModifier(new AttributeModifier(scaleKey, 2.5, AttributeModifier.Operation.ADD_NUMBER));

        phantomTop.setRotation(0, 0);
        ItemStack cthulhuSkull = getSkull("http://textures.minecraft.net/texture/2551c2fc174d1018bdaa9bc20d9cfae023e523157a606f2ba9f3e691713b2806");

        display = getPlayer().getWorld().spawn(phantomBottom.getLocation(), ItemDisplay.class, entity -> {
            entity.setItemStack(cthulhuSkull);
        });

        phantomBottom.addPassenger(display);

        phantomTop.setShouldBurnInDay(false);
        phantomBottom.setShouldBurnInDay(false);

        phantomBottom.setAnchorLocation(getPlayer().getLocation());
        phantomTop.setAnchorLocation(getPlayer().getLocation());


        display.setTransformation(
                new Transformation(
                        new Vector3f(0, 1.25f, 0), //offset
                        new AxisAngle4f(),
                        new Vector3f(6.25f, 5f, 6.25f),
                        new AxisAngle4f()
                )
        );
        display.setPersistent(false);
        display.addPassenger(phantomTop);
        phantomTop.setTarget(getPlayer());

        phantomTop.setInvisible(true);
        phantomBottom.setInvisible(true);

        phantomBottom.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, -1, 9, false, false));
        phantomTop.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, -1, 9, false, false));
        hasSummoned = true;
    }

    BukkitTask summonTask;

    @Override
    public void onEffectInitiate() {
        super.onEffectInitiate();
        getPlayer().sendMessage(miniMessage.deserialize("<#32FF82>You feel an evil presence watching you..."));
        summonTask = Bukkit.getScheduler().runTaskLater(MinigamesWithFriends.getGamePlugin(), this::summon, 320L);

    }

    @Override
    public void onEffectDecompose() {
        super.onEffectDecompose();
        if (summonTask != null && !summonTask.isCancelled()) {
            summonTask.cancel();
        }
        for (Player player : MinigamesWithFriends.getGame().getPlayers()) {
            healthBar.removeViewer(player);
        }
        if (phantomBottom != null) {
            phantomBottom.remove();
            phantomTop.remove();
            display.remove();
        }
    }

    private ItemStack getSkull(String url) {
        UUID uuid = UUID.randomUUID();
        PlayerProfile profile = Bukkit.createProfile(uuid);
        PlayerTextures textures = profile.getTextures();
        try {
            textures.setSkin(URI.create(url).toURL());
        } catch (MalformedURLException e) {
            MinigamesWithFriends.getGamePlugin().getLogger().warning("Failed to fetch skin for evil presence WYR effect, ignoring");
            return new ItemStack(Material.PLAYER_HEAD);
        }
        profile.setTextures(textures);
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) head.getItemMeta();
        skullMeta.setPlayerProfile(profile);
        head.setItemMeta(skullMeta);
        return head;
    }


}
