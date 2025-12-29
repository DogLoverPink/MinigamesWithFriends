package doglover.minigameswithfriends.wouldyourather.effects.detrimentaleffects;

import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class SCP018 extends WYREffect {


    static {
        WYREffectHandler.registerBeneficialWYREffect(SCP018.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "SCP-018 is released at your location";
    }

    Snowball scp018;

    public SCP018(Player player) {
        super(player);
        setRepeatable(true);
        subscribeToEvent(ProjectileHitEvent.class);
    }

    @Override
    public void onEffectInitiate() {
        super.onEffectInitiate();
        scp018 = getPlayer().getWorld().spawn(getPlayer().getLocation().clone().add(0, 5, 0), Snowball.class);
        scp018.setItem(ItemStack.of(Material.RED_BUNDLE));

    }

    @Override
    public void onEffectDecompose() {
        super.onEffectDecompose();
    }
}
