package xiao.armorscaling.common.scaling.damage;

import xiao.armorscaling.ArmorScaling;
import xiao.armorscaling.api.event.custom.bullethandler.DamageScalingEvent;
import xiao.armorscaling.api.scaling.damage.IDamageScalingManager;
import xiao.armorscaling.common.scaling.AbstractScalingManager;
import xiao.armorscaling.config.common.armorscaling.ArmorScalingConfigManager;
import xiao.battleroyale.BattleRoyale;
import xiao.battleroyale.api.common.McSide;
import xiao.battleroyale.api.event.CustomEventType;
import xiao.battleroyale.api.event.ICustomEvent;
import xiao.battleroyale.api.event.ICustomEventHandler;

public class DamageScalingManager extends AbstractScalingManager implements IDamageScalingManager, ICustomEventHandler {

    private static class DamageScalingManagerHolder {
        private static final DamageScalingManager INSTANCE = new DamageScalingManager();
    }

    public static DamageScalingManager get() {
        return DamageScalingManagerHolder.INSTANCE;
    }

    protected DamageScalingManager() {
    }

    public static void init(McSide mcSide) {
    }

    @Override public String getManagerName() {
        return String.format("%s:DamageScalingManager", ArmorScaling.MOD_ID);
    }

    @Override public String getEventHandlerName() {
        return String.format("%s:DurabilityScalingManager", ArmorScaling.MOD_ID);
    }
    @Override public void handleEvent(CustomEventType customEventType, ICustomEvent event) {
        if (customEventType == CustomEventType.CUSTOM_EVENT) {
            if (event instanceof DamageScalingEvent eventIn) {
                onDamageScaling(eventIn);
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
        BattleRoyale.getEventRegister().register(get(), DamageScalingEvent.class);
        return true;
    }

    @Override
    protected boolean unregisterEvents() {
        BattleRoyale.getEventRegister().unregister(get(), DamageScalingEvent.class);
        return true;
    }

    @Override
    public void reloadConfig(ArmorScalingConfigManager.ArmorScalingConfig config) {
    }

    protected void onDamageScaling(DamageScalingEvent event) {
    }
}