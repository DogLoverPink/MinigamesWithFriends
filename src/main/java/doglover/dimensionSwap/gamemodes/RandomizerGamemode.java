package doglover.dimensionSwap.gamemodes;

import doglover.dimensionSwap.utils.PlayerUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Stream;

public class RandomizerGamemode extends TimeEventBasedGamemode {

    Map<Material, ItemStack> blockMap = new HashMap<>();

    public ItemStack getBlockItem(Material material) {
        return blockMap.get(material);
    }

    @Override
    public void onGameEnd() {
        super.onGameEnd();


    }

    public void randomizeBlocks() {
        List<Material> materials = new ArrayList<>(List.of(Material.values()));
        materials = materials.stream().filter(Material::isBlock).toList();
        List<Material> materials2 = new ArrayList<>(Stream.of(Material.values()).filter(Material::isItem).toList());
        Collections.shuffle(materials2);
        blockMap.clear();
        for (int i = 0; i < materials.size(); i++) {
            blockMap.put(materials.get(i), new ItemStack(materials2.get(i)));
        }
    }

    @Override
    public void onGameStart() {
        this.setMinTicks(this.getGame().getConfig().getRandomizerConfig().getMinimumSecondsBeforeDeathMatch() * 20);
        this.setMaxTicks(this.getGame().getConfig().getRandomizerConfig().getMaximumSecondsBeforeDeathMatch() * 20);
        super.onGameStart();
        randomizeBlocks();

    }

    @Override
    public void tick() {
        super.tick();
        if (this.getGame().getConfig().getRandomizerConfig().isEnableDeathMatches()) {
            this.getGame().addScoreboardContributution("§dDeathmatch in: §b" + getFormattedTimeRemaining());
        }
    }

    @Override
    public void onDeathMatchEnd() {
        if (this.getGame().getConfig().getRandomizerConfig().isRerandomizeAfterDeathMatch()) {
            randomizeBlocks();
        }
    }

    @Override
    public void onTimeEventTrigger() {
        if (this.getGame().getConfig().getRandomizerConfig().isEnableDeathMatches()) {
            this.getGame().startDeathMatch();
        }

    }

    @Override
    public String toString() {
        return "Randomizer";
    }
}
