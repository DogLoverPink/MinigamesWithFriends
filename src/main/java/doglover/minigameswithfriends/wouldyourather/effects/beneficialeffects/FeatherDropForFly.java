package doglover.minigameswithfriends.wouldyourather.effects.beneficialeffects;

import doglover.minigameswithfriends.utils.NumberUtils;
import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

public class FeatherDropForFly extends WYREffect {


    static {
        WYREffectHandler.registerBeneficialWYREffect(FeatherDropForFly.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "Be able to fly after dropping a feather";
    }

    public FeatherDropForFly(Player player) {
        super(player);
        setRepeatable(true);
        subscribeToEvent(PlayerDropItemEvent.class);
    }

    int flightTime = 0;

    @Override
    public void on4HertzTick() {
        if (flightTime > 0) {
            flightTime--;
            getPlayer().sendActionBar(Component.text("\uD83E\uDEB6"+getFlightTimeString()));
            if (flightTime <= 0) {
                player.setAllowFlight(false);
                player.setFlying(false);
                player.sendActionBar(Component.text(""));
            }
        }
    }

    private String getFlightTimeString() {
        if (flightTime > 240) {
            return NumberUtils.formatTimeStamp(flightTime * 5);
        }
        return String.valueOf(flightTime / 4);
    }

    @Override
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (!player.equals(getPlayer())) {
            return;
        }
        ItemStack item = event.getItemDrop().getItemStack();
        if (item.getType().equals(Material.FEATHER)) {
            flightTime += item.getAmount() * 10 * 4;
            player.setAllowFlight(true);
            player.setFlying(true);
            item.setAmount(0);
        }
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
