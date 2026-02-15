package doglover.minigameswithfriends.wouldyourather.effects.detrimentaleffects;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import doglover.minigameswithfriends.utils.NumberUtils;
import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.time.Duration;
import java.util.Random;

public class SkillChecks extends WYREffect {


    static {
        WYREffectHandler.registerDetrimentalWYREffect(SkillChecks.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "You must pass skill checks occasionally";
    }


    @Override
    public void onTick() {
        if (!isInSkillCheck) {
            if (NumberUtils.chanceOf(0.00125) && skillCheckProgress >= 0) {
                giveSkillCheckWarning();
            }
            if (skillCheckProgress < 0) {
                skillCheckProgress++;
                if (skillCheckProgress >= 0) {
                    doSkillCheck();
                }
            }
            return;
        }
        skillCheckProgress++;
        if (skillCheckProgress > skillCheckSuccessRangeMax + 3) {
            onSkillCheckFail();
        }
        displaySkillCheckSubtitle();
    }

    private void giveSkillCheckWarning() {
        skillCheckProgress = -20;
        getPlayer().playSound(getPlayer(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1.5f);
        getGame().sendActionBar("skillChecks", Component.text("Press SPACE at the correct time"), getPlayer(), 30);
    }

    int skillCheckProgress = 0;
    int skillCheckSuccessRangeMin = -1;
    int skillCheckSuccessRangeMax = -1;
    boolean isInSkillCheck = false;

    private void doSkillCheck() {
        isInSkillCheck = true;
        skillCheckSuccessRangeMin = random.nextInt(20, 30);
        skillCheckSuccessRangeMax = skillCheckSuccessRangeMin + 3;
        skillCheckProgress = 0;
        displaySkillCheckSubtitle();
    }

    private void onSkillCheckFail() {
        getPlayer().damage(8);
        getPlayer().playSound(getPlayer(), Sound.BLOCK_ANVIL_LAND, 1f, 0.1f);
        isInSkillCheck = false;
    }

    private void onSkillCheckSuccess() {
        getPlayer().playSound(getPlayer(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
        isInSkillCheck = false;
    }

    private void displaySkillCheckSubtitle() {
        if (!isInSkillCheck) {
            return;
        }
        Component checkString = Component.text("[").color(NamedTextColor.YELLOW);
        for (int i = 0; i < 40; i++) {
            if (i == skillCheckProgress) {
                checkString = checkString.append(Component.text("|").color(NamedTextColor.YELLOW));
            } else if (i >= skillCheckSuccessRangeMin && i < skillCheckSuccessRangeMax) {
                checkString = checkString.append(Component.text("|").color(NamedTextColor.GREEN));
            }else {
                checkString = checkString.append(Component.text("|").color(NamedTextColor.GRAY));
            }
        }
        checkString = checkString.append(Component.text("]").color(NamedTextColor.YELLOW));
        Title.Times times = Title.Times.times(Duration.ZERO, Duration.ofMillis(250), Duration.ZERO);
        getPlayer().showTitle(Title.title(Component.text(""), checkString, times));

    }


    @Override
    public void onPlayerJump(PlayerJumpEvent event) {
        if (!event.getPlayer().equals(getPlayer()) || !isInSkillCheck) {
            return;
        }
        event.setCancelled(true);
        if (skillCheckProgress >= skillCheckSuccessRangeMin && skillCheckProgress < skillCheckSuccessRangeMax) {
            onSkillCheckSuccess();
        } else {
            onSkillCheckFail();
        }

    }

    public SkillChecks(Player player) {
        super(player);
        setRepeatable(false);
        subscribeToEvent(PlayerJumpEvent.class);
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
