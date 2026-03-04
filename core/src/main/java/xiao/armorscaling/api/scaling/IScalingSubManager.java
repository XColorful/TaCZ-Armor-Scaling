package xiao.armorscaling.api.scaling;

import xiao.armorscaling.config.common.armorscaling.ArmorScalingConfigManager.ArmorScalingConfig;

public interface IScalingSubManager {

    String getManagerName();

    void reloadConfig(ArmorScalingConfig config);

    boolean isConfigPrepared();

    void clearConfig();

    boolean registerToMod();

    boolean unregisterToMod();
}
