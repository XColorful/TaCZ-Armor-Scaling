package xiao.armorscaling.common.scaling;

import xiao.armorscaling.api.scaling.IScalingSubManager;

public abstract class AbstractScalingManager implements IScalingSubManager {

    protected boolean configPrepared = false;
    protected boolean isEnabled = false;

    public AbstractScalingManager() {
    }

    @Override
    public boolean isConfigPrepared() {
        return this.configPrepared;
    }

    protected abstract boolean unregisterEvents();

    @Override
    public boolean unregisterToMod() {
        if (unregisterEvents()) {
            clearConfig();
            return true;
        }
        return false;
    }

    @Override
    public boolean isEnabled() {
        return this.isEnabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.isEnabled = enabled;
        saveEnabled(this.isEnabled);
    }

    protected abstract void saveEnabled(boolean isEnabled);
}
