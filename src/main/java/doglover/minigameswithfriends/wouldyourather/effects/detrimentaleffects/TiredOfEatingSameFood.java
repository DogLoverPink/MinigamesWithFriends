package doglover.minigameswithfriends.wouldyourather.effects.detrimentaleffects;

import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class TiredOfEatingSameFood extends WYREffect {

    static {
        WYREffectHandler.registerDetrimentalWYREffect(TiredOfEatingSameFood.class);
    }

    private final Map<Material, Double> foodConsumptionCount = new HashMap<>();

    private static final double BASE_MULTIPLIER = 1.0;
    private static final double MULTIPLIER_DECREASE = 0.2;
    private static final double MIN_MULTIPLIER = 0.2;

    @Override
    public String getDescriptionBlurb() {
        return "You get tired of eating the same food repeatedly";
    }

    @Override
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (!event.getEntity().equals(getPlayer())) {
            return;
        }

        if (event.getFoodLevel() <= getPlayer().getFoodLevel()) {
            return;
        }

        ItemStack item = event.getItem();
        if (item == null || !item.getType().isEdible()) {
            return;
        }

        Material foodType = item.getType();

        for (Material food : foodConsumptionCount.keySet()) {
            if (foodType.equals(food)) {
                continue;
            }
            double newValue = foodConsumptionCount.get(food) - 1.6;
            newValue = Math.max(0, newValue);
            foodConsumptionCount.put(food, newValue);
        }


        double count = foodConsumptionCount.getOrDefault(foodType, 0.0) + 1.0;
        foodConsumptionCount.put(foodType, count);

        double multiplier = Math.max(BASE_MULTIPLIER - (MULTIPLIER_DECREASE * (count - 1.0)), MIN_MULTIPLIER);

        int originalIncrease = event.getFoodLevel() - getPlayer().getFoodLevel();

        int reducedIncrease = (int) Math.ceil(originalIncrease * multiplier);

        event.setFoodLevel(getPlayer().getFoodLevel() + reducedIncrease);

        int percentage = (int) (multiplier * 100);
        if (multiplier <= MIN_MULTIPLIER) {
            getPlayer().sendMessage(Component.text("Yucky! (" + percentage + "% effective)")
                    .color(NamedTextColor.RED));
        } else {
            getPlayer().sendMessage(Component.text("Meh (" + percentage + "% effective)")
                    .color(NamedTextColor.YELLOW));
        }
    }

    public TiredOfEatingSameFood(Player player) {
        super(player);
        setRepeatable(false);
        subscribeToEvent(FoodLevelChangeEvent.class);
    }

    @Override
    public void onEffectInitiate() {
        super.onEffectInitiate();
    }

    @Override
    public void onEffectDecompose() {
        super.onEffectDecompose();
        foodConsumptionCount.clear();
    }
}