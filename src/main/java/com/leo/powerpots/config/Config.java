package com.leo.powerpots.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.leo.powerpots.PowerPots;
import com.leo.powerpots.block.PotTier;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Config {
    public static Config INSTANCE;

    @Expose
    private PotTier[] tiers = new PotTier[]{
        new PotTier(1, 1000, 10, 2, 1),
        new PotTier(2, 2000, 20, 4, 2),
        new PotTier(3, 4000, 40, 8, 4)
    };

    public static void initialize() {
        Config config = new Config();
        Gson gson = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .setPrettyPrinting()
            .create();

        Path configPath = FMLPaths.CONFIGDIR.get().resolve("PowerPots.json");

        // Check if the file exists
        if (Files.exists(configPath)) {
            try (BufferedReader reader = Files.newBufferedReader(configPath)) {
                config = gson.fromJson(reader, Config.class);
            } catch (IOException e) {

                System.err.println("Failed to read config, using defaults: " + e.getMessage());
            }
        } else {
            // If file doesn't exist, ensure parent directories are created
            try {
                Files.createDirectories(configPath.getParent());
                PowerPots.LOGGER.info("Config file not found. Creating a new one with default values.");
            } catch (IOException e) {
                PowerPots.LOGGER.error("Failed to create config directory: {}", e.getMessage());
            }
        }

        // If deserialization fails or file doesn't exist, use defaults
        if (config == null) {
            config = new Config();
        }

        // Set the static instance
        INSTANCE = config;

        // Load configuration data into TIERS
        INSTANCE.onConfigLoaded();

        // Save the configuration file to disk (new or updated)
        try (BufferedWriter writer = Files.newBufferedWriter(configPath)) {
            gson.toJson(INSTANCE, writer);
            PowerPots.LOGGER.info("Configuration saved to: {}", configPath);
        } catch (IOException e) {
            PowerPots.LOGGER.error("Failed to save config: {}", e.getMessage());
        }
    }

    @Expose
    public List<PotTier> TIERS = new ArrayList<>();

    private void onConfigLoaded() {
        // Populate TIERS from tiers array
        TIERS.clear();
        if (tiers != null) {
            TIERS.addAll(Arrays.asList(tiers));
        }
    }
}

