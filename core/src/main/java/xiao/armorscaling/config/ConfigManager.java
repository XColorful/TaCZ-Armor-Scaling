package xiao.armorscaling.config;

import xiao.armorscaling.ArmorScaling;
import xiao.battleroyale.config.AbstractConfigManager;

public abstract class ConfigManager extends AbstractConfigManager {

    @Override
    public int backupAllConfigs() {
        return backupAllConfigs(ArmorScaling.getModConfigManager().getDefaultBackupRoot());
    }

    @Override
    public int backupConfigs(String subManagerNameKey) {
        return backupConfigs(ArmorScaling.getModConfigManager().getDefaultBackupRoot(), subManagerNameKey);
    }
}
