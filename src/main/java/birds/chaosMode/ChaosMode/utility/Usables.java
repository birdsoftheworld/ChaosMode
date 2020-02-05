package birds.chaosMode.ChaosMode.utility;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.loot.LootTable;
import org.bukkit.loot.LootTables;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Usables {
    private ArrayList<Material> usableBlocks;
    private ArrayList<EntityType> usableEntities;
    private ArrayList<LootTables> usableLootTables;
    private ArrayList<PotionEffectType> usablePotionEffects;

    private Random random;

    public Usables() {
        random = new Random();

        discoverBlocks();
        discoverEntities();
        discoverLootTables();
        discoverPotionEffects();
    }

    private int randInt(int lowerBound, int higherBound) {
        return random.nextInt((higherBound - lowerBound) + 1) + lowerBound;
    }

    private void discoverBlocks() {
        Bukkit.getLogger().info("Starting block discovery...");
        usableBlocks = new ArrayList<>();
        // get all blocks
        for(Material material : Material.values()) {
            // only include if a block and isn't deprecated
            if(material.isBlock() && !(material.toString().contains("LEGACY")))
                usableBlocks.add(material);
        }
        Bukkit.getLogger().info("Discovered " + usableBlocks.size() + " blocks!");
    }

    private void discoverEntities() {
        Bukkit.getLogger().info("Starting entity discovery...");
        usableEntities = new ArrayList<>();
        // get usable entities
        for(EntityType type : EntityType.values()) {
            // don't include weird types or bosses
            if (type.equals(EntityType.PLAYER)) continue;
            if (type.equals(EntityType.UNKNOWN)) continue;
            if (type.equals(EntityType.AREA_EFFECT_CLOUD)) continue;
            if (type.equals(EntityType.SPLASH_POTION)) continue;
            if (type.equals(EntityType.DROPPED_ITEM)) continue;
            if (type.equals(EntityType.FALLING_BLOCK)) continue;
            if (type.equals(EntityType.WITHER)) continue;
            if (type.equals(EntityType.ENDER_DRAGON)) continue;
            if (type.equals(EntityType.ENDER_PEARL)) continue;
            if (type.equals(EntityType.FISHING_HOOK)) continue;
            if (type.equals(EntityType.LEASH_HITCH)) continue;
            if (type.equals(EntityType.ENDER_SIGNAL)) continue;
            if (type.equals(EntityType.PAINTING)) continue;
            if (type.equals(EntityType.ITEM_FRAME)) continue;
            if (type.equals(EntityType.RAVAGER)) continue;
            usableEntities.add(type);
        }
        Bukkit.getLogger().info("Discovered " + usableEntities.size() + " entities!");
    }

    private void discoverLootTables() {
        Bukkit.getLogger().info("Starting LootTable discovery...");
        usableLootTables = new ArrayList<>();
        // get all loottables
        usableLootTables.addAll(Arrays.asList(LootTables.values()));
        Bukkit.getLogger().info("Discovered " + usableLootTables.size() + " LootTables!");
    }

    private void discoverPotionEffects() {
        Bukkit.getLogger().info("Starting effect discovery...");
        usablePotionEffects = new ArrayList<>();

        // get all effects
        for(PotionEffectType effect : PotionEffectType.values()) {
            // don't include instant potion effects, which do weird things when applied for a long time
            if(effect.equals(PotionEffectType.HARM)) continue;
            if(effect.equals(PotionEffectType.HEAL)) continue;
            usablePotionEffects.add(effect);
        }
        usablePotionEffects.addAll(Arrays.asList(PotionEffectType.values()));
        Bukkit.getLogger().info("Discovered " + usablePotionEffects.size() + " effects!");
    }

    public LootTable getUsableLootTable() {
        return Bukkit.getLootTable(usableLootTables.get(randInt(0, usableLootTables.size() - 1)).getKey());
    }

    public Material getUsableBlock() {
        return usableBlocks.get(randInt(0, usableBlocks.size() - 1));
    }

    public EntityType getUsableEntity() {
        return usableEntities.get(randInt(0, usableEntities.size() - 1));
    }

    public PotionEffectType getUsablePotionEffect() {
        return usablePotionEffects.get(randInt(0, usablePotionEffects.size() - 1));
    }
}
