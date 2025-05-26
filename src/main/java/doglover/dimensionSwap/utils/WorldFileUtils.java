package doglover.dimensionSwap.utils;

import doglover.dimensionSwap.DimensionSwap;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class WorldFileUtils {

    private static void findNestedWorldFolderAndMove(File destinationDirectory, File folder) {
        if (!folder.exists() || !folder.isDirectory()) {
            return;
        }
        for (File subFile : folder.listFiles()) {
            if (subFile.isDirectory()) {
                findNestedWorldFolderAndMove(destinationDirectory, subFile);
                continue;
            }
            if (!subFile.getName().equals("level.dat")) {
                continue;
            }
            try {
                DimensionSwap.getGamePlugin().getLogger().info("Found nested world folder: " + folder.getName() + ", moving it to saved world folders");
                FileUtils.moveDirectoryToDirectory(folder, destinationDirectory, true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void sanitizeWorldSaveFolder(File directoryWithTheWorlds) {
        while (innerSanitizeWorldSaveFolder(directoryWithTheWorlds)) {
            continue;
        }
    }

    public static void loadAllWorldsInFolder(File worldsFolder, Player playerToNotify) {
        final List<File> worlds = new ArrayList<>();
        for (File file : worldsFolder.listFiles()) {
            if (!file.isDirectory() || file.list() == null && !Arrays.asList(file.list()).contains("level.dat")) {
                continue;
            }
            File worldToLoad = new File(worldsFolder.getPath() + "/" + file.getName());
            worlds.add(worldToLoad);
        }
        int worldsCount = worlds.size();
        Bukkit.getScheduler().runTaskTimer(DimensionSwap.getGamePlugin(), (task) -> {
            if (worlds.isEmpty()) {
                playerToNotify.sendMessage("§a§lPreloading complete!.");
                playerToNotify.sendMessage("§aIt is highly recomended that you restart your server!");
                task.cancel();
                return;
            }
            File file = worlds.removeFirst();
            playerToNotify.sendMessage("§eLoading world: §a" + file.getName());
            DimensionSwap.getGamePlugin().getLogger().info("Loading world: " + file.getName());
            World world = Bukkit.createWorld(new WorldCreator(file.getPath().replace("\\", "/")));
            playerToNotify.sendMessage("§aComplete. §eUnloading §a" + world.getName());
            Bukkit.unloadWorld(world, true);
            playerToNotify.sendMessage("§aCompleted (" + (worldsCount - worlds.size()) + "/" + worldsCount + ")");
        }, 0, 20);
    }

    private static boolean innerSanitizeWorldSaveFolder(File directoryWithTheWorlds) {
        StringBuilder renamedFiles = new StringBuilder();
        StringBuilder deletedFiles = new StringBuilder();
        for (File file : directoryWithTheWorlds.listFiles()) {
            if (!file.isDirectory()) {
                continue;
            }
            if (file.list() == null) {
                try {
                    FileUtils.deleteDirectory(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                deletedFiles.append(file.getName()).append(", ");
                continue;
            }
            List<String> files = Arrays.asList(file.list());
            if (!files.contains("level.dat")) {
                try {
                    findNestedWorldFolderAndMove(directoryWithTheWorlds, file);
                    FileUtils.deleteDirectory(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                deletedFiles.append(file.getName()).append(", ");
                continue;
            }
            String fileName = file.getName();
            if (!fileName.matches("^[a-zA-Z0-9/._-]+$")) {
                String newName = file.getName().replace(" ", "_");
                newName = newName.replaceAll("[^a-zA-Z0-9/._-]", "");
                File newFile = new File(directoryWithTheWorlds, newName);
                if (!newFile.exists()) {
                    file.renameTo(newFile);
                    renamedFiles.append(newName).append(", ");
                }
            }
        }
        return wasThereChange(renamedFiles, deletedFiles);
    }

    private static boolean wasThereChange(StringBuilder renamedFiles, StringBuilder deletedFiles) {
        Logger logger = DimensionSwap.getGamePlugin().getLogger();
        boolean isChange = !renamedFiles.toString().isEmpty() || !deletedFiles.toString().isEmpty();
        if (isChange) {
            logger.info("World Folder Sanitization complete: ");
            if (!renamedFiles.toString().isEmpty()) {
                logger.info(" - Renamed " + renamedFiles + " appropriately.");
            }
            if (!deletedFiles.toString().isEmpty()) {
                logger.info(" - Deleted " + deletedFiles + ", because they were empty or invalid.");
            }
        }
        return isChange;
    }
}
