package com.cobelpvp.generator;

import net.minecraft.server.BiomeBase;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.IOException;

public class GeneratorConfig {

    private final String worldName;
    private final File file;
    private final YamlConfiguration conf;

    public boolean oceans;

    public boolean biomePlains;
    public boolean biomeDesert;
    public boolean biomeDesertHills;
    public boolean biomeExtremeHills;
    public boolean biomeExtremeHillsPlus;
    public boolean biomeForest;
    public boolean biomeForestHills;
    public boolean biomeTaiga;
    public boolean biomeTaigaHills;
    public boolean biomeSwampland;
    public boolean biomeIcePlains;
    public boolean biomeIceMountains;
    public boolean biomeMushroomIsland;
    public boolean biomeJungle;
    public boolean biomeJungleHills;
    public boolean biomeBirchForest;
    public boolean biomeBirchForestHills;
    public boolean biomeRoofedForest;
    public boolean biomeColdTaiga;
    public boolean biomeColdTaigaHills;
    public boolean biomeMegaTaiga;
    public boolean biomeMegaTaigaHills;
    public boolean biomeSavanna;
    public boolean biomeSavannaPlateau;
    public boolean biomeMesa;
    public boolean biomeMesaPlateauF;
    public boolean biomeMesaPlateau;

    public BiomeBase spawnBiome;
    public int spawnBiomeRadius;
    public boolean spawnBiomeRivers;

    public float cavesMultiplier;

    public float coalMultiplier;
    public int coalSize;
    public boolean coalMustTouchAir;
    public float ironMultiplier;
    public int ironSize;
    public boolean ironMustTouchAir;
    public float goldMultiplier;
    public int goldSize;
    public boolean goldMustTouchAir;
    public float redstoneMultiplier;
    public int redstoneSize;
    public boolean redstoneMustTouchAir;
    public float diamondMultiplier;
    public int diamondSize;
    public boolean diamondMustTouchAir;
    public float lapisMultiplier;
    public int lapisSize;
    public boolean lapisMustTouchAir;
    public float sugarCaneMultiplier;

    public GeneratorConfig(String worldName) {
        this.worldName = worldName;
        this.file = new File("config/generator", worldName + ".yml");
        conf = YamlConfiguration.loadConfiguration(file);
        conf.options().copyDefaults(true);

        oceans = getBoolean("oceans", false);
        biomePlains = getBoolean("biome.plains", false);
        biomeDesert = getBoolean("biome.desert", true);
        biomeDesertHills = getBoolean("biome.desert-hills", true);
        biomeExtremeHills = getBoolean("biome.extreme-hills", true);
        biomeExtremeHillsPlus = getBoolean("biome.extreme-hills-plus", true);
        biomeForest = getBoolean("biome.forest", false);
        biomeForestHills = getBoolean("biome.forest-hills", false);
        biomeTaiga = getBoolean("biome.taiga", true);
        biomeTaigaHills = getBoolean("biome.taiga-hills", true);
        biomeSwampland = getBoolean("biome.swampland", false);
        biomeIcePlains = getBoolean("biome.ice-plains", true);
        biomeIceMountains = getBoolean("biome.ice-mountains", false);
        biomeMushroomIsland = getBoolean("biome.mushroom-island", true);
        biomeJungle = getBoolean("biome.jungle", false);
        biomeJungleHills = getBoolean("biome.jungle-hills", false);
        biomeBirchForest = getBoolean("biome.birch-forest", true);
        biomeBirchForestHills = getBoolean("biome.birch-forest-hills", true);
        biomeRoofedForest = getBoolean("biome.roofed-forest", true);
        biomeColdTaiga = getBoolean("biome.cold-taiga", true);
        biomeColdTaigaHills = getBoolean("biome.cold-taiga-hills", true);
        biomeMegaTaiga = getBoolean("biome.mega-taiga", true);
        biomeMegaTaigaHills = getBoolean("biome.mega-taiga-hills", true);
        biomeSavanna = getBoolean("biome.savanna", true);
        biomeSavannaPlateau = getBoolean("biome.savanna-plateau", true);
        biomeMesa = getBoolean("biome.mesa", false);
        biomeMesaPlateauF = getBoolean("biome.mesa-plateau-f", false);
        biomeMesaPlateau = getBoolean("biome.mesa-plateau", false);

        spawnBiome = getBiome(getString("spawn.biome", "savanna"));
        spawnBiomeRadius = getInt("spawn.radius", 900);
        spawnBiomeRivers = getBoolean("spawn.rivers", false);

        cavesMultiplier = (float) getDouble("caves.multiplier", 1.0);

        coalMultiplier = (float) getDouble("ores.coal.multiplier", 4.0);
        coalSize = getInt("ores.coal.size", 10);
        coalMustTouchAir = getBoolean("ores.coal.must-touch-air", false);
        ironMultiplier = (float) getDouble("ores.iron.multiplier", 10.0);
        ironSize = getInt("ores.iron.size", 8);
        ironMustTouchAir = getBoolean("ores.iron.must-touch-air", false);
        goldMultiplier = (float) getDouble("ores.gold.multiplier", 11.0);
        goldSize = getInt("ores.gold.size", 11);
        goldMustTouchAir = getBoolean("ores.gold.must-touch-air", false);
        redstoneMultiplier = (float) getDouble("ores.redstone.multiplier", 5.0);
        redstoneSize = getInt("ores.redstone.size", 7);
        redstoneMustTouchAir = getBoolean("ores.redstone.must-touch-air", false);
        diamondMultiplier = (float) getDouble("ores.diamond.multiplier", 16.0);
        diamondSize = getInt("ores.diamond.size", 14);
        diamondMustTouchAir = getBoolean("ores.diamond.must-touch-air", false);
        lapisMultiplier = (float) getDouble("ores.lapis.multiplier", 0.1);
        lapisSize = getInt("ores.lapis.size", 1);
        lapisMustTouchAir = getBoolean("ores.lapis.must-touch-air", false);

        sugarCaneMultiplier = (float) getDouble("sugar-cane.multiplier", 1.5);

        try {
            conf.save(file);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private boolean getBoolean(String path, boolean def) {
        conf.addDefault(path, def);
        return conf.getBoolean(path, def);
    }

    private String getString(String path, String def) {
        conf.addDefault(path, def);
        return conf.getString(path, def);
    }

    private int getInt(String path, int def) {
        conf.addDefault(path, def);
        return conf.getInt(path, def);
    }

    private double getDouble(String path, double def) {
        conf.addDefault(path, def);
        return conf.getDouble(path, def);
    }

    private BiomeBase getBiome(String name) {
        for (BiomeBase biome : BiomeBase.getBiomes()) {
            if (biome.af.equalsIgnoreCase(name)) {
                return biome;
            }
        }
        return BiomeBase.PLAINS;
    }
}
