package xiao.armorscaling.common.scaling.damage;

import xiao.armorscaling.ArmorScaling;
import xiao.armorscaling.api.scaling.damage.IDamageScalingManager;
import xiao.armorscaling.common.scaling.AbstractScalingManager;
import xiao.armorscaling.config.common.armorscaling.ArmorScalingConfigManager;
import xiao.battleroyale.api.common.McSide;

public class DamageScalingManager extends AbstractScalingManager implements IDamageScalingManager {

    private static class DamageScalingManagerHolder {
        private static final DamageScalingManager INSTANCE = new DamageScalingManager();
    }

    public static DamageScalingManager get() {
        return DamageScalingManagerHolder.INSTANCE;
    }

    protected DamageScalingManager() {
    }

    public static void init(McSide mcSide) {
    }

    @Override public String getManagerName() {
        return String.format("%s:DamageScalingManager", ArmorScaling.MOD_ID);
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