package xiao.armorscaling.config.common.armorscaling;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xiao.armorscaling.ArmorScaling;
import xiao.armorscaling.api.config.common.armorscaling.ArmorScalingConfigTag;
import xiao.armorscaling.api.config.common.armorscaling.IArmorScalingConfigManager;
import xiao.armorscaling.api.config.common.armorscaling.IArmorScalingSingleEntry;
import xiao.armorscaling.api.scaling.IArmorScalingManager;
import xiao.armorscaling.config.ConfigSubManager;
import xiao.armorscaling.config.ModConfigManager;
import xiao.armorscaling.config.common.armorscaling.defaultconfigs.DefaultArmorScalingConfigGenerator;
import xiao.armorscaling.config.common.armorscaling.type.DamageScalingEntry;
import xiao.armorscaling.config.common.armorscaling.type.DurabilityScalingEntry;
import xiao.battleroyale.api.common.McSide;
import xiao.battleroyale.api.config.sub.IConfigAppliable;
import xiao.battleroyale.config.AbstractSingleConfig;
import xiao.battleroyale.config.FolderConfigData;
import xiao.battleroyale.util.JsonUtils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

public class ArmorScalingConfigManager
        extends ConfigSubManager<ArmorScalingConfigManager.ArmorScalingConfig>
        implements IArmorScalingConfigManager<ArmorScalingConfigManager.ArmorScalingConfig> {

    private static class ArmorScalingConfigManagerHolder {
        private static final ArmorScalingConfigManager INSTANCE = new ArmorScalingConfigManager();
    }

    public static ArmorScalingConfigManager get() {
        return ArmorScalingConfigManagerHolder.INSTANCE;
    }

    private ArmorScalingConfigManager() {
        allFolderConfigData.put(DEFAULT_ARMOR_SCALING_CONFIG_FOLDER, new FolderConfigData<>(DEFAULT_ARMOR_SCALING_CONFIG_FOLDER));
    }

    public static void init(McSide mcSide) {
        ArmorScaling.getModConfigManager().registerConfigSubManager(get());
    }

    public static final String ARMOR_SCALING_CONFIG_PATH = Paths.get(ModConfigManager.MOD_CONFIG_PATH).toString();
    public static final String ARMOR_SCALING_CONFIG_SUB_PATH = "armorScaling";

    protected final int DEFAULT_ARMOR_SCALING_CONFIG_FOLDER = 0;

    public static class ArmorScalingConfig extends AbstractSingleConfig implements IArmorScalingSingleEntry, IConfigAppliable {
        public static final String CONFIG_TYPE = "ArmorScalingConfig";

        public final DamageScalingEntry damageScalingEntry;
        public final DurabilityScalingEntry durabilityScalingEntry;

        public ArmorScalingConfig(int id, String name, String color,
                                  DamageScalingEntry damageScalingEntry,
                                  DurabilityScalingEntry durabilityScalingEntry) {
            this(id, name, color, false, damageScalingEntry, durabilityScalingEntry);
        }
        public ArmorScalingConfig(int id, String name, String color, boolean isDefault,
                                  DamageScalingEntry damageScalingEntry,
                                  DurabilityScalingEntry durabilityScalingEntry) {
            super(id, name, color, isDefault);
            this.damageScalingEntry = damageScalingEntry != null ? damageScalingEntry : new DamageScalingEntry();
            this.durabilityScalingEntry = durabilityScalingEntry != null ? durabilityScalingEntry : new DurabilityScalingEntry();
        }
        @Override public @NotNull ArmorScalingConfig copy() {
            return new ArmorScalingConfig(id, name, color, isDefault, damageScalingEntry.copy(), durabilityScalingEntry.copy());
        }

        @Override
        public DamageScalingEntry getDamageScalingEntry() {
            return this.damageScalingEntry;
        }
        @Override
        public DurabilityScalingEntry getDurabilityScalingEntry() {
            return this.durabilityScalingEntry;
        }

        @Override
        public String getType() {
            return CONFIG_TYPE;
        }

        @Override
        public JsonObject toJson() {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(ArmorScalingConfigTag.ID, id);
            if (isDefault) {
                jsonObject.addProperty(ArmorScalingConfigTag.DEFAULT, isDefault);
            }
            jsonObject.addProperty(ArmorScalingConfigTag.NAME, name);
            jsonObject.addProperty(ArmorScalingConfigTag.COLOR, color);
            if (damageScalingEntry != null) {
                jsonObject.add(ArmorScalingConfigTag.DAMAGE_SCALING_ENTRY, damageScalingEntry.toJson());
            }
            if (durabilityScalingEntry != null) {
                jsonObject.add(ArmorScalingConfigTag.DURABILITY_SCALING_ENTRY, durabilityScalingEntry.toJson());
            }
            return jsonObject;
        }

        public static DamageScalingEntry deserializeDamageScalingEntry(JsonObject jsonObject) {
            try {
                DamageScalingEntry damageScalingEntry = DamageScalingEntry.fromJson(jsonObject);
                if (damageScalingEntry != null) {
                    return damageScalingEntry;
                } else {
                    ArmorScaling.LOGGER.warn("Skipped invalid DamageScalingEntry");
                    return null;
                }
            } catch (Exception e) {
                ArmorScaling.LOGGER.error("Failed to deserialize DamageScalingEntry: {}", e.getMessage());
                return null;
            }
        }

        public static DurabilityScalingEntry deserializeDurabilityScalingEntry(JsonObject jsonObject) {
            try {
                DurabilityScalingEntry durabilityScalingEntry = DurabilityScalingEntry.fromJson(jsonObject);
                if (durabilityScalingEntry != null) {
                    return durabilityScalingEntry;
                } else {
                    ArmorScaling.LOGGER.warn("Skipped invalid DurabilityScalingEntry");
                    return null;
                }
            } catch (Exception e) {
                ArmorScaling.LOGGER.error("Failed to deserialize DurabilityScalingEntry: {}", e.getMessage());
                return null;
            }
        }

        @Override
        public void applyDefault() {
            ArmorScaling.getArmorScalingManager().reloadConfig(this);
        }
    }

    @Override protected Comparator<ArmorScalingConfig> getConfigIdComparator(int folderId) {
        return Comparator.comparingInt(ArmorScalingConfig::getConfigId);
    }

    /**
     * IConfigManager
     */
    @Override public String getFolderType(int folderId) {
        return ArmorScalingConfig.CONFIG_TYPE;
    }

    /**
     * IConfigDefaultable
     */
    @Override public boolean generateDefaultConfigs() {
        return generateDefaultConfigs(DEFAULT_ARMOR_SCALING_CONFIG_FOLDER);
    }

    @Override public boolean generateDefaultConfigs(int folderId) {
        return DefaultArmorScalingConfigGenerator.generateAllDefaultConfigs(String.valueOf(getConfigDirPath()));
    }
    @Override public int getDefaultConfigId() {
        return getDefaultConfigId(DEFAULT_ARMOR_SCALING_CONFIG_FOLDER);
    }

    /**
     * IConfigLoadable
     */
    @Nullable
    @Override
    public ArmorScalingConfig parseConfigEntry(JsonObject configObject, Path filePath, int folderId) {
        try {
            int id = JsonUtils.getJsonInt(configObject, ArmorScalingConfigTag.ID, -1);
            JsonObject damageScalingEntryObject = JsonUtils.getJsonObject(configObject, ArmorScalingConfigTag.DAMAGE_SCALING_ENTRY, null);
            JsonObject durabilityScalingEntryObject = JsonUtils.getJsonObject(configObject, ArmorScalingConfigTag.DURABILITY_SCALING_ENTRY, null);
            if (id < 0 || damageScalingEntryObject == null || durabilityScalingEntryObject == null) {
                ArmorScaling.LOGGER.warn("Skipped invalid armor scaling config in {}", filePath);
                return null;
            }
            boolean isDefault = JsonUtils.getJsonBool(configObject, ArmorScalingConfigTag.DEFAULT, false);
            String name = JsonUtils.getJsonString(configObject, ArmorScalingConfigTag.NAME, "");
            String color = JsonUtils.getJsonString(configObject, ArmorScalingConfigTag.COLOR, "#FFFFFF");
            DamageScalingEntry damageScalingEntry = ArmorScalingConfig.deserializeDamageScalingEntry(damageScalingEntryObject);
            DurabilityScalingEntry durabilityScalingEntry = ArmorScalingConfig.deserializeDurabilityScalingEntry(durabilityScalingEntryObject);
            if (damageScalingEntry == null || durabilityScalingEntry == null) {
                ArmorScaling.LOGGER.error("Failed to deserialize armor scaling entry for id: {} in {}", id, filePath);
                return null;
            }

            return new ArmorScalingConfig(id, name, color, isDefault, damageScalingEntry, durabilityScalingEntry);
        } catch (Exception e) {
            ArmorScaling.LOGGER.error("Error parsing {} entry in {}: {}", getFolderType(folderId), filePath, e.getMessage());
            return null;
        }
    }

    @Override public String getConfigPath(int folderId) {
        return ARMOR_SCALING_CONFIG_PATH;
    }
    @Override public String getConfigSubPath(int folderId) {
        return ARMOR_SCALING_CONFIG_SUB_PATH;
    }

    @Override
    public void initializeDefaultConfigsIfEmpty() {
        super.initializeDefaultConfigsIfEmpty(DEFAULT_ARMOR_SCALING_CONFIG_FOLDER);
    }
}
