package xiao.armorscaling.init;

import xiao.armorscaling.compat.tacz.Tacz;
import xiao.battleroyale.api.init.ICompatInit;

public class CompatInit implements ICompatInit {

    private static final CompatInit INSTANCE = new CompatInit();

    public static CompatInit get() {
        return INSTANCE;
    }

    private CompatInit() {}

    @Override
    public void onLoadComplete() {
        Tacz.get().checkLoaded();
    }
}
