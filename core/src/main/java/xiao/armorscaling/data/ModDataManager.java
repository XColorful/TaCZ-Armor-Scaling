package xiao.armorscaling.data;

import xiao.battleroyale.data.AbstractDataManager;

public abstract class ModDataManager extends AbstractDataManager {

    public static String MOD_DATA_PATH = "armorscaling";

    public ModDataManager() {
        super(MOD_DATA_PATH);
    }
}
