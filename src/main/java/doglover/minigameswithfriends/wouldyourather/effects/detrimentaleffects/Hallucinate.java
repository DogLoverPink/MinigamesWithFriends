package doglover.minigameswithfriends.wouldyourather.effects.detrimentaleffects;

import doglover.minigameswithfriends.gamemodes.WouldYouRatherGamemode;
import doglover.minigameswithfriends.utils.TextUtils;
import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickCallback;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class Hallucinate extends WYREffect {

    private final int minTime = WouldYouRatherGamemode.config().getMinimumSecondsBeforeNewChoice() * 5;
    private final int maxTime = WouldYouRatherGamemode.config().getMaximumSecondsBeforeNewChoice() * 5;
    private int goal = -1;
    private int ticker = 0;


    private final String[] effects = {
            "The forsaken hand strikes twelve",
            "The shattered one becomes anew",
            "The weight of your sins crawl upon your back",
            "The bonds of thyself fall asunder",
            "The rites of the prophet beckon you",
            "The words of the ancient one overpower you",
            "The crucible of the damned topples once more",
            "The tongues of the forgotten chant your fate",
            "The sanctum of flesh withers beneath the moon",
            "The veil of truth burns in silence",
            "The chained star descends once more",
            "The covenant of the holy is sealed in blood",
            "The hollow choir screeches from beneath",
            "The fractured crown weeps crimson ash",
            "The blackened hour grows ever nearer",
            "The chorus of zealots fevers in unison"

    };

    ArrayList<String> effectList = new ArrayList<>(Arrays.asList(effects));

    static {
        WYREffectHandler.registerDetrimentalWYREffect(Hallucinate.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "You hallucinate the presence of a forsaken inquisitor";
    }

    public Hallucinate(Player player) {
        super(player);
        setRepeatable(false);
    }

    public String getRandomEffectAndCut() {
        int index = random.nextInt(effectList.size());
        String result = effectList.get(index);
        effectList.remove(index);
        return result;
    }

    public void resetEffects() {
        effectList.clear();
        effectList.addAll(Arrays.asList(effects));
        Collections.shuffle(effectList);
    }


    @Override
    public void on4HertzTick()  {
        if (goal == -1) {
            goal = random.nextInt(minTime, maxTime + 1);
            ticker = 0;
        }
        ticker++;
        if (ticker >= goal) {
            sendWYRIntro(getPlayer());
            sendWYREffectChoice(getPlayer(), 1);
            getPlayer().sendMessage(Component.empty());
            getPlayer().sendMessage(Component.text("OR").appendNewline().color(NamedTextColor.YELLOW));
            sendWYREffectChoice(getPlayer(), 2);
            sendWYREnd(getPlayer());
            resetEffects();
            goal = -1;
        }
    }
    public void chooseOptionOne(Audience aud) {

        UUID uuid = aud.get(Identity.UUID).get();
        Player plr = Bukkit.getServer().getPlayer(uuid);
        if (getPlayer() == null) {
            return;
        }
        getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA, 17 * 20, 1));
        getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, 8 * 20, 1));
        getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 9 * 20, 1));
        getPlayer().playSound(getPlayer().getLocation(), Sound.AMBIENT_CAVE, 1, 1);
        getPlayer().playSound(getPlayer().getLocation(), Sound.AMBIENT_CAVE, 1, .8f);
        getPlayer().playSound(getPlayer().getLocation(), Sound.AMBIENT_CAVE, 1, .9f);
        getPlayer().sendMessage(TextUtils.MINI_MESSAGE.deserialize("<red>You swear you can recall a mysterious question being asked, but no such inquiry truly existed..."));
        getPlayer().sendMessage(TextUtils.MINI_MESSAGE.deserialize("<red>You fall deeper into mental despair..."));
    }

    private void sendWYREffectChoice(Player plr, int choice) {
        ClickCallback<Audience> callback = this::chooseOptionOne;
        Component message = Component.text(getRandomEffectAndCut()).color(NamedTextColor.GREEN).appendNewline();
        message = message.append(Component.text("BUT...").color(NamedTextColor.YELLOW).appendNewline());
        message = message.append(Component.text(getRandomEffectAndCut()).color(NamedTextColor.RED)).appendNewline();
        message = message.append(
                Component.text("[SELECT]")
                        .clickEvent(ClickEvent.callback(callback))
                        .hoverEvent(Component.text("Choose option " + choice).color(NamedTextColor.GREEN)));
        getPlayer().sendMessage(message);
    }

    private void sendWYRIntro(Player plr) {
        Component message = Component.text("-------------------------").color(NamedTextColor.YELLOW).appendNewline();
        message = message.append(Component.text("Would you rather: ").color(NamedTextColor.AQUA));
        getPlayer().sendMessage(message);
    }

    private void sendWYREnd(Player plr) {
        Component message = Component.text("-------------------------").color(NamedTextColor.YELLOW);
        getPlayer().sendMessage(message);
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
