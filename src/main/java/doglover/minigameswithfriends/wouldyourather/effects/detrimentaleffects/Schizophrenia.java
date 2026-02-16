package doglover.minigameswithfriends.wouldyourather.effects.detrimentaleffects;

import doglover.minigameswithfriends.utils.BlockUtils;
import doglover.minigameswithfriends.utils.NumberUtils;
import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.apache.commons.lang3.text.StrBuilder;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.*;

public class Schizophrenia extends WYREffect {


    static {
        WYREffectHandler.registerDetrimentalWYREffect(Schizophrenia.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "You have schizophrenia";
    }

    public Schizophrenia(Player player) {
        super(player);
        setRepeatable(false);
        subscribeToEvent(PlayerInteractEvent.class);
    }

    int isReadyForDiamondTroll = random.nextInt(480, 1200);
    Location fakeOreLocation = null;


    @Override
    public void on4HertzTick() {
        if (NumberUtils.chanceOf(0.0025)) {
            playFakeEntitySound();
        }
        if (NumberUtils.chanceOf(0.000325)) {
            doFakeJoinMessage();
        }
        if (NumberUtils.chanceOf(0.000325)) {
            doFakeChatMessage();
        }
        Block target = getPlayer().getTargetBlock(null, 30);
        if (isReadyForDiamondTroll-- <= 0 && (target.getType() == Material.STONE || target.getType() == Material.DEEPSLATE)) {
            if (target.getLocation().distanceSquared(getPlayer().getLocation()) > 37) {
                sendFakeDiamonds(target);
            }
        }
        if (fakeOreLocation != null && !getPlayer().getWorld().equals(fakeOreLocation.getWorld())) {
            clearFakeDiamonds();
        }
        if (fakeOreLocation != null && fakeOreLocation.distanceSquared(getPlayer().getLocation()) < 36) {
            clearFakeDiamonds();
        }
    }


    private void clearFakeDiamonds() {
        List<Location> stoneLocs = BlockUtils.getNearbyNonAirBlocks(fakeOreLocation, 0.5);
        stoneLocs.add(fakeOreLocation);
        for (Location loc : stoneLocs) {
            BlockData data = loc.getWorld().getBlockData(loc);
            getPlayer().sendBlockChange(loc, data);
        }
        fakeOreLocation = null;
    }

    private void sendFakeDiamonds(Block target) {
        fakeOreLocation = target.getLocation();
        isReadyForDiamondTroll = random.nextInt(480, 1000);
        List<Location> stoneLocs = BlockUtils.getNearbyNonAirBlocksOfType(target.getLocation(), target.getType(), 0.5);
        stoneLocs.add(fakeOreLocation);

        for (Location loc : stoneLocs) {
            getPlayer().sendBlockChange(loc, getMostApplicableTrollOre(target.getType()).createBlockData());
        }
    }

    private final Map<Material, Integer> pointsPerMat = Map.ofEntries(
            Map.entry(Material.IRON_INGOT, 1),
            Map.entry(Material.SHIELD, 1),
            Map.entry(Material.WATER_BUCKET, 3),
            Map.entry(Material.IRON_SWORD, 2),
            Map.entry(Material.IRON_PICKAXE, 4),
            Map.entry(Material.IRON_SHOVEL, 1),
            Map.entry(Material.IRON_AXE, 3),
            Map.entry(Material.RAW_IRON, 1),
            Map.entry(Material.RAW_IRON_BLOCK, 9),
            Map.entry(Material.IRON_BLOCK, 9),
            Map.entry(Material.IRON_CHESTPLATE, 2),
            Map.entry(Material.IRON_LEGGINGS, 2),
            Map.entry(Material.IRON_HELMET, 2),
            Map.entry(Material.IRON_BOOTS, 2)
    );


    private final List<String> fakeNames = List.of(
            "Lazten",
            "Korvyn_",
            "Tarnq_",
            "JorvexYT",
            "Zyflair",
            "Cr4bby",
            "HardCatTTV",
            "itsS1mple",
            "LeafMatters221",
            "dorinthemorin",
            "4NTHRAX"
    );


    private void doFakeJoinMessage() {
        List<OfflinePlayer> players = new ArrayList<>(Arrays.asList(Bukkit.getOfflinePlayers()));
        players.removeAll(Bukkit.getOnlinePlayers());
        String fakeName;
        if (players.isEmpty()) {
            fakeName = fakeNames.get(random.nextInt(fakeNames.size()));
        } else {
            fakeName = players.get(random.nextInt(players.size())).getName();
        }
        Component fakeMessage = Component.text(fakeName+" joined the game").color(NamedTextColor.YELLOW);
        getPlayer().sendMessage(fakeMessage);

    }


    private int getIronCount() {
        int score = 0;
        for (ItemStack item : getPlayer().getInventory().getContents()) {
            if (item == null || item.isEmpty()) {
                continue;
            }
            score += pointsPerMat.getOrDefault(item.getType(), 0) * item.getAmount();
        }
        return score;
    }

    private Material getMostApplicableTrollOre(Material targetType) {
        if (getPlayer().getLocation().getY() < 30) {
            return targetType == Material.DEEPSLATE_DIAMOND_ORE ? Material.DEEPSLATE_DIAMOND_ORE : Material.DIAMOND_ORE;
        }
        if (getIronCount() <= 6) {
            return targetType == Material.DEEPSLATE_DIAMOND_ORE ? Material.DEEPSLATE_IRON_ORE : Material.IRON_ORE;
        }
        return targetType == Material.DEEPSLATE_DIAMOND_ORE ? Material.DEEPSLATE_DIAMOND_ORE : Material.DIAMOND_ORE;
    }


    private void playFakeEntitySound() {
        int rand = random.nextInt(0, 3);
        if (rand == 0) {
            getPlayer().playSound(getBlockBehindPlayer(), Sound.ENTITY_CREEPER_PRIMED, 1, 1);
        } else {
            Sound hurtSound = rand == 1 ? Sound.ENTITY_ZOMBIE_AMBIENT : Sound.ENTITY_SPIDER_AMBIENT;
            Location loc = getBlockBehindPlayer();
            getPlayer().playSound(loc, hurtSound, 1, 1);
            getPlayer().damage(0.0001);
            Vector launchLoc = getPlayer().getLocation().getDirection().multiply(0.4);
            launchLoc.setY(0.25);
            getPlayer().setVelocity(launchLoc);
        }
    }

    private Location getBlockBehindPlayer() {
        Vector inverseDirectionVec = getPlayer().getLocation().getDirection().normalize().multiply(-1);
        return getPlayer().getLocation().add(inverseDirectionVec);
    }

    private static final List<String> messages = List.of(
            "yo {NAME} do you have any wood on you?",
            "hey {NAME} I just died near spawn, can you grab my stuff?",
            "look behind you {NAME}",
            "guys I think {NAME} is x-raying",
            "anyone got food? I’m at 1 heart",
            "how rude {NAME}",
            "{NAME} did you just steal my furnace?",
            "hehe, {NAME}'s never gonna see this coming",
            "Don't believe everything that you see {NAME}"
    );


    private void doFakeChatMessage() {
        List<Player> applicablePlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
        if (applicablePlayers.isEmpty()) {
            getPlayer().sendMessage("womp womp (debug message)");
            return;
        }
        String randomName = applicablePlayers.get(random.nextInt(applicablePlayers.size())).getName();
        String casualPlayerName = extractCasualFormOfUsername(getPlayer().getName());
        String message = messages.get(random.nextInt(messages.size())).replace("{NAME}", casualPlayerName);
        message = "<"+randomName+"> "+message;
        getPlayer().sendMessage(Component.text(message));
    }


    @Override
    public void onEffectInitiate() {
        super.onEffectInitiate();
        isReadyForDiamondTroll = 20;
    }

    @Override
    public void onEffectDecompose() {
        if (fakeOreLocation != null) {
            clearFakeDiamonds();
        }
        super.onEffectDecompose();
    }




    public static String extractCasualFormOfUsername(String input) {
        StringBuilder result = new StringBuilder();
        boolean started = false;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (!started && Character.isLetter(c)) {
                result.append(c);
                started = true;
            } else if (started) {
                if (!Character.isLetter(c) && result.length() <= 3) {
                    result.setLength(0);
                    continue;
                }
                if ((Character.isUpperCase(c) || (!Character.isLetter(c))) && result.length() > 3) {
                    break;
                }
                result.append(c);
            }
        }
        String finalResult = result.toString();
        if (finalResult.isBlank()) {
            finalResult = input;
        }

        return finalResult.toLowerCase();
    }
}
