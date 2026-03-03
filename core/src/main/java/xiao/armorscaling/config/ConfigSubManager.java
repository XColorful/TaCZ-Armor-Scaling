package xiao.armorscaling.config;

import xiao.armorscaling.ArmorScaling;
import xiao.battleroyale.api.config.sub.IConfigSingleEntry;
import xiao.battleroyale.config.AbstractConfigSubManager;

public abstract class ConfigSubManager<T extends IConfigSingleEntry> extends AbstractConfigSubManager<T> {

    @Override
    public int backupAllConfigs() {
        return backupAllConfigs(ArmorScaling.getModConfigManager().getDefaultBackupRoot());
    }
    @Override
    public boolean backupConfigs(int folderId) {
        return backupConfigs(ArmorScaling.getModConfigManager().getDefaultBackupRoot(), folderId);
    }
}
