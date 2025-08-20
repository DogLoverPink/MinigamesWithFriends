package doglover.minigameswithfriends.wouldyourather.effects.detrimentaleffects;

import doglover.minigameswithfriends.utils.NumberUtils;
import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PhasingItems extends WYREffect {


    static {
        WYREffectHandler.registerBeneficialWYREffect(PhasingItems.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "Your items sometimes intermittently phase out";
    }

    public PhasingItems(Player player) {
        super(player);
        setRepeatable(true);
    }

    private static class ItemWithCounter {
        ItemStack item;
        int counter;

        public ItemWithCounter(ItemStack item) {
            this.item = item;
            this.counter = NumberUtils.random.nextInt(40, 220);
        }

        boolean shouldReturn() {
            return --counter <= 0;
        }
    }

    List<ItemWithCounter> phasedOutItems = new ArrayList<>();

    @Override
    public void on4HertzTick() {
        for (int i = phasedOutItems.size() - 1; i >= 0; i--) {
            ItemWithCounter itemWithCounter = phasedOutItems.get(i);
            if (itemWithCounter.shouldReturn()) {
                getPlayer().getInventory().addItem(itemWithCounter.item);
                phasedOutItems.remove(i);
//                getPlayer().playSound(getPlayer().getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.5f, 1f);
            }
        }
        if (NumberUtils.chanceOf(0.01)) {
            ItemStack randomItemInInventory = getPlayer().getInventory().getContents()[(int) (Math.random() * getPlayer().getInventory().getContents().length)];
            if (randomItemInInventory == null) {
                return;
            }
            phasedOutItems.add(new ItemWithCounter(randomItemInInventory));
            getPlayer().getInventory().remove(randomItemInInventory);
//            getPlayer().playSound(getPlayer().getLocation(), Sound.BLOCK_CHEST_OPEN, 0.4f, 1f);
        }
    }

    @Override
    public void onEffectInitiate() {
        super.onEffectInitiate();
    }

    @Override
    public void onEffectDecompose() {
        for (ItemWithCounter itemWithCounter : phasedOutItems) {
            getPlayer().getInventory().addItem(itemWithCounter.item);
        }
        super.onEffectDecompose();
    }
}
