package doglover.minigameswithfriends.utils;

import doglover.minigameswithfriends.MinigamesWithFriends;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarUtils {

    //this isn't malware I swear, I just hate having to register 59 different classes manually
    public static void initalizeWouldYouRatherClasses(String packageName) {
        String packagePath = packageName.replace(".", "/");
        try {
            String jarName = new java.io.File(MinigamesWithFriends.class.getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .getPath())
                    .getName();
            JarFile jarFile = new JarFile("plugins/" + jarName);
            Enumeration<JarEntry> entries = jarFile.entries();

            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String entryName = entry.getName();
                if (entryName.endsWith(".class") && entryName.startsWith(packagePath)) {
                    String className = entryName.substring(0, entryName.length() - 6).replace("/", ".");
                    try {
                        //will run the static methods in that file
                        Class<?> clazz = Class.forName(className);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
            jarFile.close();

        } catch (Exception e) {
            e.printStackTrace();
            MinigamesWithFriends.getGamePlugin().getLogger().severe("Failed to load would you rather classes! Would you rather with NOT work!");
        }
    }
}


