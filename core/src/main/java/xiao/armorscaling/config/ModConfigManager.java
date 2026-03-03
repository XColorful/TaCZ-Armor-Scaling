package xiao.armorscaling.config;

import xiao.armorscaling.config.common.armorscaling.ArmorScalingConfigManager;
import xiao.armorscaling.data.ModDataManager;
import xiao.battleroyale.api.common.McSide;
import xiao.battleroyale.api.config.IModConfigManager;
import xiao.battleroyale.config.AbstractModConfigManager;

import java.nio.file.Paths;

public class ModConfigManager extends AbstractModConfigManager {

    public static String MOD_CONFIG_PATH = "config";

    private static class ModConfigManagerHolder {
        private static final ModConfigManager INSTANCE = new ModConfigManager();
    }

    public static IModConfigManager getApi() {
        return ModConfigManagerHolder.INSTANCE;
    }

    public static void init(McSide mcSide) {
        ArmorScalingConfigManager.init(mcSide);
    }

    public static String configBackupRoot = Paths.get(ModDataManager.MOD_DATA_PATH).resolve("backup").toString();
    @Override public String getDefaultBackupRoot() {
        return configBackupRoot;
    }
}
