package doglover.minigameswithfriends.gamemodes;

import doglover.minigameswithfriends.MinigamesWithFriends;
import doglover.minigameswithfriends.commands.CommandHandler;
import doglover.minigameswithfriends.utils.JarUtils;
import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickCallback;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

import static doglover.minigameswithfriends.commands.BuiltInCommandDefinitions.filterByStartsWith;

public class WouldYouRatherGamemode extends TimeEventBasedGamemode {

    static {
        CommandHandler.registerCommand("WouldYouRather",
                WouldYouRatherGamemode::handleWouldYouRatherCommand,
                WouldYouRatherGamemode::handleWouldYouRatherCompletions);

    }

    public WouldYouRatherGamemode() {
        subscribeToEvent(PlayerDropItemEvent.class);
    }

    /**
     * Whether the Would You Rather gamemode is currently live (game running and gamemode enabled).
     * Replaces the old {@code WYREventHandler.isActive()} check.
     */
    private static boolean isWouldYouRatherActive() {
        return MinigamesWithFriends.getGame().isRunning()
                && MinigamesWithFriends.getGame().isGamemodeActive(WouldYouRatherGamemode.class);
    }


    private static void handleWouldYouRatherCommand(CommandSender commandSender, String[] args) {
        if (args.length == 1) {
            commandSender.sendMessage("§cPlease specify a subcommand.");
            return;
        }
        if (args[1].equalsIgnoreCase("RemoveEffect")) {
            if (args.length < 4) {
                commandSender.sendMessage("§cInvalid usage! Do §e/mg wouldyourather removeEffect <player> <effect>");
                return;
            }
            Player plr = Bukkit.getPlayer(args[2]);
            if (plr == null) {
                commandSender.sendMessage("§cUnknown player §e" + args[2]);
                return;
            }
            String effectToRemove = args[3];
            Iterator<WYREffect> effectIterator = WYREffectHandler.getManagedEffects().iterator();
            while (effectIterator.hasNext()) {
                WYREffect effect = effectIterator.next();
                if (effect.getPlayer().equals(plr) && effect.getClass().getSimpleName().equalsIgnoreCase(effectToRemove)) {
                    effect.selfDestruct();
                    commandSender.sendMessage("§aRemoved §e" + effectToRemove + "§a from §e" + plr.getName());
                    effectIterator.remove();
                    return;
                }
            }
            commandSender.sendMessage("§cEffect not found!");
        }
        if (args[1].equalsIgnoreCase("CheckActiveEffects")) {
            Player plr;
            if (args.length < 3 && commandSender instanceof Player) {
                plr = (Player) commandSender;
            } else if (args.length >= 3) {
                plr = Bukkit.getPlayer(args[2]);
                if (plr == null) {
                    commandSender.sendMessage("§cUnknown player §e" + args[2]);
                    return;
                }
            } else {
                commandSender.sendMessage("§cUnknown player");
                return;
            }
            if (!isWouldYouRatherActive() || WYREffectHandler.getManagedEffects().isEmpty()) {
                commandSender.sendMessage("§e" + plr.getName() + "§c has no active effects.");
                return;
            }
            commandSender.sendMessage("§eActive effects on §b" + plr.getName() + "§e:");
            for (WYREffect effect : WYREffectHandler.getManagedEffects()) {
                if (effect.getPlayer().equals(plr)) {
                    NamedTextColor color = WYREffectHandler.isEffectBeneficial(effect) ? NamedTextColor.GREEN : NamedTextColor.RED;
                    TextComponent effectComponent = Component.text(" - ").color(NamedTextColor.YELLOW);
                    effectComponent = effectComponent.append(Component.text(effect.getClass().getSimpleName()).color(color));
                    effectComponent = effectComponent.hoverEvent(Component.text(effect.getDescriptionBlurb()).color(color));
                    commandSender.sendMessage(effectComponent);
                }
            }
        }
        if (args[1].equalsIgnoreCase("SendNewPrompt")) {
            if (!isWouldYouRatherActive()) {
                commandSender.sendMessage("§cThe Would You Rather gamemode is not active!");
                return;
            }
            MinigamesWithFriends.getGame().getGamemode(WouldYouRatherGamemode.class).onTimeEventTrigger();
        }
    }

    private static List<String> handleWouldYouRatherCompletions(String[] args) {
        if (args.length == 2) {
            return filterByStartsWith(List.of("RemoveEffect", "SendNewPrompt", "CheckActiveEffects"), args[1]);
        }
        if (args[1].equalsIgnoreCase("RemoveEffect")) {
            if (args.length == 3) {
                return filterByStartsWith(Bukkit.getOnlinePlayers().stream().map(Player::getName).toList(), args[2]);
            }
            if (args.length == 4) {
                Player plr = Bukkit.getPlayer(args[2]);
                if (!isWouldYouRatherActive() || plr == null) {
                    return List.of();
                }
                return filterByStartsWith(WYREffectHandler.getManagedEffects().stream()
                        .filter(wyrEffect -> wyrEffect.getPlayer().equals(plr))
                        .map(wyrEffect -> wyrEffect.getClass().getSimpleName())
                        .toList(), args[3]);
            }
        }
        if (args[1].equalsIgnoreCase("CheckActiveEffects")) {
            return filterByStartsWith(Bukkit.getOnlinePlayers().stream().map(Player::getName).toList(), args[2]);
        }
        return List.of();
    }
    public final Map<UUID, List<Class<? extends WYREffect>>> currentlyAppliedBenefitsAndDetriments = new HashMap<>();


    public static void initialize() {
        JarUtils.initalizeClassesInPackage("doglover.minigameswithfriends.wouldyourather.effects.beneficialeffects");
        JarUtils.initalizeClassesInPackage("doglover.minigameswithfriends.wouldyourather.effects.detrimentaleffects");
        JarUtils.initalizeClassesInPackage("doglover.minigameswithfriends.wouldyourather.effects.simple");
        WYREffectHandler.sendEffectCounts();
    }

    public boolean playerHasEffect(Player plr, Class<? extends WYREffect> effectClass) {
        return currentlyAppliedBenefitsAndDetriments.get(plr.getUniqueId()).contains(effectClass);
    }


    public void removeEffectFromPlayer(Player plr, Class<? extends WYREffect> effectClass) {
        currentlyAppliedBenefitsAndDetriments.get(plr.getUniqueId()).remove(effectClass);
    }

    @Override
    public void tick() {
        if (!getGame().isInDeathMatch()) {
            super.tick();
        }
        if (!effectsToChooseFrom.isEmpty()) {
            getGame().addScoreboardContribution("§bTime to make choice: §d" + getFormattedTimeRemaining());
        }
        WYREffectHandler.tick();

    }

    private void chooseOptionOne(Audience aud) {
        UUID uuid = aud.get(Identity.UUID).get();
        List<WYREffect> effects = effectsToChooseFrom.get(uuid);
        if (effects == null || effects.isEmpty() || Bukkit.getPlayer(uuid) == null || !getGame().isRunning()) {
            return;
        }
        applyEffects(Bukkit.getPlayer(uuid), effects.get(0), effects.get(1));
    }


    private void chooseOptionTwo(Audience aud) {
        UUID uuid = aud.get(Identity.UUID).get();
        List<WYREffect> effects = effectsToChooseFrom.get(uuid);
        if (effects == null || effects.isEmpty() || Bukkit.getPlayer(uuid) == null || !getGame().isRunning()) {
            return;
        }
        applyEffects(Bukkit.getPlayer(uuid), effects.get(2), effects.get(3));
    }

    private void applyEffects(Player plr, WYREffect goodEffect, WYREffect badEffect) {
        if (!goodEffect.isRepeatable()) {
            currentlyAppliedBenefitsAndDetriments.get(plr.getUniqueId()).add(goodEffect.getClass());
        }
        if (!badEffect.isRepeatable()) {
            currentlyAppliedBenefitsAndDetriments.get(plr.getUniqueId()).add(badEffect.getClass());
        }
        plr.sendMessage(Component.text("You chose!"));
        if (getGame().getConfig().getWouldYouRatherConfig().shouldApplyDamageImmunityDuringChoiceSelection()) {
            plr.removePotionEffect(PotionEffectType.RESISTANCE);
        }
        if (getGame().getConfig().getWouldYouRatherConfig().shouldPreventMovingDuringChoiceSelection()) {
            plr.getAttribute(Attribute.MOVEMENT_SPEED).removeModifier(preventMovingKey);
            plr.getAttribute(Attribute.JUMP_STRENGTH).removeModifier(preventJumpingKey);
        }
        goodEffect.onEffectInitiate();
        badEffect.onEffectInitiate();
        effectsToChooseFrom.remove(plr.getUniqueId());
        if (effectsToChooseFrom.isEmpty()) {
            endChoiceChooseTimer();
        }

    }

    boolean isCurrentChoosing = false;

    public boolean isCurrentChoosing() {
        return isCurrentChoosing;
    }

    public boolean isPlayerCurrentlyChoosing(Player plr) {
        return effectsToChooseFrom.containsKey(plr.getUniqueId());
    }

    public Map<UUID, List<WYREffect>> effectsToChooseFrom = new HashMap<>();

    NamespacedKey preventMovingKey = new NamespacedKey(MinigamesWithFriends.getGamePlugin(), "WYRPreventMoving");
    NamespacedKey preventJumpingKey = new NamespacedKey(MinigamesWithFriends.getGamePlugin(), "WYRPreventJumping");

    @Override
    public void onTimeEventTrigger() {
        isCurrentChoosing = true;
        for (Player plr : getGame().getPlayers()) {
            if (getGame().getConfig().getWouldYouRatherConfig().shouldApplyDamageImmunityDuringChoiceSelection()) {
                plr.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, -1, 4));
            }
            if (getGame().getConfig().getWouldYouRatherConfig().shouldPreventMovingDuringChoiceSelection()) {
                if (plr.getAttribute(Attribute.MOVEMENT_SPEED).getModifier(preventMovingKey) == null
                        && plr.getAttribute(Attribute.JUMP_STRENGTH).getModifier(preventJumpingKey) == null) {
                    plr.getAttribute(Attribute.MOVEMENT_SPEED).addModifier(new AttributeModifier(preventMovingKey, -1, AttributeModifier.Operation.MULTIPLY_SCALAR_1));
                    plr.getAttribute(Attribute.JUMP_STRENGTH).addModifier(new AttributeModifier(preventJumpingKey, -1, AttributeModifier.Operation.MULTIPLY_SCALAR_1));
                }
            }
            List<Class<? extends WYREffect>> current = currentlyAppliedBenefitsAndDetriments.get(plr.getUniqueId());
            effectsToChooseFrom.putIfAbsent(plr.getUniqueId(), new ArrayList<>());
            WYREffect good1 = WYREffectHandler.getRandomBeneficialWYREffectExceptFor(plr, current);
            WYREffect bad1 = WYREffectHandler.getRandomDetrimentialWYREffectExceptFor(plr, current);
            WYREffect good2 = WYREffectHandler.getRandomBeneficialWYREffectExceptFor(plr, current);
            WYREffect bad2 = WYREffectHandler.getRandomDetrimentialWYREffectExceptFor(plr, current);
            sendWYRIntro(plr);
            sendWYREffectChoice(plr, good1, bad1, 1);
            plr.sendMessage(Component.empty());
            plr.sendMessage(Component.text("OR").appendNewline().color(NamedTextColor.YELLOW));
            sendWYREffectChoice(plr, good2, bad2, 2);
            sendWYREnd(plr);
            effectsToChooseFrom.get(plr.getUniqueId()).addAll(Arrays.asList(good1, bad1, good2, bad2));
        }
        runChoiceChooseTimer();
    }

    private void runChoiceChooseTimer() {
        setTickGoal(20 * getGame().getConfig().getWouldYouRatherConfig().getAllocatedSecondsForChoosingOption());
        new BukkitRunnable() {
            @Override
            public void run() {
                if (effectsToChooseFrom.isEmpty()) {
                    cancel();
                }
                if (getTickGoal() > 3) {
                    return;
                }
                endChoiceChooseTimer();
                cancel();
            }
        }.runTaskTimer(MinigamesWithFriends.getGamePlugin(), 0, 1);
    }

    private void endChoiceChooseTimer() {
        setTickGoal(getNextComputedTime());
        List<UUID> uuids = new ArrayList<>(effectsToChooseFrom.keySet());
        for (UUID uuid : uuids) {
            Player plr = Bukkit.getPlayer(uuid);
            plr.closeInventory();
            plr.sendMessage(Component.text("You ran out of time! Automatically choosing first option... ").color(NamedTextColor.RED));
            chooseOptionOne(plr);
        }
        for (Player plr : getGame().getPlayers()) {
            plr.removePotionEffect(PotionEffectType.RESISTANCE);
        }
        isCurrentChoosing = false;
    }

    private void sendWYRIntro(Player plr) {
        Component message = Component.text("-------------------------").color(NamedTextColor.YELLOW).appendNewline();
        message = message.append(Component.text("Would you rather: ").color(NamedTextColor.AQUA));
        plr.sendMessage(message);
    }

    private void sendWYREnd(Player plr) {
        Component message = Component.text("-------------------------").color(NamedTextColor.YELLOW);
        plr.sendMessage(message);
    }


    private void sendWYREffectChoice(Player plr, WYREffect goodEffect, WYREffect badEffect, int option1or2) {
        ClickCallback<Audience> callback = option1or2 == 1 ? this::chooseOptionOne : this::chooseOptionTwo;
        Component message = Component.text(" " + goodEffect.getDescriptionBlurb()).color(NamedTextColor.GREEN).appendNewline();
        message = message.append(Component.text("BUT...").color(NamedTextColor.YELLOW).appendNewline());
        message = message.append(Component.text(badEffect.getDescriptionBlurb()).color(NamedTextColor.RED)).appendNewline();
        message = message.append(
                Component.text("[SELECT]")
                        .clickEvent(ClickEvent.callback(callback))
                        .hoverEvent(Component.text("Choose option " + option1or2).color(NamedTextColor.GREEN)));
        plr.sendMessage(message);
    }


    @Override
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (isPlayerCurrentlyChoosing(event.getPlayer())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("§cTsk Tsk, I know what you're up to...");
        }
    }

    @Override
    public void onGameEnd() {
        currentlyAppliedBenefitsAndDetriments.clear();
        WYREffectHandler.clearAndDecomposeManagedEffects();
        for (Player plr : getGame().getPlayers()) {
            if (getGame().getConfig().getWouldYouRatherConfig().shouldPreventMovingDuringChoiceSelection()) {
                plr.getAttribute(Attribute.MOVEMENT_SPEED).removeModifier(preventMovingKey);
                plr.getAttribute(Attribute.JUMP_STRENGTH).removeModifier(preventJumpingKey);
            }
            for (PotionEffect effect : plr.getActivePotionEffects()) {
                plr.removePotionEffect(effect.getType());
            }
            plr.removePotionEffect(PotionEffectType.RESISTANCE);
        }

    }

    @Override
    public void updateConfig() {
        this.setMinTicks(getGame().getConfig().getWouldYouRatherConfig().getMinimumSecondsBeforeNewChoice() * 20);
        this.setMaxTicks(getGame().getConfig().getWouldYouRatherConfig().getMaximumSecondsBeforeNewChoice() * 20);
    }

    @Override
    public void onGameStart() {
        updateConfig();
        super.onGameStart();
        if (this.getGame().getConfig().getWouldYouRatherConfig().shouldStartGameWithAChoicePrompt()) {
            setTickGoal(60);
        }
        for (Player plr : this.getGame().getPlayers()) {
            currentlyAppliedBenefitsAndDetriments.put(plr.getUniqueId(), new ArrayList<>());
        }
    }

    @Override
    public void onPlayerJoin(Player plr) {
        currentlyAppliedBenefitsAndDetriments.putIfAbsent(plr.getUniqueId(), new ArrayList<>());
    }

    @Override
    public String toString() {
        return "WouldYouRather";
    }
}
