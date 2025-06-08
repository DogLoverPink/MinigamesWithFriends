package doglover.minigameswithfriends.wouldyourather.effects.beneficialeffects;

import doglover.minigameswithfriends.MinigamesWithFriends;
import doglover.minigameswithfriends.utils.ItemUtils;
import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import javax.naming.Name;
import java.util.List;

public class RodOfDiscord extends WYREffect {


    static {
        WYREffectHandler.registerBeneficialWYREffect(RodOfDiscord.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "Get a rod of discord";
    }

    public RodOfDiscord(Player player) {
        super(player);
        setRepeatable(true);
        subscribeToEvent(PlayerInteractEvent.class);
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!event.getPlayer().equals(getPlayer())) {
            return;
        }
        if (event.getItem() == null || event.getItem().getType() != Material.WOODEN_SHOVEL) {

            return;
        }
        if (!event.getItem().getPersistentDataContainer().has(key, PersistentDataType.LONG)) {
            return;
        }
        long timeLeft = event.getItem().getPersistentDataContainer().get(key, PersistentDataType.LONG).longValue() - System.currentTimeMillis();
        if (timeLeft > 0) {
            getPlayer().sendActionBar(Component.text("Rod Cooldown: " + timeLeft / 1000 + "s").color(NamedTextColor.RED));
            return;
        }
        getPlayer().setFallDistance(0);
        getPlayer().getWorld().playSound(getPlayer().getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
        getPlayer().teleport(getPlayer().getTargetBlockExact(50).getLocation());
        getPlayer().playSound(getPlayer().getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
        event.getItem().damage(6, event.getPlayer());
        applyCooldown(event.getItem());
        event.setCancelled(true);
    }

    NamespacedKey key = new NamespacedKey(MinigamesWithFriends.getGamePlugin(), "rod-of-discord" + getUniqueNumber());


    private void applyCooldown(ItemStack item) {
        item.editMeta(meta ->
                meta.getPersistentDataContainer().set(key, PersistentDataType.LONG, System.currentTimeMillis() + 5000L)
        );
    }

    @Override
    public void onEffectInitiate() {
        super.onEffectInitiate();
        ItemStack item = new ItemStack(Material.WOODEN_SHOVEL, 1);
        item.editMeta(meta -> {
                    meta.customName(Component.text("Rod of Discord").color(TextColor.fromHexString("#96FF0A")).decoration(TextDecoration.ITALIC, false));
                    meta.lore(List.of(
                            Component.text("Causes the chaos state").color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false),
                            Component.text("Teleports you to the position of the cursor").color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)
                    ));
                    meta.getPersistentDataContainer().set(key, PersistentDataType.LONG, System.currentTimeMillis());
                }
        );
        ItemUtils.giveItemsToPlayer(getPlayer(), item);
    }

    @Override
    public void onEffectDecompose() {
        super.onEffectDecompose();
    }
}
