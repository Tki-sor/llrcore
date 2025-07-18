package com.tkisor.llrcore.gtm.config;

import net.minecraftforge.fml.loading.FMLPaths;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.mojang.logging.LogUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class LLRCoreConfig {

    protected static Path path = FMLPaths.CONFIGDIR.get().resolve("llrcore_config.json");
    private static volatile LLRCoreConfig config;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final String _comment1 = "Set the GT Difficulty (Easy, Normal, Hard)";
    public String gt_difficulty = GTDifficultyEnum.Normal.name();

    private LLRCoreConfig() {}

    public GTDifficultyEnum getGTDifficulty() {
        return GTDifficultyEnum.fromString(config.gt_difficulty);
    }

    public static LLRCoreConfig create() {
        if (path == null)
            throw new IllegalStateException(
                    "[MemorySweep.Config]Path must be initialized before creating Config.");
        if (config == null) {
            synchronized (LLRCoreConfig.class) {
                if (config == null) {
                    config = new LLRCoreConfig();
                }
            }
        }
        read();
        write();

        return config;
    }

    public static LLRCoreConfig get() {
        if (config == null) create();
        return config;
    }

    public static void read() {
        if (!Files.exists(path)) {
            // config already has default values
            LogUtils.getLogger().info("[Config.read] No config file found; using default values.");

        } else {
            try {
                List<String> lines = Files.readAllLines(path);
                String rawData = String.join("\n", lines);
                config = GSON.fromJson(rawData, LLRCoreConfig.class);
                LogUtils.getLogger().info("[Config.read] Loaded config info from '{}'!", path);

            } catch (JsonIOException | JsonSyntaxException e) {
                writeCopy();
                reset();
                LogUtils.getLogger().info("[Config.read] The config couldn't be loaded; copied old data and reset:", e);

            } catch (IOException e) {
                reset();
                LogUtils.getLogger().error(
                        "[Config.read] An error occurred while trying to load config data from '{}':", path, e);
            }
        }
    }

    public static void write() {
        try (FileWriter fw = new FileWriter(path.toFile())) {
            GSON.toJson(config, config.getClass(), fw);
            LogUtils.getLogger().info("[Config.write] Saved config info to '{}'!", path);

        } catch (Exception e) {
            LogUtils.getLogger().error(
                    "[Config.write] An error occurred while trying to save the config to '{}':", path, e);
        }
    }

    public static void writeCopy() {
        try {
            Files.copy(
                    path, path.resolveSibling(
                            "memorysweep_" + new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date()) + ".json"));
        } catch (IOException e) {
            LogUtils.getLogger().warn(
                    "[Config.writeCopy] An error occurred trying to write a copy of the original config file:", e);
        }
    }

    public static void reset() {
        config = new LLRCoreConfig();
    }
}
