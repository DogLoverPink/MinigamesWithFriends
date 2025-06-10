package doglover.minigameswithfriends.wouldyourather.effects.beneficialeffects;

import doglover.minigameswithfriends.MinigamesWithFriends;
import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

public class FasterMiningWhenStandingStill extends WYREffect {


    static {
        WYREffectHandler.registerBeneficialWYREffect(FasterMiningWhenStandingStill.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "Mine blocks increasingly faster when standing still";
    }

    public FasterMiningWhenStandingStill(Player player) {
        super(player);
        setRepeatable(true);
        subscribeToEvent(BlockBreakEvent.class);
    }

    @Override
    public void onTick() {
        if (getPlayer() == null || !getPlayer().isOnline()) {
            return;
        }
        Vector lastLocationVector = lastLocation != null ? lastLocation.toVector() : new Vector(0, 0, 0);
        if (!getPlayer().getLocation().toVector().equals(lastLocationVector)) {
            hasteStacks = 0;
            getPlayer().getAttribute(Attribute.BLOCK_BREAK_SPEED).removeModifier(key);
        }
        lastLocation = getPlayer().getLocation();
    }

    Location lastLocation;
    double hasteStacks = 0;
    NamespacedKey key = new NamespacedKey(MinigamesWithFriends.getGamePlugin(), "stand-still-haste-stacks"+getUniqueNumber());

    @Override
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (!player.equals(getPlayer())) {
            return;
        }
        hasteStacks += 0.2;
        AttributeModifier mod = new AttributeModifier(key, hasteStacks, AttributeModifier.Operation.ADD_NUMBER);
        player.getAttribute(Attribute.BLOCK_BREAK_SPEED).removeModifier(mod);
        player.getAttribute(Attribute.BLOCK_BREAK_SPEED).addModifier(mod);
    }

    @Override
    public void onEffectInitiate() {
        super.onEffectInitiate();
    }

    @Override
    public void onEffectDecompose() {
        super.onEffectDecompose();
        getPlayer().getAttribute(Attribute.BLOCK_BREAK_SPEED).removeModifier(key);
    }
}
