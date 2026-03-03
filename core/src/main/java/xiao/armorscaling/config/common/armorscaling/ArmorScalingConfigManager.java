package xiao.armorscaling.config.common.armorscaling;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xiao.armorscaling.ArmorScaling;
import xiao.armorscaling.api.config.common.armorscaling.ArmorScalingConfigTag;
import xiao.armorscaling.api.config.common.armorscaling.IArmorScalingConfigManager;
import xiao.armorscaling.api.config.common.armorscaling.IArmorScalingSingleEntry;
import xiao.armorscaling.config.ConfigSubManager;
import xiao.armorscaling.config.ModConfigManager;
import xiao.armorscaling.config.common.armorscaling.defaultconfigs.DefaultArmorScalingConfigGenerator;
import xiao.battleroyale.api.common.McSide;
import xiao.battleroyale.config.AbstractSingleConfig;
import xiao.battleroyale.config.FolderConfigData;
import xiao.battleroyale.util.JsonUtils;

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

    public static class ArmorScalingConfig extends AbstractSingleConfig implements IArmorScalingSingleEntry {
        public static final String CONFIG_TYPE = "ArmorScalingConfig";

        public ArmorScalingConfig(int id, String name, String color) {
            this(id, name, color, false);
        }
        public ArmorScalingConfig(int id, String name, String color, boolean isDefault) {
            super(id, name, color, isDefault);
        }
        @Override public @NotNull ArmorScalingConfig copy() {
            return new ArmorScalingConfig(id, name, color, isDefault);
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
            return jsonObject;
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
    public ArmorScalingConfig parseConfigEntry(JsonObject configObject, java.nio.file.Path filePath, int folderId) {
        try {
            int id = JsonUtils.getJsonInt(configObject, ArmorScalingConfigTag.ID, -1);
            if (id < 0) {
                ArmorScaling.LOGGER.warn("Skipped invalid armor scaling config in {}", filePath);
                return null;
            }
            boolean isDefault = JsonUtils.getJsonBool(configObject, ArmorScalingConfigTag.DEFAULT, false);
            String name = JsonUtils.getJsonString(configObject, ArmorScalingConfigTag.NAME, "");
            String color = JsonUtils.getJsonString(configObject, ArmorScalingConfigTag.COLOR, "#FFFFFF");

            return new ArmorScalingConfig(id, name, color, isDefault);
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
