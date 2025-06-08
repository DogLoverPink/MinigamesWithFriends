package doglover.minigameswithfriends.wouldyourather.effects.beneficialeffects;

import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockDamageAbortEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class BreakAnything extends WYREffect {


    static {
        WYREffectHandler.registerBeneficialWYREffect(BreakAnything.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "Be able to break any type of block";
    }

    public BreakAnything(Player player) {
        super(player);
        setRepeatable(false);
        subscribeToEvent(BlockDamageEvent.class);
        subscribeToEvent(BlockDamageAbortEvent.class);
    }

    float progress = -1f;
    Block targetBlock;



    @Override
    public void onTick() {
        if (progress == -1 || targetBlock == null) {
            return;
        }
        if (targetBlock.isEmpty()) {
            progress = 0f;
            targetBlock = null;
        }
        sendProgressUpdate();
        progress += 0.01f;
        if (progress >= 1) {
            targetBlock.breakNaturally(new ItemStack(Material.NETHERITE_PICKAXE), true);
        }


    }

    @Override
    public void onBlockDamageAbort(BlockDamageAbortEvent event) {
        if (!event.getPlayer().equals(getPlayer())) {
            return;
        }
        if (targetBlock != null) {
            progress = 0f;
            sendProgressUpdate();
            targetBlock = null;
        }
    }

    private void sendProgressUpdate() {
        getPlayer().sendBlockDamage(targetBlock.getLocation(), progress, 5);
    }

    @Override
    public void onBlockDamage(BlockDamageEvent event) {
        if (!event.getPlayer().equals(getPlayer())) {
            return;
        }
        Block block = event.getBlock();
        if (block.getType().getHardness() == -1 || block.getType().getHardness() >= 50) {
            event.setCancelled(true);
            targetBlock = block;
            progress = 0f;
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
