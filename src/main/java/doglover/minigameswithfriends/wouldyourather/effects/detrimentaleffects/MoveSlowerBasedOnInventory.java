package doglover.minigameswithfriends.wouldyourather.effects.detrimentaleffects;

import doglover.minigameswithfriends.MinigamesWithFriends;
import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class MoveSlowerBasedOnInventory extends WYREffect {


    static {
        WYREffectHandler.registerDetrimentalWYREffect(MoveSlowerBasedOnInventory.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "You move slower based on how much's in your inventory";
    }

    public MoveSlowerBasedOnInventory(Player player) {
        super(player);
        setRepeatable(false);
    }

    NamespacedKey key = new NamespacedKey(MinigamesWithFriends.getGamePlugin(), "slow_from_inv"+getUniqueNumber());

    private void alterSpeed(double speedMod) {
        getPlayer().getAttribute(Attribute.MOVEMENT_SPEED).removeModifier(key);
        AttributeModifier mod = new AttributeModifier(key, speedMod, AttributeModifier.Operation.ADD_NUMBER);
        getPlayer().getAttribute(Attribute.MOVEMENT_SPEED).addModifier(mod);
    }

    private int getItemCount() {
        int amount = 0;
        for (ItemStack item : getPlayer().getInventory().getContents()) {
            if (item == null) {
                continue;
            }
            amount += item.getAmount();
        }
        return amount;
    }

    @Override
    public void on4HertzTick() {
        alterSpeed((getItemCount() / -2304.0) / 10.0);

    }

    @Override
    public void onEffectInitiate() {
        super.onEffectInitiate();
    }

    @Override
    public void onEffectDecompose() {
        super.onEffectDecompose();
        getPlayer().getAttribute(Attribute.MOVEMENT_SPEED).removeModifier(key);
    }
}
