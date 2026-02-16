package doglover.minigameswithfriends.wouldyourather.effects.beneficialeffects;

import doglover.minigameswithfriends.gamemodes.WouldYouRatherGamemode;
import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FiveMillionSmackeroonies extends WYREffect {


    static {
        WYREffectHandler.registerBeneficialWYREffect(FiveMillionSmackeroonies.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "Get $5,000,000!!!";
    }

    public FiveMillionSmackeroonies(Player player) {
        super(player);
        setRepeatable(true);
    }

    boolean dontTick = false;

    @Override
    public void onTick() {
        if (!isPlayerValid() || dontTick) {
            return;
        }
        String money = formatDollarsWithCommas(moneyTotals.get(getPlayer().getUniqueId()));
        getGame().addScoreboardContribution("§a" + getPlayer().getName()+": §e"+money);
    }

    static Map<UUID, Integer> moneyTotals = new HashMap<>();

    @Override
    public void onEffectInitiate() {
        super.onEffectInitiate();
        if (moneyTotals.containsKey(getPlayer().getUniqueId())) {
            //Prevent duplicate scoreboard entries
            dontTick = true;
        }
        moneyTotals.putIfAbsent(getPlayer().getUniqueId(), 0);
        moneyTotals.replace(getPlayer().getUniqueId(), moneyTotals.get(getPlayer().getUniqueId()) + 5000000);
        getGame().broadcast(MiniMessage.miniMessage().deserialize(
                "<green>Congratulations to </green><yellow>"
                        + getPlayer().getName()
                        + "</yellow><green>! They just won </green><yellow>$5,000,000!</yellow>"));
    }

    private String formatDollarsWithCommas(int amount) {
        return String.format("$%,d", amount);
    }

    @Override
    public void onEffectDecompose() {
        super.onEffectDecompose();
        moneyTotals.replace(getPlayer().getUniqueId(), moneyTotals.get(getPlayer().getUniqueId()) - 5000000);
    }
}
