package xiao.armorscaling;

import com.mojang.logging.LogUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import xiao.armorscaling.api.compat.tacz.ITaczEventRegister;
import xiao.armorscaling.config.ModConfigManager;
import xiao.battleroyale.api.common.McSide;
import xiao.battleroyale.api.config.IModConfigManager;

public class ArmorScaling {
    public static final String MOD_ID = "armorscaling";
    public static final String MOD_NAME_SHORT = "arsl";
    public static final Logger LOGGER = LogUtils.getLogger();

    protected static boolean initialized;
    protected static McSide mcSide = McSide.CLIENT;
    public record CompatApi(ITaczEventRegister taczEventRegister) {}
    private static CompatApi compatApi;

    public static void init(McSide mcSide,
                            CompatApi compatApi) {
        if (initialized) return;

        ArmorScaling.mcSide = mcSide;

        ArmorScaling.compatApi = compatApi;

        modConfigManager = ModConfigManager.getApi();
        ModConfigManager.init(mcSide);

        initialized = true;
    }

    public static McSide getMcSide() {
        return mcSide;
    }
    public static CompatApi getCompatApi() {
        if (compatApi == null) {
            throw new IllegalStateException("Compat api has not initialized. Call init() first.");
        }
        return compatApi;
    }

    private static IModConfigManager modConfigManager;
    public static IModConfigManager getModConfigManager() {
        return modConfigManager;
    }
    @Deprecated(forRemoval = false)
    public static void setModConfigManager(@NotNull IModConfigManager modConfigManager) {
        ArmorScaling.modConfigManager = modConfigManager;
    }
}
