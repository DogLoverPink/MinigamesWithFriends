package doglover.minigameswithfriends.wouldyourather.effects.detrimentaleffects;

import doglover.minigameswithfriends.utils.NumberUtils;
import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import org.bukkit.Registry;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.List;
import java.util.Random;

public class Annoyance extends WYREffect {


    static {
        WYREffectHandler.registerDetrimentalWYREffect(Annoyance.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "You get periodically annoyed";
    }

    public Annoyance(Player player) {
        super(player);
        setRepeatable(true);
    }

    boolean inTargetedSoundAnnoyance = false;

    @Override
    public void onTick() {
        if (NumberUtils.chanceOf(0.0005)) {
            getPlayer().showWinScreen();
        }
        if (NumberUtils.chanceOf(0.0005)) {
            getPlayer().showDemoScreen();
        }
        if (!inTargetedSoundAnnoyance && NumberUtils.chanceOf(0.00075)) {
            annoyanceCount = 200;
        }
        if (!inTargetedSoundAnnoyance && annoyanceCount-- > 0) {
            getPlayer().playSound(getPlayer(), getRandomSound(), 1, getRandomPitch());
            getPlayer().playSound(getPlayer(), getRandomSound(), 1, getRandomPitch());
            if (annoyanceCount == 0) {
                stopSounds();
            }
        } else if (annoyanceCount-- > 0) {
            if (annoyanceCount == 0) {
                inTargetedSoundAnnoyance = false;
            }
            getPlayer().playSound(getPlayer(), Sound.ENTITY_DOLPHIN_DEATH, 1, 2);
            getPlayer().playSound(getPlayer(), Sound.ENTITY_GHAST_DEATH, 1, 1);
        }
        if (!inTargetedSoundAnnoyance && annoyanceCount <= 0 && NumberUtils.chanceOf(0.00075)) {
            inTargetedSoundAnnoyance = true;
            annoyanceCount = 100;
        }
    }

    private void stopSounds() {
        getPlayer().stopAllSounds();
    }

    List<Sound> randomSounds = Registry.SOUNDS.stream().toList();
    static final Random random = new Random();

    private Sound getRandomSound() {
        return randomSounds.get(random.nextInt(randomSounds.size()));
    }

    private float getRandomPitch() {
        return random.nextFloat(0, 2);
    }

    int annoyanceCount = -1;

    @Override
    public void onEffectInitiate() {
        super.onEffectInitiate();
    }

    @Override
    public void onEffectDecompose() {
        super.onEffectDecompose();
        stopSounds();
    }
}
