package birds.chaosMode.ChaosMode.modes.options;

public class BooleanOption extends ConfigurableOption {
    private boolean value;
    private boolean defaultValue;

    public BooleanOption(boolean defaultValue, String name) {
        this.value = defaultValue;
        this.defaultValue = defaultValue;
        this.name = name;
    }

    public BooleanOption() {}

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    public boolean getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(boolean defaultValue) {
        this.defaultValue = defaultValue;
    }
}
