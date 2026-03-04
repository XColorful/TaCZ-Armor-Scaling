package xiao.armorscaling.common.scaling.armorignore;

import xiao.armorscaling.ArmorScaling;
import xiao.armorscaling.api.scaling.armorignore.IArmorIgnoreManager;
import xiao.armorscaling.common.scaling.AbstractScalingManager;
import xiao.armorscaling.config.common.armorscaling.ArmorScalingConfigManager;
import xiao.battleroyale.api.common.McSide;

public class ArmorIgnoreManager extends AbstractScalingManager implements IArmorIgnoreManager {

    private static class ArmorIgnoreManagerHolder {
        private static final ArmorIgnoreManager INSTANCE = new ArmorIgnoreManager();
    }

    public static ArmorIgnoreManager get() {
        return ArmorIgnoreManagerHolder.INSTANCE;
    }

    protected ArmorIgnoreManager() {
    }

    public static void init(McSide mcSide) {
    }

    @Override public String getManagerName() {
        return String.format("%s:ArmorIgnoreManager", ArmorScaling.MOD_ID);
    }

    @Override
    public void clearConfig() {
    }

    @Override
    public boolean registerToMod() {
        return true;
    }

    @Override
    protected boolean unregisterEvents() {
        return true;
    }

    @Override
    public void reloadConfig(ArmorScalingConfigManager.ArmorScalingConfig config) {
    }
}