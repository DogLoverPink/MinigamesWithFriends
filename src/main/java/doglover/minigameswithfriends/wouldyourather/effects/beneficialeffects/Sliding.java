package doglover.minigameswithfriends.wouldyourather.effects.beneficialeffects;

import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.util.TriState;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.util.Vector;

public class Sliding extends WYREffect {


    static {
        WYREffectHandler.registerBeneficialWYREffect(Sliding.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "Be able to slide by crouching while sprinting";
    }

    public Sliding(Player player) {
        super(player);
        setRepeatable(false);
        subscribeToEvent(PlayerToggleSneakEvent.class);
    }

    Silverfish fish = null;
    Vector fishVector = null;
    Vector maxFishVelocity = null;
    Location lastPos = null;
    int countWithoutMoreSpeed = 40;
    int noMovementTicks = 0;


    @Override
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        if (!event.getPlayer().equals(getPlayer()) || !event.isSneaking()) {
            return;
        }
        if (!getPlayer().isSprinting() || getPlayer().getGameMode().equals(GameMode.SPECTATOR)) {
            return;
        }

        if (fish != null) {
            killFish();
            return;
        }

        fish = getPlayer().getWorld().spawn(getPlayer().getLocation(), Silverfish.class, silverfish -> {
            silverfish.setAware(false);
            silverfish.setInvulnerable(true);
            silverfish.setSilent(true);
            silverfish.setInvisible(true);
        });
        fish.setFrictionState(TriState.FALSE);
        fishVector = getPlayer().getLocation().getDirection().multiply(new Vector(0.5, 0, 0.5));
        maxFishVelocity = fishVector.clone();
        fishVector.setY(getPlayer().getVelocity().getY());
        lastPos = getPlayer().getLocation();
        countWithoutMoreSpeed = 40;
        noMovementTicks = 0;
        fish.setVelocity(fishVector);
        event.setCancelled(true);
        fish.addPassenger(getPlayer());
        getGame().sendActionBar("sliding", Component.empty(), getPlayer(), 1);
    }

    @Override
    public void onTick() {
        if (fish == null) {
            return;
        }
        if (fish.getLocation().getY() < lastPos.getY()) {
            countWithoutMoreSpeed = 40;
        } else {
            countWithoutMoreSpeed--;
        }
        if (fish.getLocation().getWorld() != getPlayer().getWorld()) {
            killFish();
            return;
        }
        if (fish.getLocation().distanceSquared(lastPos) < 0.001) {
            if (noMovementTicks++ >= 4) {
                killFish();
                return;
            }
        } else {
            noMovementTicks = 0;
        }
        fishVector.setX(maxFishVelocity.getX() * (countWithoutMoreSpeed / 40.0));
        fishVector.setZ(maxFishVelocity.getZ() * (countWithoutMoreSpeed / 40.0));
        fishVector.setY(fish.getVelocity().getY());
        fish.setVelocity(fishVector);
        lastPos = fish.getLocation();
        if (countWithoutMoreSpeed <= 5) {
            killFish();
        }
    }

    private void killFish() {
        fish.remove();
        fish = null;
        fishVector = null;
    }


    @Override
    public void onEffectInitiate() {
        super.onEffectInitiate();
    }

    @Override
    public void onEffectDecompose() {
        super.onEffectDecompose();
        if (fish != null) {
            killFish();
        }
    }
}
