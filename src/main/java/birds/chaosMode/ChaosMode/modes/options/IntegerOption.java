package birds.chaosMode.ChaosMode.modes.options;

public class IntegerOption extends ConfigurableOption {

    public int getMaximumValue() {
        return maximumValue;
    }

    public void setMaximumValue(int maximumValue) {
        this.maximumValue = maximumValue;
    }

    public int getMinimumValue() {
        return minimumValue;
    }

    public void setMinimumValue(int minimumValue) {
        this.minimumValue = minimumValue;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(int defaultValue) {
        this.defaultValue = defaultValue;
    }

    private int maximumValue;
    private int minimumValue;
    private int defaultValue;
    private int value;

    public IntegerOption(int defaultValue, int minimumValue, int maximumValue, String name) {
        this.value = defaultValue;
        this.defaultValue = defaultValue;
        this.minimumValue = minimumValue;
        this.maximumValue = maximumValue;
        this.name = name;
    }

    public IntegerOption() {}
}
