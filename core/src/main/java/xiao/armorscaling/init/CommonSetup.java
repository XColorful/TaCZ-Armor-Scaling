package xiao.armorscaling.init;

import xiao.armorscaling.ArmorScaling;
import xiao.armorscaling.data.io.TempDataManager;
import xiao.battleroyale.api.init.ICommonSetup;

public class CommonSetup implements ICommonSetup {

    private static final CommonSetup INSTANCE = new CommonSetup();

    public static CommonSetup get() {
        return INSTANCE;
    }

    private CommonSetup() {}

    @Override
    public void onCommonSetup() {
        ArmorScaling.LOGGER.debug("onCommonSetup, reloadAllConfigs");
        ArmorScaling.getModConfigManager().reloadAllConfigs();
        TempDataManager.get().saveTempData();
    }
}
