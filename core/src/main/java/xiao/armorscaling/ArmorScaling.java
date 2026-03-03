package xiao.armorscaling;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;
import xiao.armorscaling.api.compat.tacz.ITaczEventRegister;
import xiao.battleroyale.api.common.McSide;

public class ArmorScaling {
    public static final String MOD_ID = "armorscaling";
    public static final String MOD_NAME_SHORT = "armorsl";
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

        if (mcSide.isClientSide()) {
        }
        if (mcSide.isClientSide()) {
        }

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
}
