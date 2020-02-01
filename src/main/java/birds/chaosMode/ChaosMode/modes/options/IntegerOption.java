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
        if(isEvent)
            changeEvent.change(this.value, value);
        this.value = value;
    }

    public void setChangeEvent(IntChangeEvent changeEvent) {
        this.changeEvent = changeEvent;
        isEvent = true;
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
    private boolean isEvent;

    private IntChangeEvent changeEvent;

    public IntegerOption(int defaultValue, int minimumValue, int maximumValue) {
        this.value = defaultValue;
        this.defaultValue = defaultValue;
        this.minimumValue = minimumValue;
        this.maximumValue = maximumValue;
    }

    public IntegerOption() {}
}
