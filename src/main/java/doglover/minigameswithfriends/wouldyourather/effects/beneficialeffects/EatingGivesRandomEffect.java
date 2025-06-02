package doglover.minigameswithfriends.wouldyourather.effects.beneficialeffects;

import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EatingGivesRandomEffect extends WYREffect {


    static {
        WYREffectHandler.registerBeneficialWYREffect(EatingGivesRandomEffect.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "Get a random beneficial effect after eating";
    }

    public EatingGivesRandomEffect(Player player) {
        super(player);
        setRepeatable(true);
        subscribeToEvent(PlayerItemConsumeEvent.class);
    }

    @Override
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        if (!player.equals(getPlayer())) {
            return;
        }
        if (event.getItem().getType().isEdible()) {
            giveRandomEffect();
        }
    }

    private static final Random random= new Random();

    private void giveRandomEffect() {
        List<PotionEffectType> availableEffects = new ArrayList<>(effects);
        PotionEffectType randomEffect = availableEffects.get((int) (Math.random() * availableEffects.size()));
        int time = random.nextInt(300) + 60;
        int level = random.nextInt(2);
        getPlayer().addPotionEffect(new PotionEffect(randomEffect, time, level, false, true, true));
    }

    List<PotionEffectType> effects = List.of(
            PotionEffectType.SPEED,
            PotionEffectType.JUMP_BOOST,
            PotionEffectType.STRENGTH,
            PotionEffectType.REGENERATION,
            PotionEffectType.FIRE_RESISTANCE,
            PotionEffectType.RESISTANCE,
            PotionEffectType.HASTE,
            PotionEffectType.INVISIBILITY
    );

    @Override
    public void onEffectInitiate() {
        super.onEffectInitiate();
    }

    @Override
    public void onEffectDecompose() {
        super.onEffectDecompose();
    }
}
