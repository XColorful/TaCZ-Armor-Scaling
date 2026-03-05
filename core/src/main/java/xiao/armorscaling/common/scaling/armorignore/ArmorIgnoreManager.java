package xiao.armorscaling.common.scaling.armorignore;

import xiao.armorscaling.ArmorScaling;
import xiao.armorscaling.api.event.custom.bullethandler.ArmorIgnoreEvent;
import xiao.armorscaling.api.scaling.armorignore.IArmorIgnoreManager;
import xiao.armorscaling.common.scaling.AbstractScalingManager;
import xiao.armorscaling.config.common.armorscaling.ArmorScalingConfigManager;
import xiao.armorscaling.data.io.TempDataManager;
import xiao.battleroyale.BattleRoyale;
import xiao.battleroyale.api.common.McSide;
import xiao.battleroyale.api.event.CustomEventType;
import xiao.battleroyale.api.event.ICustomEvent;
import xiao.battleroyale.api.event.ICustomEventHandler;

import static xiao.armorscaling.api.data.TempDataTag.*;

public class ArmorIgnoreManager extends AbstractScalingManager implements IArmorIgnoreManager, ICustomEventHandler {

    private static class ArmorIgnoreManagerHolder {
        private static final ArmorIgnoreManager INSTANCE = new ArmorIgnoreManager();
    }

    public static ArmorIgnoreManager get() {
        return ArmorIgnoreManagerHolder.INSTANCE;
    }

    protected ArmorIgnoreManager() {
        TempDataManager tempDataManager = TempDataManager.get();
        Boolean enable = tempDataManager.getBool(TACZ_ARMOR_SCALING, TURN_ARMOR_IGNORE);
        this.isEnabled = enable != null ? enable : false;
        Double ratio = tempDataManager.getDouble(TACZ_ARMOR_SCALING, ARMOR_IGNORE_SCALE);
        this.setArmorIgnoreScaleInternal(ratio != null ? (float) ((double) ratio) : 1);
    }

    public static void init(McSide mcSide) {
    }

    protected float armorIgnoreScale;

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
    protected void saveEnabled(boolean isEnabled) {
        TempDataManager tempDataManager = TempDataManager.get();
        tempDataManager.writeBool(TACZ_ARMOR_SCALING, TURN_ARMOR_IGNORE, isEnabled);
        tempDataManager.saveTempData();
    }

    @Override
    public float getArmorIgnoreScale() {
        return this.armorIgnoreScale;
    }

    @Override
    public void setArmorIgnoreScale(float ratio) {
        this.setArmorIgnoreScaleInternal(ratio);
        TempDataManager tempDataManager = TempDataManager.get();
        tempDataManager.writeDouble(TACZ_ARMOR_SCALING, ARMOR_IGNORE_SCALE, ratio);
        tempDataManager.saveTempData();
    }
    private void setArmorIgnoreScaleInternal(float ratio) {
        this.armorIgnoreScale = Math.min(0, Math.max(ratio, 1));
    }

    @Override
    public void reloadConfig(ArmorScalingConfigManager.ArmorScalingConfig config) {
    }

    protected void onArmorIgnore(ArmorIgnoreEvent event) {
        if (!isEnabled()) return;
        event.setArmorIgnorePercent(this.armorIgnoreScale);
        event.setHandled();
    }
}