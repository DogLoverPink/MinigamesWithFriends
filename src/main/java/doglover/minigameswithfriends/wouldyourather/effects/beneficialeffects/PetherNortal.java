package doglover.minigameswithfriends.wouldyourather.effects.beneficialeffects;

import doglover.minigameswithfriends.wouldyourather.WYREffect;
import doglover.minigameswithfriends.wouldyourather.WYREffectHandler;
import org.bukkit.Axis;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Orientable;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

public class PetherNortal extends WYREffect {


    static {
        WYREffectHandler.registerBeneficialWYREffect(PetherNortal.class);
    }

    @Override
    public String getDescriptionBlurb() {
        return "P E T H E R  N O R T A L";
    }

    public PetherNortal(Player player) {
        super(player);
        setRepeatable(true);
    }

    @Override
    public void onEffectInitiate() {
        super.onEffectInitiate();
        createPetherNortal(getPlayer());
        this.selfDestruct();
    }

    public void createPetherNortal(Player player) {
        World world = player.getWorld();
        Location base = player.getLocation().add(player.getLocation().getDirection().normalize().multiply(3).add(new Vector(0, 4.5, 0)));
        base.setX(base.getBlockX() + 0.5);
        base.setZ(base.getBlockZ() + 0.5);

        int width = 2;
        int height = 3;

        Material frameBlock = Material.NETHER_PORTAL;
        Material insideBlock = Material.OBSIDIAN;

        BlockFace cardinalFacing = getCardinalFacing(getPlayer());
        Vector forward = cardinalFacing.getDirection();
        Vector right = new Vector(-forward.getZ(), 0, forward.getX());

        Axis facingAxis = Axis.X;
        if (cardinalFacing.equals(BlockFace.EAST) || cardinalFacing.equals(BlockFace.WEST)) {
            facingAxis = Axis.Z;
        }

        for (int y = 0; y <= height + 1; y++) {
            for (int x = -1; x <= width; x++) {
                Location loc = base.clone()
                        .add(right.clone().multiply(x))
                        .subtract(0, y, 0);

                boolean isFrame = y == 0 || y == height + 1 || x == -1 || x == width;

                Block block = world.getBlockAt(loc);

                if (isFrame) {
                    block.setType(frameBlock, false);
                    if (block.getBlockData() instanceof Orientable orientable) {
                        orientable.setAxis(facingAxis);
                        block.setBlockData(orientable);
                    }
                } else {
                    block.setType(insideBlock, false);
                }
            }
        }
    }

    public BlockFace getCardinalFacing(Player player) {
        float yaw = player.getLocation().getYaw();
        yaw = (yaw % 360 + 360) % 360;

        if (yaw >= 45 && yaw < 135) return BlockFace.EAST;
        if (yaw >= 135 && yaw < 225) return BlockFace.SOUTH;
        if (yaw >= 225 && yaw < 315) return BlockFace.WEST;
        return BlockFace.NORTH;
    }

    @Override
    public void onEffectDecompose() {
        super.onEffectDecompose();
    }
}
