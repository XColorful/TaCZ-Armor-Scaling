package xiao.armorscaling.config.common.armorscaling.type;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import xiao.armorscaling.api.config.common.armorscaling.DamageScalingEntryTag;
import xiao.armorscaling.api.config.common.armorscaling.IArmorScalingEntry;
import xiao.battleroyale.util.JsonUtils;

import java.util.ArrayList;
import java.util.List;

public class DamageScalingEntry implements IArmorScalingEntry {
    public final List<DamageScaleEntry> damageScaleEntries;

    public static class DamageScaleEntry {
        public String itemRl;
        public float scale;
        public DamageScaleEntry(String itemRl, float scale) {
            this.itemRl = itemRl;
            this.scale = scale;
        }
        public DamageScaleEntry copy() {
            return new DamageScaleEntry(itemRl, scale);
        }
    }

    public DamageScalingEntry() {
        this(new ArrayList<>());
    }
    public DamageScalingEntry(List<DamageScaleEntry> damageScaleEntries) {
        this.damageScaleEntries = damageScaleEntries;
    }
    @Override public @NotNull DamageScalingEntry copy() {
        List<DamageScaleEntry> damageScaleEntriesCopy = new ArrayList<>(damageScaleEntries.size());
        for (DamageScaleEntry entry : damageScaleEntries) {
            damageScaleEntriesCopy.add(entry.copy());
        }
        return new DamageScalingEntry(damageScaleEntriesCopy);
    }

    @Override
    public String getType() {
        return "damageScalingEntry";
    }

    @Override
    public JsonObject toJson() {
        JsonObject jsonObject = new JsonObject();
        JsonArray jsonArray = new JsonArray();

        for (DamageScaleEntry entry : damageScaleEntries) {
            JsonObject scaleObject = new JsonObject();
            scaleObject.addProperty(DamageScalingEntryTag.ITEM_RL, entry.itemRl);
            scaleObject.addProperty(DamageScalingEntryTag.SCALE, entry.scale);
            jsonArray.add(scaleObject);
        }

        jsonObject.add(DamageScalingEntryTag.DAMAGE_SCALE_DATA, jsonArray);
        return jsonObject;
    }

    public static DamageScalingEntry fromJson(JsonObject jsonObject) {
        List<DamageScaleEntry> damageScaleEntries = new ArrayList<>();
        JsonArray scaleArray = JsonUtils.getJsonArray(jsonObject, DamageScalingEntryTag.DAMAGE_SCALE_DATA, null);

        if (scaleArray != null) {
            for (JsonElement element : scaleArray) {
                if (!element.isJsonObject()) continue;
                JsonObject scaleObject = element.getAsJsonObject();

                String itemRl = JsonUtils.getJsonString(scaleObject, DamageScalingEntryTag.ITEM_RL, null);
                if (itemRl == null) continue;

                float scale = (float) JsonUtils.getJsonDouble(scaleObject, DamageScalingEntryTag.SCALE, 1.0);
                damageScaleEntries.add(new DamageScaleEntry(itemRl, scale));
            }
        }

        return new DamageScalingEntry(damageScaleEntries);
    }
}
