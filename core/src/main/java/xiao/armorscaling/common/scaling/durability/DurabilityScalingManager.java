package xiao.armorscaling.common.scaling.durability;

import xiao.armorscaling.ArmorScaling;
import xiao.armorscaling.api.event.custom.bullethandler.DurabilityScalingEvent;
import xiao.armorscaling.api.scaling.durability.IDurabilityScalingManager;
import xiao.armorscaling.common.scaling.AbstractScalingManager;
import xiao.armorscaling.config.common.armorscaling.ArmorScalingConfigManager;
import xiao.battleroyale.BattleRoyale;
import xiao.battleroyale.api.common.McSide;
import xiao.battleroyale.api.event.CustomEventType;
import xiao.battleroyale.api.event.ICustomEvent;
import xiao.battleroyale.api.event.ICustomEventHandler;

public class DurabilityScalingManager extends AbstractScalingManager implements IDurabilityScalingManager, ICustomEventHandler {

    private static class DurabilityScalingManagerHolder {
        private static final DurabilityScalingManager INSTANCE = new DurabilityScalingManager();
    }

    public static DurabilityScalingManager get() {
        return DurabilityScalingManagerHolder.INSTANCE;
    }

    protected DurabilityScalingManager() {
    }

    public static void init(McSide mcSide) {
    }

    @Override public String getManagerName() {
        return String.format("%s:DurabilityScalingManager", ArmorScaling.MOD_ID);
    }

    @Override public String getEventHandlerName() {
        return String.format("%s:DurabilityScalingManager", ArmorScaling.MOD_ID);
    }
    @Override public void handleEvent(CustomEventType customEventType, ICustomEvent event) {
        if (customEventType == CustomEventType.CUSTOM_EVENT) {
            if (event instanceof DurabilityScalingEvent eventIn) {
                onDurabilityScaling(eventIn);
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
        BattleRoyale.getEventRegister().register(get(), DurabilityScalingEvent.class);
        return true;
    }

    @Override
    protected boolean unregisterEvents() {
        BattleRoyale.getEventRegister().unregister(get(), DurabilityScalingEvent.class);
        return true;
    }

    @Override
    public void reloadConfig(ArmorScalingConfigManager.ArmorScalingConfig config) {
    }

    protected void onDurabilityScaling(DurabilityScalingEvent event) {
    }
}