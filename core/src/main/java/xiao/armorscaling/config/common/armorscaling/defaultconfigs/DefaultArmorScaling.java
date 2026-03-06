package xiao.armorscaling.config.common.armorscaling.defaultconfigs;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import xiao.armorscaling.config.common.armorscaling.ArmorScalingConfigManager.ArmorScalingConfig;
import xiao.armorscaling.config.common.armorscaling.type.DamageScalingEntry;
import xiao.armorscaling.config.common.armorscaling.type.DamageScalingEntry.DamageScaleEntry;
import xiao.armorscaling.config.common.armorscaling.type.DurabilityScalingEntry;
import xiao.armorscaling.config.common.armorscaling.type.DurabilityScalingEntry.DurabilityScaleEntry;
import xiao.battleroyale.api.minecraft.EquipmentLevel;
import xiao.battleroyale.util.JsonUtils;

import java.nio.file.Paths;
import java.util.Arrays;

public class DefaultArmorScaling {

    private static final String DEFAULT_FILE_NAME = "example.json";

    public static void generateDefaultConfigs(String configDirPath) {
        JsonArray armorScalingConfigJson = new JsonArray();
        armorScalingConfigJson.add(generateDefaultArmorScaling());
        JsonUtils.writeJsonToFile(Paths.get(configDirPath, DEFAULT_FILE_NAME).toString(), armorScalingConfigJson);
    }

    private static JsonObject generateDefaultArmorScaling() {
        DamageScalingEntry damageScalingEntry = new DamageScalingEntry(Arrays.asList(
                new DamageScaleEntry("minecraft:iron_helmet", 0.6F),
                new DamageScaleEntry("minecraft:diamond_chestplate", 0.45F),
                new DamageScaleEntry("minecraft:chainmail_chestplate", 0.8F)
        ));

        DurabilityScalingEntry durabilityScalingEntry = new DurabilityScalingEntry(
                5, true, true,
                Arrays.asList(
                        new DurabilityScaleEntry("minecraft:iron_helmet", 150, null),
                        new DurabilityScaleEntry("minecraft:diamond_chestplate", 250,
                                EquipmentLevel.equipment(EquipmentLevel.CHAINMAIL, EquipmentLevel.CHESTPLATE, 1)
                        )
                )
        );

        ArmorScalingConfig armorScalingConfig = new ArmorScalingConfig(0, "Example Armor Scaling config", "#FFFFFF",
                damageScalingEntry, durabilityScalingEntry);

        return armorScalingConfig.toJson();
    }
}
