package doglover.minigameswithfriends.wouldyourather.effects.beneficialeffects;

import doglover.minigameswithfriends.MinigamesWithFriends;
import doglover.minigameswithfriends.utils.TextUtils;
import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MoreHealthForEachHoe extends WYREffect {


    static {
        WYREffectHandler.registerBeneficialWYREffect(MoreHealthForEachHoe.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "Gain more max health for every " + TextUtils.formatMaterialName(targetMaterial) + " you have";
    }

    public MoreHealthForEachHoe(Player player) {
        super(player);
        setRepeatable(true);
        List<Material> materials = new ArrayList<>(HEALTH_GAIN.keySet());
        targetMaterial = materials.get(random.nextInt(materials.size()));
    }

    private final Material targetMaterial;
    private static final Map<Material, Double> HEALTH_GAIN = Map.of(
            Material.WOODEN_HOE, 0.5,
            Material.STONE_HOE, 0.75,
            Material.IRON_HOE, 1.0,
            Material.GOLDEN_HOE, 1.0
    );

    @Override
    public void on4HertzTick() {
        int hoeCount = getPlayer().getInventory().all(targetMaterial).size();
        removeModifier();
        addModifier(hoeCount * HEALTH_GAIN.getOrDefault(targetMaterial, 0.0));
    }

    NamespacedKey key = new NamespacedKey(MinigamesWithFriends.getGamePlugin(), "hoe_health" + getUniqueNumber());


    private void removeModifier() {
        getPlayer().getAttribute(Attribute.MAX_HEALTH).removeModifier(key);
    }

    private void addModifier(double amount) {
        AttributeModifier mod = new AttributeModifier(key, amount, AttributeModifier.Operation.ADD_NUMBER);
        getPlayer().getAttribute(Attribute.MAX_HEALTH).addModifier(mod);
    }

    @Override
    public void onEffectInitiate() {
        super.onEffectInitiate();
    }

    @Override
    public void onEffectDecompose() {
        super.onEffectDecompose();
        getPlayer().getAttribute(Attribute.MAX_HEALTH).removeModifier(key);
    }
}
