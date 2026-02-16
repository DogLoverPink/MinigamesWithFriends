package doglover.minigameswithfriends.wouldyourather.effects.beneficialeffects;

import doglover.minigameswithfriends.utils.ItemUtils;
import doglover.minigameswithfriends.utils.PlayerUtils;
import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class Telekinesis extends WYREffect {


    static {
        WYREffectHandler.registerBeneficialWYREffect(Telekinesis.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "Have mined blocks be automatically collected";
    }

    public Telekinesis(Player player) {
        super(player);
        setRepeatable(false);
        subscribeToEvent(BlockDropItemEvent.class, EventPriority.LOW);
    }

    @Override
    public void onBlockDropItem(BlockDropItemEvent event) {
        if (!event.getPlayer().equals(getPlayer())) {
            return;
        }
        event.setCancelled(true);
        for (Item itemEntity : event.getItems()) {
            ItemUtils.giveItemsToPlayer(getPlayer(), itemEntity.getItemStack());
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
