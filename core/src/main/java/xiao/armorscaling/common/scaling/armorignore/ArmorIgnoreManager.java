package xiao.armorscaling.common.scaling.armorignore;

import xiao.armorscaling.ArmorScaling;
import xiao.armorscaling.api.event.custom.bullethandler.ArmorIgnoreEvent;
import xiao.armorscaling.api.scaling.armorignore.IArmorIgnoreManager;
import xiao.armorscaling.common.scaling.AbstractScalingManager;
import xiao.armorscaling.config.common.armorscaling.ArmorScalingConfigManager;
import xiao.battleroyale.BattleRoyale;
import xiao.battleroyale.api.common.McSide;
import xiao.battleroyale.api.event.CustomEventType;
import xiao.battleroyale.api.event.ICustomEvent;
import xiao.battleroyale.api.event.ICustomEventHandler;

public class ArmorIgnoreManager extends AbstractScalingManager implements IArmorIgnoreManager, ICustomEventHandler {

    private static class ArmorIgnoreManagerHolder {
        private static final ArmorIgnoreManager INSTANCE = new ArmorIgnoreManager();
    }

    public static ArmorIgnoreManager get() {
        return ArmorIgnoreManagerHolder.INSTANCE;
    }

    protected ArmorIgnoreManager() {
    }

    public static void init(McSide mcSide) {
    }

    @Override public String getManagerName() {
        return String.format("%s:ArmorIgnoreManager", ArmorScaling.MOD_ID);
    }

    @Override public String getEventHandlerName() {
        return String.format("%s:DurabilityScalingManager", ArmorScaling.MOD_ID);
    }
    @Override public void handleEvent(CustomEventType customEventType, ICustomEvent event) {
        if (customEventType == CustomEventType.CUSTOM_EVENT) {
            if (event instanceof ArmorIgnoreEvent eventIn) {
                onArmorIgnore(eventIn);
            }
        } else {
            onReceiveWrongEvent(customEventType);
        }
    }

    @Override
    public void clearConfig() {
    }

    @Override
    public boolean registerToMod() {
        BattleRoyale.getEventRegister().register(get(), ArmorIgnoreEvent.class);
        return true;
    }

    @Override
    protected boolean unregisterEvents() {
        BattleRoyale.getEventRegister().unregister(get(), ArmorIgnoreEvent.class);
        return true;
    }

    @Override
    public void reloadConfig(ArmorScalingConfigManager.ArmorScalingConfig config) {
    }

    protected void onArmorIgnore(ArmorIgnoreEvent event) {
        event.setHandled();
    }
}