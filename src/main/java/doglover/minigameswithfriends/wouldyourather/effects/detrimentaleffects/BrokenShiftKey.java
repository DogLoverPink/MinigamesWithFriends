package doglover.minigameswithfriends.wouldyourather.effects.detrimentaleffects;

import doglover.minigameswithfriends.MinigamesWithFriends;
import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class BrokenShiftKey extends WYREffect {


    static {
        WYREffectHandler.registerDetrimentalWYREffect(BrokenShiftKey.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "Your shift key is broken";
    }

    public BrokenShiftKey(Player player) {
        super(player);
        setRepeatable(false);
        subscribeToEvent(PlayerToggleSneakEvent.class);
    }

    NamespacedKey stepHeightKey = new NamespacedKey(MinigamesWithFriends.getGamePlugin(), "no_sneak" + getUniqueNumber());
    NamespacedKey speedKey = new NamespacedKey(MinigamesWithFriends.getGamePlugin(), "no_sneak_movement_speed" + getUniqueNumber());


    Location fakeBlockLocation;

    @Override
    public void onTick() {
        if (fakeBlockLocation != null) {
            BlockData realData = fakeBlockLocation.getBlock().getBlockData();
            getPlayer().sendBlockChange(fakeBlockLocation, realData);
            fakeBlockLocation = null;
        }

        if (doesPlayerThinkTheyreSneaking && getPlayer().getLocation().clone().subtract(0, 0.1, 0).getBlock().getType().isAir()) {
            AttributeModifier mod = new AttributeModifier(stepHeightKey, 10, AttributeModifier.Operation.ADD_NUMBER);
            getPlayer().getAttribute(Attribute.STEP_HEIGHT).removeModifier(mod);
            getPlayer().getAttribute(Attribute.STEP_HEIGHT).addModifier(mod);
            fakeBlockLocation = getPlayer().getLocation().clone().subtract(0, 9, 0);
            getPlayer().sendBlockChange(fakeBlockLocation, Material.BARRIER.createBlockData());
        } else {
            getPlayer().getAttribute(Attribute.STEP_HEIGHT).removeModifier(stepHeightKey);
        }
    }

    private boolean doesPlayerThinkTheyreSneaking;

    @Override
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        Player plr = event.getPlayer();
        if (!plr.equals(this.getPlayer())) {
            return;
        }
        doesPlayerThinkTheyreSneaking = event.isSneaking();
        if (event.isSneaking()) {
            event.setCancelled(true);
        } else {
            getPlayer().getAttribute(Attribute.STEP_HEIGHT).removeModifier(stepHeightKey);
        }
    }

    @Override
    public void onEffectInitiate() {
        super.onEffectInitiate();
        AttributeModifier mod = new AttributeModifier(speedKey, 1, AttributeModifier.Operation.ADD_NUMBER);
        getPlayer().getAttribute(Attribute.SNEAKING_SPEED).removeModifier(mod);
        getPlayer().getAttribute(Attribute.SNEAKING_SPEED).addModifier(mod);
    }

    @Override
    public void onEffectDecompose() {
        super.onEffectDecompose();
        getPlayer().getAttribute(Attribute.STEP_HEIGHT).removeModifier(stepHeightKey);
        getPlayer().getAttribute(Attribute.SNEAKING_SPEED).removeModifier(speedKey);
    }
}
