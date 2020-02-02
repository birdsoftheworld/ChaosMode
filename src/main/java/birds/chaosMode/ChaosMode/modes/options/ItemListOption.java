package birds.chaosMode.ChaosMode.modes.options;

import org.bukkit.Material;

import java.util.ArrayList;

public class ItemListOption extends ConfigurableOption {

    private ArrayList<Material> items = new ArrayList<>();
    private ArrayList<Material> defaultValue = new ArrayList<>();

    public ArrayList<Material> getItems() {
        return items;
    }

    public void setItems(ArrayList<Material> items) {
        this.items = items;
    }

    public void addItem(Material item) {
        this.items.add(item);
    }

    public void removeItem(Material item) {
        this.items.remove(item);
    }
}
