package doglover.minigameswithfriends.wouldyourather.effects.beneficialeffects;

import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class RandomFishing extends WYREffect {


    static {
        WYREffectHandler.registerBeneficialWYREffect(RandomFishing.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "Fish faster and have fishing be randomized";
    }

    public RandomFishing(Player player) {
        super(player);
        setRepeatable(false);
        subscribeToEvent(PlayerFishEvent.class);
    }

    @Override
    public void onPlayerFish(PlayerFishEvent event) {
        if (!event.getPlayer().equals(getPlayer())) {
            return;
        }
        if (event.getState().equals(PlayerFishEvent.State.FISHING)) {
            event.getHook().setMaxWaitTime((int) (event.getHook().getMaxWaitTime() * 0.66));
        }
        if (event.getState().equals(PlayerFishEvent.State.CAUGHT_FISH)) {
            if (!(event.getCaught() instanceof Item item)) {
                return;
            }
            List<Material> itemMats = Arrays.stream(Material.values())
                    .filter(m -> m != Material.AIR && m.isItem())
                    .toList();
            Material randomMat = itemMats.get(random.nextInt(itemMats.size()));
            int amount = random.nextInt(randomMat.getMaxStackSize()) + 1;
            item.setItemStack(new ItemStack(randomMat, amount));
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
