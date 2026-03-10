package doglover.minigameswithfriends.wouldyourather.effects.beneficialeffects;

import doglover.minigameswithfriends.Game;
import doglover.minigameswithfriends.MinigamesWithFriends;
import doglover.minigameswithfriends.gamemodes.WouldYouRatherGamemode;
import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.key.Keyed;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickCallback;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;
import java.util.function.Consumer;

public class StreetInterviewer extends WYREffect {


    static {
        WYREffectHandler.registerBeneficialWYREffect(StreetInterviewer.class);
    }

    List<Player> alreadyPicked = new ArrayList<>();
    Map<String, Consumer<Player>> positiveEffects = new HashMap<>();
    Map<String, Consumer<Player>> negativeEffects = new HashMap<>();
    List<PotionEffectType> positivePotionEffects
            = new ArrayList<>(List.of(PotionEffectType.HASTE, PotionEffectType.SPEED, PotionEffectType.JUMP_BOOST, PotionEffectType.RESISTANCE, PotionEffectType.REGENERATION, PotionEffectType.ABSORPTION, PotionEffectType.LUCK, PotionEffectType.STRENGTH));
    List<PotionEffectType> negativePotionEffects = new ArrayList<>(List.of(PotionEffectType.MINING_FATIGUE, PotionEffectType.HUNGER, PotionEffectType.SLOWNESS, PotionEffectType.POISON, PotionEffectType.WEAKNESS));

    PotionEffectType goodPotionEffect = PotionEffectType.HERO_OF_THE_VILLAGE;
    PotionEffectType badPotionEffect = PotionEffectType.BAD_OMEN;

    String goodEffect = "a";
    String badEffect = "b";
    UUID affectedPlayer = null;

    int intensity = 1;
    ArrayDeque<Player> plrQueue = new ArrayDeque<>();

    @Override
    public String getDescriptionBlurb() {
        return "Get approached by a street interviewer.";
    }

    public StreetInterviewer(Player player) {
        super(player);
        setRepeatable(true);
        subscribeToEvent(PlayerRespawnEvent.class);

    }

    @Override
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (affectedPlayer == null || !affectedPlayer.equals(event.getPlayer().getUniqueId())) {
            return;
        }
        Player plr = event.getPlayer();
        Bukkit.getScheduler().runTaskLater(MinigamesWithFriends.getGamePlugin(), () -> {
            if (goodEffect.startsWith("Gain permanent")) {
                positiveEffects.get(goodEffect).accept(plr);
            }
            if (badEffect.startsWith("Gain permanent")) {
                negativeEffects.get(badEffect).accept(plr);
            }
        }, 2);
    }

    public String formatEffect(String effect, boolean good) {
        if (effect.toUpperCase().contains("HEARTS")) {
            return effect.replace("%1$", intensity * 2 + "");
        }
        return effect.replace("%2$", good ? goodPotionEffect.getName() : badPotionEffect.getName()).replace("%1$", intensity + "");
    }

    public String getRandomGoodEffect() {
        int rand = Game.getRandom().nextInt(0, positiveEffects.size());
        String effect = positiveEffects.keySet().toArray(String[]::new)[rand];
        goodPotionEffect = positivePotionEffects.get(Game.getRandom().nextInt(0, positivePotionEffects.size()));
        return effect;
    }

    public String getRandomBadEffect() {
        int rand = Game.getRandom().nextInt(0, negativeEffects.size());
        String effect = negativeEffects.keySet().toArray(String[]::new)[rand];
        badPotionEffect = negativePotionEffects.get(Game.getRandom().nextInt(0, negativePotionEffects.size()));
        return effect;
    }

    MiniMessage mm = MiniMessage.miniMessage();

    @Override
    public void onEffectInitiate() {
        super.onEffectInitiate();
        initilizeMaps();
        Player p = getPlayer();
        List<Player> players = new ArrayList<>(MinigamesWithFriends.getGame().getPlayers());
        players.remove(getPlayer());
        Collections.shuffle(players);
        plrQueue = new ArrayDeque<>(players);
        goodEffect = getRandomGoodEffect();
        badEffect = getRandomBadEffect();
        sendPromptToPlayer(p);
    }

    public void sendPromptToPlayer(Player p) {
        p.playSound(p, Sound.ENTITY_VILLAGER_CELEBRATE, 1, 1);
        p.showTitle(Title.title(Component.text("You have an offer!", NamedTextColor.GREEN), Component.text("Check Chat", NamedTextColor.YELLOW)));

        String goodEffectFormatted = formatEffect(goodEffect, true);
        String badEffectFormatted = formatEffect(badEffect, false);
        p.sendMessage(mm.deserialize("<green>-----------------------</green>\n" +
                "<yellow>Well well well, what's it gonna be?</yellow>\n" +
                "<color:#63ffa7>" + goodEffectFormatted + "</color>\n" +
                "                    <b><dark_red>BUT</dark_red></b>\n" +
                "<color:#ed6f5f>" + badEffectFormatted + "</color>\n"));
        ClickCallback<Audience> callback = this::chooseOptionOne;
        Component message = Component.text("").appendNewline();
        message = message.append(
                Component.text("[Select this option]").color(NamedTextColor.YELLOW).decorate(TextDecoration.BOLD)
                        .clickEvent(ClickEvent.callback(callback))
                        .hoverEvent(Component.text("Take these effects for yourself").color(NamedTextColor.GREEN)));
        p.sendMessage(message);
        ClickCallback<Audience> callback2 = this::chooseOptionTwo;
        Component message2 = Component.text("OR").color(NamedTextColor.YELLOW).appendNewline();
        message2 = message2.append(
                Component.text("[Double it and give it to the next person]").color(NamedTextColor.GREEN).decorate(TextDecoration.BOLD)
                        .clickEvent(ClickEvent.callback(callback2))
                        .hoverEvent(Component.text("Pass on these effects to someone else").color(NamedTextColor.GREEN)));
        p.sendMessage(message2);
    }

    private void chooseOptionOne(Audience aud) {

        UUID uuid = aud.get(Identity.UUID).get();

        Player p = Bukkit.getPlayer(uuid);

        if (alreadyPicked.contains(p)) {
            return;
        } else {
            alreadyPicked.add(p);
        }

        p.playSound(p, Sound.BLOCK_DISPENSER_DISPENSE, 1, 1);
        p.sendMessage(mm.deserialize("<yellow>Whew, thank goodness! I didn't want to have to get double of these things anyway. Enjoy!</yellow>"));

        positiveEffects.get(goodEffect).accept(p);
        negativeEffects.get(badEffect).accept(p);
        affectedPlayer = p.getUniqueId();

    }

    private void chooseOptionTwo(Audience aud) {
        UUID uuid = aud.get(Identity.UUID).get();

        Player p = Bukkit.getPlayer(uuid);

        if (alreadyPicked.contains(p)) {
            return;
        } else {
            alreadyPicked.add(p);
        }

        if (plrQueue.isEmpty()) {
            p.sendMessage(mm.deserialize("<yellow>Oh well, you were the last one... guess I'm taking all these goodies for myself! Bye!</yellow>"));
            p.playSound(p, Sound.BLOCK_DISPENSER_DISPENSE, 1, 1);
            this.selfDestruct();
            return;
        }
        Player plrToPassTo = plrQueue.pop();
        p.sendMessage(mm.deserialize("<yellow>Doubling it eh? Now it's up to <b><green>" + plrToPassTo.getName() + "</green></b> to choose. Bye!</yellow>"));
        intensity *= 2;
        p.playSound(p, Sound.BLOCK_DISPENSER_DISPENSE, 1, 1);
        sendPromptToPlayer(plrToPassTo);
    }

    private void initilizeMaps() {

        positiveEffects.put("Gain permanent %2$ %1$", this::givePositivePotionEffect);
        positiveEffects.put("Gain %1$ random beneficial WYR effects", this::gainBeneficialWYREffect);
        positiveEffects.put("Gain %1$ max hearts", this::gainMaxHearts);

        negativeEffects.put("Gain permanent %2$ %1$", this::giveNegativePotionEffect);
        negativeEffects.put("Lose %1$ max hearts", this::loseMaxHearts);
        negativeEffects.put("Gain %1$ random detrimental WYR effects", this::gainDetrimentalWYREffect);
    }


    private void givePositivePotionEffect(Player plr) {
        plr.addPotionEffect(new PotionEffect(goodPotionEffect, -1, intensity - 1));
        plr.sendMessage(mm.deserialize("<yellow>You've gained permanent</yellow> <green>" + goodPotionEffect.getName() + "</green><yellow>!</yellow>"));
    }

    private void gainBeneficialWYREffect(Player plr) {
        for (int i = 0; i < intensity; i++) {

            List<Class<? extends WYREffect>> current = MinigamesWithFriends.getGame().getGamemode(WouldYouRatherGamemode.class).currentlyAppliedBenefitsAndDetriments.get(plr.getUniqueId());
            MinigamesWithFriends.getGame().getGamemode(WouldYouRatherGamemode.class).effectsToChooseFrom.putIfAbsent(plr.getUniqueId(), new ArrayList<>());
            WYREffect good1 = WYREffectHandler.getRandomBeneficialWYREffectExceptFor(plr, current);

            if (!good1.isRepeatable()) {
                MinigamesWithFriends.getGame().getGamemode(WouldYouRatherGamemode.class).currentlyAppliedBenefitsAndDetriments.get(plr.getUniqueId()).add(good1.getClass());
            }

            plr.sendMessage(mm.deserialize("<yellow>You gained <green>" + good1.getDescriptionBlurb() + "</green>!</yellow>"));
            good1.onEffectInitiate();
        }

    }

    NamespacedKey positiveInterviewerKey = new NamespacedKey(MinigamesWithFriends.getGamePlugin(), "wyr_street_interviewer_positive_" + getUniqueNumber());
    NamespacedKey negativeinterviewerKey = new NamespacedKey(MinigamesWithFriends.getGamePlugin(), "wyr_street_interviewer_negative_" + getUniqueNumber());

    private void gainMaxHearts(Player plr) {
        plr.getAttribute(Attribute.MAX_HEALTH).addModifier(new AttributeModifier(positiveInterviewerKey, intensity * 4.0, AttributeModifier.Operation.ADD_NUMBER));
        plr.sendMessage(mm.deserialize("<yellow>You have <green>gained</green> <dark_red>" + intensity * 2 + "</dark_red> max hearts!</yellow>"));
    }

    private void giveNegativePotionEffect(Player plr) {
        plr.addPotionEffect(new PotionEffect(badPotionEffect, -1, intensity - 1));
        plr.sendMessage(mm.deserialize("<yellow>You've gained permanent</yellow> <red>" + badPotionEffect.getName() + "</red><yellow>!</yellow>"));
    }

    private void loseMaxHearts(Player plr) {
        plr.getAttribute(Attribute.MAX_HEALTH).addModifier(new AttributeModifier(negativeinterviewerKey, intensity * -4.0, AttributeModifier.Operation.ADD_NUMBER));
        plr.sendMessage(mm.deserialize("<yellow>You have <red>lost</red> <dark_red>" + intensity * 2 + "</dark_red> max hearts!</yellow>"));
    }

    private void gainDetrimentalWYREffect(Player plr) {

        for (int i = 0; i < intensity; i++) {
            List<Class<? extends WYREffect>> current = MinigamesWithFriends.getGame().getGamemode(WouldYouRatherGamemode.class).currentlyAppliedBenefitsAndDetriments.get(plr.getUniqueId());
            MinigamesWithFriends.getGame().getGamemode(WouldYouRatherGamemode.class).effectsToChooseFrom.putIfAbsent(plr.getUniqueId(), new ArrayList<>());
            WYREffect bad1 = WYREffectHandler.getRandomDetrimentialWYREffectExceptFor(plr, current);

            if (!bad1.isRepeatable()) {
                MinigamesWithFriends.getGame().getGamemode(WouldYouRatherGamemode.class).currentlyAppliedBenefitsAndDetriments.get(plr.getUniqueId()).add(bad1.getClass());
            }

            plr.sendMessage(mm.deserialize("<yellow>You gained <red>" + bad1.getDescriptionBlurb() + "</red>!</yellow>"));
            bad1.onEffectInitiate();
        }

    }

    @Override
    public void onEffectDecompose() {
        super.onEffectDecompose();
        if (affectedPlayer != null) {
            Player plr = Bukkit.getPlayer(affectedPlayer);
            if (plr != null) {
                plr.getAttribute(Attribute.MAX_HEALTH).removeModifier(positiveInterviewerKey);
                plr.getAttribute(Attribute.MAX_HEALTH).removeModifier(negativeinterviewerKey);
                if (goodEffect.startsWith("Gain permanent")) {
                    plr.removePotionEffect(goodPotionEffect);
                }
                if (badEffect.startsWith("Gain permanent")) {
                    plr.removePotionEffect(badPotionEffect);
                }
            }
        }

    }


}
