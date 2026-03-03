package xiao.armorscaling.data.io;

import xiao.armorscaling.data.ModDataManager;

public class TempDataManager extends ModDataManager {

    public static final String TEMP_DATA_SUB_PATH = "temp";

    private static class TempDataManagerHolder {
        private static final TempDataManager INSTANCE = new TempDataManager();
    }

    public static TempDataManager get() {
        return TempDataManagerHolder.INSTANCE;
    }

    private TempDataManager() {
        this.reloadData();
    }

    @Override
    protected String getSubPath() {
        return TEMP_DATA_SUB_PATH;
    }

    public void saveTempData() {
        super.saveData();
    }

    public void clearTempData() {
        super.clearData();
    }
}
