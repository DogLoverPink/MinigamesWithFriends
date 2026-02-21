package doglover.minigameswithfriends.wouldyourather.effects.beneficialeffects;

import doglover.minigameswithfriends.MinigamesWithFriends;
import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import io.papermc.paper.datacomponent.DataComponentTypes;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Pokeball extends WYREffect {


    static {
        WYREffectHandler.registerBeneficialWYREffect(Pokeball.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "Be the very best, like no one ever was";
    }

    public Pokeball(Player player) {
        super(player);
        setRepeatable(false);
        subscribeToEvent(ProjectileHitEvent.class);
        subscribeToEvent(ProjectileLaunchEvent.class);
    }

    @Override
    public void onEffectInitiate() {
        super.onEffectInitiate();
        getPlayer().give(getPokeball(3));
    }

    @Override
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        ItemStack item = getPlayer().getInventory().getItemInMainHand().getPersistentDataContainer().has(pokeOwnerKey) ?
                getPlayer().getInventory().getItemInMainHand() :
                getPlayer().getInventory().getItemInOffHand();
        if (!item.getPersistentDataContainer().has(pokeOwnerKey)) {
            return;
        }
        String ownerUUID = item.getPersistentDataContainer().get(pokeOwnerKey, PersistentDataType.STRING);
        if (ownerUUID == null || !ownerUUID.equals(getPlayer().getUniqueId().toString())) {
            return;
        }
        event.getEntity().getPersistentDataContainer().set(pokeOwnerKey, PersistentDataType.STRING, getPlayer().getUniqueId().toString());

        String storedUUID = item.getPersistentDataContainer().get(pokeStoredKey, PersistentDataType.STRING);
        if (storedUUID == null) {
            return;
        }
        event.getEntity().getPersistentDataContainer().set(pokeStoredKey, PersistentDataType.STRING, storedUUID);
    }

    final NamespacedKey pokeOwnerKey = new NamespacedKey(MinigamesWithFriends.getGamePlugin(), "pokeballOwner" + getUniqueNumber());
    final NamespacedKey pokeStoredKey = new NamespacedKey(MinigamesWithFriends.getGamePlugin(), "pokeballStored" + getUniqueNumber());

    @Override
    @SuppressWarnings("UnstableApiUsage")
    public void onProjectileHit(ProjectileHitEvent event) {
        getPlayer().sendMessage("trigger hit: " + event.getHitEntity() + ":" + event.getHitBlock());
        Projectile entity = event.getEntity();
        if (!entity.getPersistentDataContainer().has(pokeOwnerKey)) {
            return;
        }
        String ownerUUID = entity.getPersistentDataContainer().get(pokeOwnerKey, PersistentDataType.STRING);
        if (ownerUUID == null || !ownerUUID.equals(getPlayer().getUniqueId().toString())) {
            return;
        }
        String storedUUID = entity.getPersistentDataContainer().get(pokeStoredKey, PersistentDataType.STRING);
        if (storedUUID != null) {
            entity.getWorld().dropItem(entity.getLocation(), getPokeball(1));
            Entity entityToSpawn = pokeballEntities.remove(storedUUID);
            if (entityToSpawn != null) {
                entityToSpawn.copy(entity.getLocation());
            }
            return;
        }
        Entity hurtEntity = event.getHitEntity();
        if (hurtEntity != null && !hurtEntity.getType().equals(EntityType.PLAYER)) {
            pokeballEntities.put(hurtEntity.getUniqueId().toString(), hurtEntity);
            entity.getWorld().dropItem(entity.getLocation(), getFilledPokeball(1, hurtEntity));
            hurtEntity.remove();
            return;
        }
        entity.getWorld().dropItem(entity.getLocation(), getPokeball(1));
    }

    Map<String, Entity> pokeballEntities = new HashMap<>();

    MiniMessage mm = MiniMessage.miniMessage();


    Component propertyOfLabel = Component.text("Property of " + getPlayer().getName()).decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY);

    @SuppressWarnings("UnstableApiUsage")
    private ItemStack getFilledPokeball(int amount, Entity entity) {
        ItemStack stack = new ItemStack(Material.SNOWBALL, amount);
        stack.editPersistentDataContainer(pdc -> {
            pdc.set(pokeOwnerKey, PersistentDataType.STRING, getPlayer().getUniqueId().toString());
            pdc.set(pokeStoredKey, PersistentDataType.STRING, entity.getUniqueId().toString());
        });
        stack.editMeta(meta -> {
                    meta.displayName(mm.deserialize("<red>Poke</red>Ball <red>[</red>" + entity.getType().toString() + "<red>]</red>").decoration(TextDecoration.ITALIC, false));
                    meta.lore(List.of(propertyOfLabel));
                }
        );
        stack.setData(DataComponentTypes.ITEM_MODEL, Material.HEART_OF_THE_SEA.getKey());
        return stack;
    }

    @SuppressWarnings("UnstableApiUsage")
    private ItemStack getPokeball(int amount) {
        ItemStack stack = new ItemStack(Material.SNOWBALL, amount);
        stack.editPersistentDataContainer(pdc -> {
            pdc.set(pokeOwnerKey, PersistentDataType.STRING, getPlayer().getUniqueId().toString());
        });
        stack.editMeta(meta -> {
                    meta.displayName(mm.deserialize("<red>Poke</red>Ball").decoration(TextDecoration.ITALIC, false));
                    meta.lore(List.of(propertyOfLabel));
                }
        );
        stack.setData(DataComponentTypes.ITEM_MODEL, Material.HEART_OF_THE_SEA.getKey());
        return stack;
    }

    @Override
    public void onEffectDecompose() {
        super.onEffectDecompose();
    }
}
