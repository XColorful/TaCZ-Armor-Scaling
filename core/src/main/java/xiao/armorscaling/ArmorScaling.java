package xiao.armorscaling;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;
import xiao.battleroyale.api.common.McSide;

import java.util.Random;

public class ArmorScaling {
    public static final String MOD_ID = "armorscaling";
    public static final String MOD_NAME_SHORT = "armorsl";
    public static final Logger LOGGER = LogUtils.getLogger();

    protected static boolean initialized;
    protected static McSide mcSide = McSide.CLIENT;
    public static void init(McSide mcSide) {
        if (initialized) return;

        ArmorScaling.mcSide = mcSide;

        if (mcSide.isClientSide()) {
        }
        if (mcSide.isClientSide()) {
        }

        initialized = true;
    }

    public static McSide getMcSide() {
        return mcSide;
    }
}
