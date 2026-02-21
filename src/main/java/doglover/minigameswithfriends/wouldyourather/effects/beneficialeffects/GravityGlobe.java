package doglover.minigameswithfriends.wouldyourather.effects.beneficialeffects;

import doglover.minigameswithfriends.MinigamesWithFriends;
import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class GravityGlobe extends WYREffect {


    static {
        WYREffectHandler.registerBeneficialWYREffect(GravityGlobe.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "Get a gravity globe";
    }

    public GravityGlobe(Player player) {
        super(player);
        setRepeatable(false);
        subscribeToEvent(PlayerInteractEvent.class);
        subscribeToEvent(PlayerDeathEvent.class);
    }

    MiniMessage miniMessage = MiniMessage.miniMessage();


    @Override
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (event.getPlayer().equals(getPlayer())) {
            getPlayer().getAttribute(Attribute.GRAVITY).removeModifier(gravityGlobeAttributeKey);
            getPlayer().setAllowFlight(false);
            isGravityInverted = false;
        }
    }

    boolean isGravityInverted = false;

    @Override
    public void onPlayerInteract(PlayerInteractEvent event) {
       if (!event.getPlayer().equals(getPlayer())) {
           return;
       }
       if (event.getItem() == null || !event.getItem().getType().equals(Material.RECOVERY_COMPASS)) {
           return;
       }
       if (Boolean.FALSE.equals(event.getItem().getPersistentDataContainer().get(gravityGlobeOwnerKey, PersistentDataType.BOOLEAN))) {
           return;
       }
       if (isGravityInverted) {
           getPlayer().getAttribute(Attribute.GRAVITY).removeModifier(gravityGlobeAttributeKey);
           getPlayer().setAllowFlight(false);
           isGravityInverted = false;
           return;
       }
       isGravityInverted = true;
       getPlayer().setAllowFlight(true);
       getPlayer().damage(2);
       double strength = -2 * getPlayer().getAttribute(Attribute.GRAVITY).getValue();
       getPlayer().getAttribute(Attribute.GRAVITY).addModifier(new AttributeModifier(gravityGlobeAttributeKey, strength, AttributeModifier.Operation.ADD_NUMBER));
    }

    NamespacedKey gravityGlobeAttributeKey = new NamespacedKey(MinigamesWithFriends.getGamePlugin(), "gravity_globe_key" + getUniqueNumber());
    NamespacedKey gravityGlobeOwnerKey = new NamespacedKey(MinigamesWithFriends.getGamePlugin(), "gravity_globe_owner_key" + getUniqueNumber());

    @Override
    public void onEffectInitiate() {
        super.onEffectInitiate();
        ItemStack globe = new ItemStack(Material.RECOVERY_COMPASS, 1);
        globe.editMeta(meta -> {
                    meta.displayName(miniMessage.deserialize("<#FF2864>Gravity Globe").decoration(TextDecoration.ITALIC, false));
                    meta.lore(List.of(
                            miniMessage.deserialize("<gray>Right click to change gravity").decoration(TextDecoration.ITALIC, false),
                            miniMessage.deserialize("<gray>Property of "+getPlayer().getName()).decoration(TextDecoration.ITALIC, false)
                    ));
                }
        );
        globe.editPersistentDataContainer(container -> {
            container.set(gravityGlobeOwnerKey, PersistentDataType.BOOLEAN, Boolean.TRUE);
        });
        getPlayer().give(globe);
    }

    @Override
    public void onEffectDecompose() {
        super.onEffectDecompose();
        getPlayer().getAttribute(Attribute.GRAVITY).removeModifier(gravityGlobeAttributeKey);
    }
}
