package xiao.armorscaling.config.common.armorscaling.defaultconfigs;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import xiao.armorscaling.config.common.armorscaling.ArmorScalingConfigManager;
import xiao.armorscaling.config.common.armorscaling.type.DamageScalingEntry;
import xiao.armorscaling.config.common.armorscaling.type.DurabilityScalingEntry;
import xiao.battleroyale.api.config.common.loot.ILootEntry;
import xiao.battleroyale.api.minecraft.EquipmentLevel;
import xiao.battleroyale.util.JsonUtils;

import java.nio.file.Paths;
import java.util.Arrays;

public class PubgArmorScaling {

    private static final String DEFAULT_FILE_NAME = "example_pubg_armor_scaling.json";

    public static void generateDefaultConfigs(String configDirPath) {
        JsonArray armorScalingConfigJson = new JsonArray();
        armorScalingConfigJson.add(generatePubgArmorScaling());
        JsonUtils.writeJsonToFile(Paths.get(configDirPath, DEFAULT_FILE_NAME).toString(), armorScalingConfigJson);
    }

    private static JsonObject generatePubgArmorScaling() {
        DamageScalingEntry damageScalingEntry = new DamageScalingEntry(Arrays.asList(
                // 三级套
                new DamageScalingEntry.DamageScaleEntry("minecraft:netherite_helmet", 0.45F),
                new DamageScalingEntry.DamageScaleEntry("minecraft:diamond_chestplate", 0.45F),
                // 二级套
                new DamageScalingEntry.DamageScaleEntry("minecraft:iron_helmet", 0.60F),
                new DamageScalingEntry.DamageScaleEntry("minecraft:iron_chestplate", 0.60F),
                // 一级套
                new DamageScalingEntry.DamageScaleEntry("minecraft:leather_helmet", 0.70F),
                new DamageScalingEntry.DamageScaleEntry("minecraft:leather_chestplate", 0.70F),
                // 碎甲
                new DamageScalingEntry.DamageScaleEntry("minecraft:chainmail_chestplate", 0.80F)
        ));

        ILootEntry brokenArmor = EquipmentLevel.equipment(EquipmentLevel.CHAINMAIL, EquipmentLevel.CHESTPLATE, 1);
        DurabilityScalingEntry durabilityScalingEntry = new DurabilityScalingEntry(
                5, false, false,
                Arrays.asList(
                        // 三级套
                        new DurabilityScalingEntry.DurabilityScaleEntry("minecraft:netherite_helmet", 230F, null),
                        new DurabilityScalingEntry.DurabilityScaleEntry("minecraft:diamond_chestplate", 250F, 0),
                        // 二级套
                        new DurabilityScalingEntry.DurabilityScaleEntry("minecraft:iron_helmet", 150F, null),
                        new DurabilityScalingEntry.DurabilityScaleEntry("minecraft:iron_chestplate", 220F, 0),
                        // 一级套
                        new DurabilityScalingEntry.DurabilityScaleEntry("minecraft:leather_helmet", 80F, null),
                        new DurabilityScalingEntry.DurabilityScaleEntry("minecraft:leather_chestplate", 200F, 0)
                ),
                Arrays.asList(
                        new DurabilityScalingEntry.LootDataEntry(0, brokenArmor)
                )
        );

        ArmorScalingConfigManager.ArmorScalingConfig armorScalingConfig = new ArmorScalingConfigManager.ArmorScalingConfig(0, "Example PUBG Armor Scaling config", "#FFFFFF",
                damageScalingEntry, durabilityScalingEntry);

        return armorScalingConfig.toJson();
    }
}
