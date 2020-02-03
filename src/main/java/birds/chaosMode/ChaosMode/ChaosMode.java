package birds.chaosMode.ChaosMode;

import birds.chaosMode.ChaosMode.commands.ChaosCommand;
import birds.chaosMode.ChaosMode.modes.Mode;
import birds.chaosMode.ChaosMode.modes.chaosModes.*;
import birds.chaosMode.ChaosMode.modes.options.BooleanOption;
import birds.chaosMode.ChaosMode.modes.options.ConfigurableOption;
import birds.chaosMode.ChaosMode.modes.options.IntegerOption;
import birds.chaosMode.ChaosMode.modes.options.ItemListOption;
import birds.chaosMode.ChaosMode.utility.Usables;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChaosMode extends JavaPlugin {

    public ArrayList<Mode> getModes() {
        return modes;
    }

    private FileConfiguration getCustomConfig() {
        return this.config;
    }

    private ArrayList<Mode> modes = new ArrayList<>();

    private FileConfiguration config;
    private File configFile;

    @Override
    public void onEnable() {
        Usables usables = new Usables();
        modes.add(new MoonGravity(this));
        modes.add(new TntDrop(this));
        modes.add(new Corruption(this, usables));
        modes.add(new PotionEffects(this, usables));
        modes.add(new EternalNight(this));
        modes.add(new BlockCollapse(this));

        this.getCommand("chaos").setExecutor(new ChaosCommand(this)); // open menu hub

        createConfig();
        loadConfig();
    }

    @Override
    public void onDisable() {
        saveConfig();
    }

    private void createConfig() {
        configFile = new File(getDataFolder(), "savedsettings.yml");
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            saveResource("savedsettings.yml", false);
        }

        config = new YamlConfiguration();
        try {
            config.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void saveConfig() {
        for(Mode mode : modes) {
            String name = mode.getInternalName() + ".";
            for(ConfigurableOption option : mode.getOptions()) {
                if(option instanceof IntegerOption)
                    getCustomConfig().set(name + option.getName(), ((IntegerOption) option).getValue());
                else if(option instanceof BooleanOption)
                    getCustomConfig().set(name + option.getName(), ((BooleanOption) option).getValue());
                else if(option instanceof ItemListOption)
                    getCustomConfig().set(name + option.getName(), ((ItemListOption) option).getItems());
            }
        }
        try {
            getCustomConfig().save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadConfig() {
        for(Mode mode : modes) {
            String name = mode.getInternalName() + ".";
            for(ConfigurableOption option : mode.getOptions()) {
                if (option instanceof IntegerOption)
                    ((IntegerOption) option).setValue(getCustomConfig().getInt(name + option.getName()));
                else if (option instanceof BooleanOption)
                    ((BooleanOption) option).setValue(getCustomConfig().getBoolean(name + option.getName()));
                else if (option instanceof ItemListOption)
                    ((ItemListOption) option).setItems((List<Material>) getCustomConfig().getList(name + option.getName()));
            }
        }
    }
}
