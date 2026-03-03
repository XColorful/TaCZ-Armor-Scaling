package xiao.armorscaling.config.common.armorscaling.defaultconfigs;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import xiao.armorscaling.config.common.armorscaling.ArmorScalingConfigManager.ArmorScalingConfig;
import xiao.battleroyale.util.JsonUtils;

import java.nio.file.Paths;

public class DefaultArmorScaling {

    private static final String DEFAULT_FILE_NAME = "example.json";

    public static void generateDefaultConfigs(String configDirPath) {
        JsonArray armorScalingConfigJson = new JsonArray();
        armorScalingConfigJson.add(generateDefaultArmorScaling());
        JsonUtils.writeJsonToFile(Paths.get(configDirPath, DEFAULT_FILE_NAME).toString(), armorScalingConfigJson);
    }

    private static JsonObject generateDefaultArmorScaling() {
        ArmorScalingConfig armorScalingConfig = new ArmorScalingConfig(0, "Example Armor Scaling config", "#FFFFFF");

        return armorScalingConfig.toJson();
    }
}
