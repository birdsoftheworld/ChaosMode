package birds.chaosMode.ChaosMode.modes;

public class Mode {

    private boolean enabled;

    Mode() {
        this.enabled = false;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
