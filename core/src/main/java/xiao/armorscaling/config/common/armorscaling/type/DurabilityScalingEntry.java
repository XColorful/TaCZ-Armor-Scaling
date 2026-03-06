package xiao.armorscaling.config.common.armorscaling.type;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xiao.armorscaling.api.config.common.armorscaling.DurabilityScalingEntryTag;
import xiao.armorscaling.api.config.common.armorscaling.IArmorScalingEntry;
import xiao.battleroyale.api.config.common.loot.ILootEntry;
import xiao.battleroyale.config.common.loot.LootConfigManager;
import xiao.battleroyale.util.JsonUtils;

import java.util.ArrayList;
import java.util.List;

public class DurabilityScalingEntry implements IArmorScalingEntry {
    public float healthToDurabilityRatio;
    public boolean useAbsorbedDamageOnly;
    public boolean headShotMultiplierScaling;
    public final @NotNull List<DurabilityScaleEntry> durabilityScaleEntries;
    public final @NotNull List<LootDataEntry> replaceItemLootData;

    public static class DurabilityScaleEntry {
        public String itemRl;
        public float damagePercent;
        public int lootDataId;
        public @Nullable ILootEntry replaceItemLoot;
        public DurabilityScaleEntry(String itemRl, float damagePercent, @Nullable ILootEntry replaceItemLoot) {
            this(itemRl, damagePercent, replaceItemLoot, -1);
        }
        public DurabilityScaleEntry(String itemRl, float damagePercent, int lootDataId) {
            this(itemRl, damagePercent, null, lootDataId);
        }
        protected DurabilityScaleEntry(String itemRl, float damagePercent, @Nullable ILootEntry replaceItemLoot, int lootDataId) {
            this.itemRl = itemRl;
            this.damagePercent = damagePercent;
            this.replaceItemLoot = replaceItemLoot;
            this.lootDataId = lootDataId;
        }
        public DurabilityScaleEntry copy() {
            return new DurabilityScaleEntry(itemRl, damagePercent, replaceItemLoot != null ? replaceItemLoot.copy() : null);
        }
    }

    public static class LootDataEntry {
        public int lootDataId;
        public @NotNull ILootEntry lootEntry;
        public LootDataEntry(int lootDataId, @NotNull ILootEntry lootEntry) {
            this.lootDataId = lootDataId;
            this.lootEntry = lootEntry;
        }
        public LootDataEntry copy() {
            return new LootDataEntry(lootDataId, lootEntry.copy());
        }
    }


    public DurabilityScalingEntry() {
        this(5, true, true, new ArrayList<>(), new ArrayList<>());
    }

    public DurabilityScalingEntry(float healthToDurabilityRatio, boolean useAbsorbedDamageOnly, boolean headShotMultiplierScaling,
                                  @Nullable List<DurabilityScaleEntry> durabilityScaleEntries, @Nullable List<LootDataEntry> replaceItemLootData) {
        this.healthToDurabilityRatio = healthToDurabilityRatio;
        this.useAbsorbedDamageOnly = useAbsorbedDamageOnly;
        this.headShotMultiplierScaling = headShotMultiplierScaling;
        this.durabilityScaleEntries = durabilityScaleEntries != null ? durabilityScaleEntries : new ArrayList<>();
        this.replaceItemLootData = replaceItemLootData != null ? replaceItemLootData : new ArrayList<>();
    }
    @Override public @NotNull DurabilityScalingEntry copy() {
        List<DurabilityScaleEntry> durabilityScaleEntriesCopy = new ArrayList<>(durabilityScaleEntries.size());
        for (DurabilityScaleEntry entry : durabilityScaleEntries) {
            durabilityScaleEntriesCopy.add(entry.copy());
        }
        List<LootDataEntry> replaceItemLootDataCopy = new ArrayList<>(replaceItemLootData.size());
        for (LootDataEntry lootData : replaceItemLootData) {
            replaceItemLootDataCopy.add(lootData.copy());
        }
        return new DurabilityScalingEntry(healthToDurabilityRatio, useAbsorbedDamageOnly, headShotMultiplierScaling,
                durabilityScaleEntriesCopy, replaceItemLootDataCopy);
    }

    @Override
    public String getType() {
        return "durabilityScalingEntry";
    }

    @Override
    public JsonObject toJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(DurabilityScalingEntryTag.HEALTH_TO_DURABILITY_RATIO, healthToDurabilityRatio);
        jsonObject.addProperty(DurabilityScalingEntryTag.USE_ABSORBED_DAMAGE_ONLY, useAbsorbedDamageOnly);
        jsonObject.addProperty(DurabilityScalingEntryTag.HEADSHOT_MULTIPLIER_SCALING, headShotMultiplierScaling);

        JsonArray jsonArray = new JsonArray();
        for (DurabilityScaleEntry entry : durabilityScaleEntries) {
            JsonObject scaleObject = new JsonObject();
            scaleObject.addProperty(DurabilityScalingEntryTag.ITEM_RL, entry.itemRl);
            scaleObject.addProperty(DurabilityScalingEntryTag.DAMAGE_PERCENT, entry.damagePercent);
            if (entry.lootDataId < 0 || entry.replaceItemLoot != null) {
                scaleObject.add(DurabilityScalingEntryTag.REPLACE_ITEM_LOOT, entry.replaceItemLoot != null ? entry.replaceItemLoot.toJson() : new JsonObject());
            }
            if (entry.lootDataId >= 0) {
                scaleObject.addProperty(DurabilityScalingEntryTag.LOOT_DATA_ID, entry.lootDataId);
            }
            jsonArray.add(scaleObject);
        }
        jsonObject.add(DurabilityScalingEntryTag.DURABILITY_SCALE_DATA, jsonArray);

        jsonArray = new JsonArray();
        for (LootDataEntry lootData : replaceItemLootData) {
            JsonObject lootDataObject = new JsonObject();
            lootDataObject.addProperty(DurabilityScalingEntryTag.LOOT_DATA_ID, lootData.lootDataId);
            lootDataObject.add(DurabilityScalingEntryTag.REPLACE_ITEM_LOOT, lootData.lootEntry.toJson());
            jsonArray.add(lootDataObject);
        }
        jsonObject.add(DurabilityScalingEntryTag.REPLACE_ITEM_LOOT_DATA, jsonArray);

        return jsonObject;
    }

    public static DurabilityScalingEntry fromJson(JsonObject jsonObject) {
        float healthToDurabilityRatio = (float) JsonUtils.getJsonDouble(jsonObject, DurabilityScalingEntryTag.HEALTH_TO_DURABILITY_RATIO, 5);
        boolean useAbsorbedDamageOnly = JsonUtils.getJsonBoolean(jsonObject, DurabilityScalingEntryTag.USE_ABSORBED_DAMAGE_ONLY, true);
        boolean headShotMultiplierScaling = JsonUtils.getJsonBoolean(jsonObject, DurabilityScalingEntryTag.HEADSHOT_MULTIPLIER_SCALING, true);

        List<DurabilityScaleEntry> durabilityScaleEntries = new ArrayList<>();
        JsonArray scaleArray = JsonUtils.getJsonArray(jsonObject, DurabilityScalingEntryTag.DURABILITY_SCALE_DATA, null);
        if (scaleArray != null) {
            for (JsonElement element : scaleArray) {
                if (!element.isJsonObject()) continue;
                JsonObject scaleObject = element.getAsJsonObject();

                String itemRl = JsonUtils.getJsonString(scaleObject, DurabilityScalingEntryTag.ITEM_RL, null);
                if (itemRl == null) continue;

                float damagePercent = (float) JsonUtils.getJsonDouble(scaleObject, DurabilityScalingEntryTag.DAMAGE_PERCENT, 100);
                @Nullable JsonObject lootObject = JsonUtils.getJsonObject(scaleObject, DurabilityScalingEntryTag.REPLACE_ITEM_LOOT, null);
                @Nullable ILootEntry replaceItemLoot = LootConfigManager.LootConfig.deserializeLootEntry(lootObject);
                int lootDataId = JsonUtils.getJsonInt(scaleObject, DurabilityScalingEntryTag.LOOT_DATA_ID, -1);

                durabilityScaleEntries.add(new DurabilityScaleEntry(itemRl, damagePercent, replaceItemLoot, lootDataId));
            }
        }

        List<LootDataEntry> replaceItemLootData = new ArrayList<>();
        JsonArray lootDataArray = JsonUtils.getJsonArray(jsonObject, DurabilityScalingEntryTag.REPLACE_ITEM_LOOT_DATA, null);
        if (lootDataArray != null) {
            for (JsonElement element : lootDataArray) {
                if (!element.isJsonObject()) continue;
                JsonObject lootDataObject = element.getAsJsonObject();

                int lootDataId = JsonUtils.getJsonInt(lootDataObject, DurabilityScalingEntryTag.LOOT_DATA_ID, -1);
                @Nullable JsonObject lootObject = JsonUtils.getJsonObject(lootDataObject, DurabilityScalingEntryTag.REPLACE_ITEM_LOOT, null);
                @Nullable ILootEntry lootEntry = LootConfigManager.LootConfig.deserializeLootEntry(lootObject);
                if (lootEntry != null) {
                    replaceItemLootData.add(new LootDataEntry(lootDataId, lootEntry));
                }
            }
        }

        return new DurabilityScalingEntry(healthToDurabilityRatio, useAbsorbedDamageOnly, headShotMultiplierScaling,
                durabilityScaleEntries, replaceItemLootData);
    }
}
