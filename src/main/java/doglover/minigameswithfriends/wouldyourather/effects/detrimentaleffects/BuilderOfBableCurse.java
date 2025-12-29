package doglover.minigameswithfriends.wouldyourather.effects.detrimentaleffects;

import doglover.minigameswithfriends.utils.NumberUtils;
import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class BuilderOfBableCurse extends WYREffect {


    static {
        WYREffectHandler.registerDetrimentalWYREffect(BuilderOfBableCurse.class);

    }

    List<String> randomCharSets = List.of(
            "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ  ",
            "1111111100000000 ",
            "АБВГДЕЖЗИЙКЛМНОПРУФЦЧШЪЫЮЯ  ",
            "  אבגדהוזחטיכלמנסעפצקרשת",
            "अआइईउऊएऐओऔअंअःकखगघचछजझटठडढतथदधनप  ",
            "我是的不了在人有他这",
            "あいうえおかきくけこさしすせそた",
            "ㅏㅑㅓㅕㅗㅛㅜㅠㅡㅣ  ",
            "กขฃคฅฆงจฉชซฌญฎฏฐฑฒณดตถทธนบ  "
    );


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
        return "You are cursed with the builder of bable curse";
    }

    @Override
    public void on4HertzTick() {
        for (ItemStack item : getPlayer().getInventory().getContents()) {
            renameItem(item);
        }
        if (NumberUtils.chanceOf(0.005)) {
            sendCurseMessage();
            itemMappings.clear();
        }
    }

    private void renameItem(ItemStack item) {
        if (item == null || item.getItemMeta().hasCustomName()) {
            return;
        }
        itemMappings.putIfAbsent(item.getType(), getRandomCharString());
        item.editMeta(meta -> meta.customName(Component.text(itemMappings.get(item.getType()))));
    }

    private void sendCurseMessage() {
        getPlayer().sendMessage(Component.text("rawr :3 :3 XD >w<").decoration(TextDecoration.OBFUSCATED, true));
    }

    Map<Material, String> itemMappings = new HashMap<>();

    @Override
    public void onPlayerAttemptPickupItem(PlayerAttemptPickupItemEvent event) {
        if (event.getPlayer() != getPlayer()) {
            return;
        }
        renameItem(event.getItem().getItemStack());
    }

    public BuilderOfBableCurse(Player player) {
        super(player);
        setRepeatable(false);
        subscribeToEvent(PlayerAttemptPickupItemEvent.class);
    }

    @Override
    public void onEffectInitiate() {
        super.onEffectInitiate();
        sendCurseMessage();
    }

    @Override
    public void onEffectDecompose() {
        super.onEffectDecompose();
    }
}
