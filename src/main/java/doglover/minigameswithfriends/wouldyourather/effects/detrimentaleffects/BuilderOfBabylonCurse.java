package doglover.minigameswithfriends.wouldyourather.effects.detrimentaleffects;

import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Random;

public class BuilderOfBabylonCurse extends WYREffect {


    static {
        WYREffectHandler.registerDetrimentalWYREffect(BuilderOfBabylonCurse.class);

    }

    List<String> randomCharSets = List.of(
            "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ  ",
            "10 ",
            "АБВГДЕЖЗИЙКЛМНОПРУФЦЧШЪЫЮЯ  ",
            "  אבגדהוזחטיכלמנסעפצקרשת",
            "अआइईउऊएऐओऔअंअःकखगघचछजझटठडढतथदधनप  ",
            "我是的不了在人有他这",
            "あいうえおかきくけこさしすせそた",
            "ㅏㅑㅓㅕㅗㅛㅜㅠㅡㅣ  ",
            "กขฃคฅฆงจฉชซฌญฎฏฐฑฒณดตถทธนบ  "
    );



    private static final Random random = new Random();

    private String getRandomCharString() {
        String charset = randomCharSets.get(random.nextInt(randomCharSets.size()));
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < random.nextInt(3, 20); i++) {
            sb.append(charset.charAt(random.nextInt(charset.length())));
        }
        return sb.toString();
    }


    @Override
    public String getDescriptionBlurb() {
        return "You are cursed with the builder of babylon curse FINISH THIS EVENT";
    }

    @Override
    public void on4HertzTick() {
        for (ItemStack item : getPlayer().getInventory().getContents()) {
            if (item == null || item.getItemMeta().hasCustomName()) {
                continue;

            }
            item.editMeta(meta -> meta.customName(Component.text(getRandomCharString())));
        }
    }

    @Override
    public void onPlayerAttemptPickupItem(PlayerAttemptPickupItemEvent event) {
        if (event.getPlayer() != getPlayer()) {
            return;
        }
        ItemStack eventItem = event.getItem().getItemStack();
        for (ItemStack item : getPlayer().getInventory().getContents()) {
            if (item == null || eventItem.getType() != item.getType() || item.getAmount() == item.getMaxStackSize()) {
                continue;
            }
            int amountToAdd = Math.min(eventItem.getAmount(), item.getMaxStackSize() - item.getAmount());
            item.setAmount(item.getAmount() + amountToAdd);
            eventItem.setAmount(eventItem.getAmount() - amountToAdd);
            event.setCancelled(true);
        }

    }

    public BuilderOfBabylonCurse(Player player) {
        super(player);
        setRepeatable(false);
        subscribeToEvent(PlayerAttemptPickupItemEvent.class);
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
