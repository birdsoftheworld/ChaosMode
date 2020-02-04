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
        modes.add(new NeverStop(this));

        this.getCommand("chaos").setExecutor(new ChaosCommand(this)); // open menu hub

        createConfig();
    }

    private void createConfig() {
        configFile = new File(getDataFolder(), "savedsettings.yml");
        // create a config if it doesn't already exist
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
        // save all options in a custom config file

        getLogger().info("Saving config...");

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
            getCustomConfig().set(name + "enabled", mode.isEnabled());
            getCustomConfig().set("active", true);
        }
        try {
            getCustomConfig().save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadConfig() {
        // load previous options

        getLogger().info("Loading config...");

        // stop if not already saved
        if(!getCustomConfig().getBoolean("active")) return;

        for(Mode mode : modes) {
            String name = mode.getInternalName() + ".";
            // set modes to loaded state, but only if loaded state is different than current state
            if(getCustomConfig().getBoolean(name + "enabled") && !mode.isEnabled())
                mode.enable();
            else if(mode.isEnabled())
                mode.disable();

            for(ConfigurableOption option : mode.getOptions()) {
                if (option instanceof IntegerOption)
                    ((IntegerOption) option).setValue(getCustomConfig().getInt(name + option.getName()));
                else if (option instanceof BooleanOption)
                    ((BooleanOption) option).setValue(getCustomConfig().getBoolean(name + option.getName()));
                else if (option instanceof ItemListOption)
                    ((ItemListOption) option).setItems((List<Material>) getCustomConfig().getList(name + option.getName()));
            }

            // make sure all values update
            if(mode.isEnabled())
                mode.update();
        }
    }

    public void resetToDefaults() {
        // reset all options to their default values
        for(Mode mode : modes) {
            for(ConfigurableOption option : mode.getOptions()) {
                if (option instanceof IntegerOption)
                    ((IntegerOption) option).setValue(((IntegerOption) option).getDefaultValue());
                else if (option instanceof BooleanOption)
                    ((BooleanOption) option).setValue(((BooleanOption) option).getDefaultValue());
                else if (option instanceof ItemListOption)
                    ((ItemListOption) option).setItems(new ArrayList<Material>());
            }
            mode.disable();
        }
    }
}
