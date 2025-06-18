package doglover.minigameswithfriends.wouldyourather.effects.detrimentaleffects;

import doglover.minigameswithfriends.utils.NumberUtils;
import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class HarderMobs extends WYREffect {


    static {
        WYREffectHandler.registerDetrimentalWYREffect(HarderMobs.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "Mobs around you become vastly harder sometimes";
    }

    public HarderMobs(Player player) {
        super(player);
        setRepeatable(true);
    }

    Set<UUID> checkedMobs = new HashSet<>();

    @Override
    public void on4HertzTick() {
        for (Entity potentialEntity : getPlayer().getNearbyEntities(20, 7, 20)) {
            if (!(potentialEntity instanceof Monster entity)) {
                continue;
            }
            if (checkedMobs.contains(entity.getUniqueId())) {
                continue;
            }
            checkedMobs.add(entity.getUniqueId());
            if (!NumberUtils.chanceOf(0.175)) {
                continue;
            }
            switch (entity) {
                case Ageable ageableEntity -> {
                    ageableEntity.setBaby();
                    ageableEntity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, -1, 1));
                    ageableEntity.getEquipment().setHelmet(new ItemStack(Material.JACK_O_LANTERN));
                }
                case Skeleton skeleton -> {
                    ItemStack mainHandItem = skeleton.getEquipment().getItemInMainHand();
                    mainHandItem.addUnsafeEnchantment(Enchantment.FLAME, 1);
                    mainHandItem.addUnsafeEnchantment(Enchantment.POWER, 2);
                    skeleton.getEquipment().setItemInMainHand(mainHandItem);
                    skeleton.getEquipment().setHelmet(new ItemStack(Material.JACK_O_LANTERN));
                }
                case Creeper creeper -> {
                    creeper.setPowered(true);
                    creeper.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, -1, 1));
                }
                default -> {
                    entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, -1, 1));
                    ItemStack helmet = new ItemStack(Material.JACK_O_LANTERN);
                    helmet.addUnsafeEnchantment(Enchantment.PROTECTION, 3);
                    entity.getEquipment().setHelmet(helmet);
                }
            }
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
