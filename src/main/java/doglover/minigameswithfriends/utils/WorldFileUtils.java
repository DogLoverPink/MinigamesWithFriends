package doglover.minigameswithfriends.utils;

import doglover.minigameswithfriends.MinigamesWithFriends;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class WorldFileUtils {

    private static File getUniqueDirectory(File parentDirectory, String desiredName) {
        File candidate = new File(parentDirectory, desiredName);
        if (!candidate.exists()) {
            return candidate;
        }

        int suffix = 2;
        while (true) {
            File suffixedCandidate = new File(parentDirectory, desiredName + "_" + suffix);
            if (!suffixedCandidate.exists()) {
                return suffixedCandidate;
            }
            suffix++;
        }
    }

    private static void findNestedWorldFolderAndMove(File destinationDirectory, File folder) {
        if (!folder.exists() || !folder.isDirectory()) {
            return;
        }
        for (File subFile : folder.listFiles()) {
            if (subFile.isDirectory() && !subFile.getName().equals("poi")) {
                findNestedWorldFolderAndMove(destinationDirectory, subFile);
                continue;
            }
            if (!subFile.isDirectory() || !subFile.getName().equals("poi")) {
                continue;
            }
            try {
                File destinationFolder = getUniqueDirectory(destinationDirectory, folder.getName());
                MinigamesWithFriends.getGamePlugin().getLogger().info(
                        "Found nested world folder: " + folder.getName() + ", moving it to " + destinationFolder.getName()
                );
                FileUtils.moveDirectory(folder, destinationFolder);
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

    public static void loadAllWorldsInFolder(File worldsFolder, Audience playerToNotify) {
        final List<File> worlds = new ArrayList<>();
        for (File file : worldsFolder.listFiles()) {
            if (!file.isDirectory() || file.list() == null || !new File(file, "poi").isDirectory()) {
                continue;
            }
            File worldToLoad = new File(worldsFolder.getPath() + "/" + file.getName());
            worlds.add(worldToLoad);
        }
        int worldsCount = worlds.size();
        Bukkit.getScheduler().runTaskTimer(MinigamesWithFriends.getGamePlugin(), (task) -> {
            if (worlds.isEmpty()) {
                playerToNotify.sendMessage(Component.text("§a§lPreloading complete!."));
                playerToNotify.sendMessage(Component.text("§aIt is highly recomended that you restart your server!"));
                task.cancel();
                return;
            }
            File file = worlds.removeFirst();
            playerToNotify.sendMessage(Component.text("§eLoading world: §a" + file.getName()));
            MinigamesWithFriends.getGamePlugin().getLogger().info("Loading world: " + file.getName());
            String filePath = file.getPath().replace("\\", "/");
            String worldPath = filePath.replace("world/dimensions/minecraft/", "");
            World world = Bukkit.createWorld(new WorldCreator(worldPath));
            playerToNotify.sendMessage(Component.text("§aComplete. §eUnloading §a" + world.getName()));
            Bukkit.unloadWorld(world, true);
            playerToNotify.sendMessage(Component.text("§aCompleted (" + (worldsCount - worlds.size()) + "/" + worldsCount + ")"));
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
            if (!new File(file, "poi").isDirectory()) {
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
        Logger logger = MinigamesWithFriends.getGamePlugin().getLogger();
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
