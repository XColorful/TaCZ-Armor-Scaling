package xiao.armorscaling.common.scaling.durability;

import xiao.armorscaling.ArmorScaling;
import xiao.armorscaling.api.scaling.durability.IDurabilityScalingManager;
import xiao.armorscaling.common.scaling.AbstractScalingManager;
import xiao.armorscaling.config.common.armorscaling.ArmorScalingConfigManager;
import xiao.battleroyale.api.common.McSide;

public class DurabilityScalingManager extends AbstractScalingManager implements IDurabilityScalingManager {

    private static class DurabilityScalingManagerHolder {
        private static final DurabilityScalingManager INSTANCE = new DurabilityScalingManager();
    }

    public static DurabilityScalingManager get() {
        return DurabilityScalingManagerHolder.INSTANCE;
    }

    protected DurabilityScalingManager() {
    }

    public static void init(McSide mcSide) {
    }

    @Override public String getManagerName() {
        return String.format("%s:DurabilityScalingManager", ArmorScaling.MOD_ID);
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