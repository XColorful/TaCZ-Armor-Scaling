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
    public final List<DurabilityScaleEntry> durabilityScaleEntries;

    public static class DurabilityScaleEntry {
        public String itemRl;
        public float damagePercent;
        public @Nullable ILootEntry replaceItemLoot;
        public DurabilityScaleEntry(String itemRl, float damagePercent, @Nullable ILootEntry replaceItemLoot) {
            this.itemRl = itemRl;
            this.damagePercent = damagePercent;
            this.replaceItemLoot = replaceItemLoot;
        }
        public DurabilityScaleEntry copy() {
            return new DurabilityScaleEntry(itemRl, damagePercent, replaceItemLoot != null ? replaceItemLoot.copy() : null);
        }
    }


    public DurabilityScalingEntry() {
        this(5, true, true, new ArrayList<>());
    }

    public DurabilityScalingEntry(float healthToDurabilityRatio, boolean useAbsorbedDamageOnly, boolean headShotMultiplierScaling,
                                  List<DurabilityScaleEntry> durabilityScaleEntries) {
        this.healthToDurabilityRatio = healthToDurabilityRatio;
        this.useAbsorbedDamageOnly = useAbsorbedDamageOnly;
        this.headShotMultiplierScaling = headShotMultiplierScaling;
        this.durabilityScaleEntries = durabilityScaleEntries;
    }
    @Override public @NotNull DurabilityScalingEntry copy() {
        List<DurabilityScaleEntry> durabilityScaleEntriesCopy = new ArrayList<>(durabilityScaleEntries.size());
        for (DurabilityScaleEntry entry : durabilityScaleEntries) {
            durabilityScaleEntriesCopy.add(entry.copy());
        }
        return new DurabilityScalingEntry(healthToDurabilityRatio, useAbsorbedDamageOnly, headShotMultiplierScaling,
                durabilityScaleEntriesCopy);
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
            scaleObject.add(DurabilityScalingEntryTag.REPLACE_ITEM_LOOT, entry.replaceItemLoot != null ? entry.replaceItemLoot.toJson() : new JsonObject());
            jsonArray.add(scaleObject);
        }
        jsonObject.add(DurabilityScalingEntryTag.DURABILITY_SCALE_DATA, jsonArray);

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

                durabilityScaleEntries.add(new DurabilityScaleEntry(itemRl, damagePercent, replaceItemLoot));
            }
        }
        return new DurabilityScalingEntry(healthToDurabilityRatio, useAbsorbedDamageOnly, headShotMultiplierScaling,
                durabilityScaleEntries);
    }
}
