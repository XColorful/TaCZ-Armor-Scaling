package xiao.armorscaling.common.scaling;

import xiao.armorscaling.api.scaling.IScalingSubManager;

public abstract class AbstractScalingManager implements IScalingSubManager {

    protected boolean configPrepared = false;

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
}
