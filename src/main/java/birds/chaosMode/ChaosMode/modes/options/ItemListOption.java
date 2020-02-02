package birds.chaosMode.ChaosMode.modes.options;

import org.bukkit.Material;

import java.util.ArrayList;

public class ItemListOption extends ConfigurableOption {

    ArrayList<Material> items = new ArrayList<>();

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

    public boolean itemIsAcceptable(Material item) {
        // return true unless duplicate
        return !items.contains(item);
    }
}
