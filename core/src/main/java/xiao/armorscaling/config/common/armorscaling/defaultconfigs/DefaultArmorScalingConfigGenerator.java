package xiao.armorscaling.config.common.armorscaling.defaultconfigs;

public class DefaultArmorScalingConfigGenerator {

    public static boolean generateAllDefaultConfigs(String configDirPath) {
        generateDefaultArmorScalingConfigs(configDirPath);
        return true;
    }

    public static void generateDefaultArmorScalingConfigs(String configDirPath) {
        DefaultArmorScaling.generateDefaultConfigs(configDirPath);
        PubgArmorScaling.generateDefaultConfigs(configDirPath);
    }
}
