package doglover.minigameswithfriends.wouldyourather.effects.detrimentaleffects;

import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;

public class TemuTools extends WYREffect {


    static {
        WYREffectHandler.registerBeneficialWYREffect(TemuTools.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "You bought your tools from Temu";
    }

    public TemuTools(Player player) {
        super(player);
        setRepeatable(false);
        subscribeToEvent(PlayerItemDamageEvent.class);
    }

    @Override
    public void onPlayerItemDamage(PlayerItemDamageEvent event) {
        if (!event.getPlayer().equals(getPlayer())) {
            return;
        }
        event.setDamage(event.getDamage() * random.nextInt(5) + 1);
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
