package doglover.minigameswithfriends.wouldyourather.effects.detrimentaleffects;

import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PainfulSeeds extends WYREffect {


    static {
        WYREffectHandler.registerDetrimentalWYREffect(PainfulSeeds.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "Having seeds in your inventory deeply pains you";
    }

    public PainfulSeeds(Player player) {
        super(player);
        setRepeatable(false);
    }

    Material[] seeds = {Material.WHEAT_SEEDS, Material.MELON_SEEDS, Material.PUMPKIN_SEEDS, Material.BEETROOT_SEEDS, Material.TORCHFLOWER_SEEDS};

    int debounce = 0;

    @Override
    public void on4HertzTick() {
        for (Material seed : seeds) {
            if (getPlayer().getInventory().contains(seed)) {
                getPlayer().damage(1.5);
                if (debounce % 2 == 0) {
                    getPlayer().playSound(getPlayer(), Sound.ENTITY_ALLAY_HURT, 1, 1);
                }
                if (debounce > 0) {
                    debounce--;
                } else {
                    getPlayer().sendMessage(MiniMessage.miniMessage().deserialize("<dark_red><b>NO SEEDS! >:(</b></dark_red>"));
                    debounce = 8;
                }
                return;
            }
        }
        debounce = 0;
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
