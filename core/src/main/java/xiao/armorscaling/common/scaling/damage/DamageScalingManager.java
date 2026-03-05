package xiao.armorscaling.common.scaling.damage;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import xiao.armorscaling.ArmorScaling;
import xiao.armorscaling.api.event.custom.bullethandler.DamageScalingEvent;
import xiao.armorscaling.api.scaling.damage.IDamageScalingManager;
import xiao.armorscaling.common.scaling.AbstractScalingManager;
import xiao.armorscaling.config.common.armorscaling.ArmorScalingConfigManager;
import xiao.armorscaling.config.common.armorscaling.type.DamageScalingEntry;
import xiao.armorscaling.data.io.TempDataManager;
import xiao.battleroyale.BattleRoyale;
import xiao.battleroyale.api.common.McSide;
import xiao.battleroyale.api.event.CustomEventType;
import xiao.battleroyale.api.event.ICustomEvent;
import xiao.battleroyale.api.event.ICustomEventHandler;
import xiao.battleroyale.api.minecraft.IMcRegistry;

import java.util.HashMap;
import java.util.Map;

import static xiao.armorscaling.api.data.TempDataTag.TACZ_ARMOR_SCALING;
import static xiao.armorscaling.api.data.TempDataTag.TURN_DAMAGE_SCALING;

public class DamageScalingManager extends AbstractScalingManager implements IDamageScalingManager, ICustomEventHandler {

    private static class DamageScalingManagerHolder {
        private static final DamageScalingManager INSTANCE = new DamageScalingManager();
    }

    public static DamageScalingManager get() {
        return DamageScalingManagerHolder.INSTANCE;
    }

    protected DamageScalingManager() {
        TempDataManager tempDataManager = TempDataManager.get();
        Boolean enable = tempDataManager.getBool(TACZ_ARMOR_SCALING, TURN_DAMAGE_SCALING);
        this.isEnabled = enable != null ? enable : false;
    }

    public static void init(McSide mcSide) {
    }

    protected final Map<String, Float> damageScale = new HashMap<>();

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
        this.damageScale.clear();
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
    protected void saveEnabled(boolean isEnabled) {
        TempDataManager tempDataManager = TempDataManager.get();
        tempDataManager.writeBool(TACZ_ARMOR_SCALING, TURN_DAMAGE_SCALING, isEnabled);
        tempDataManager.saveTempData();
    }

    @Override
    public void reloadConfig(ArmorScalingConfigManager.ArmorScalingConfig config) {
        clearConfig();

        IMcRegistry mcRegistry = BattleRoyale.getMcRegistry();
        DamageScalingEntry entry = config.getDamageScalingEntry();
        for (DamageScalingEntry.DamageScaleEntry damageScale : entry.damageScaleEntries) {
            ResourceLocation itemRl = mcRegistry.createResourceLocation(damageScale.itemRl);
            if (itemRl != null) {
                this.damageScale.put(itemRl.toString(), damageScale.scale);
            }
        }
    }

    protected void onDamageScaling(DamageScalingEvent event) {
        if (!isEnabled()) return;

        EquipmentSlot slot = event.isHeadShot() ? EquipmentSlot.HEAD : EquipmentSlot.CHEST;
        ItemStack armor = event.getVictim().getItemBySlot(slot);
        @Nullable ResourceLocation armorRl = BattleRoyale.getMcRegistry().getItemRl(armor.getItem());
        if (armorRl == null) return;
        String armorRlString = armorRl.toString();

        Float scale = this.damageScale.get(armorRlString);
        if (scale == null) return;

        event.setDamageScale(scale);
    }
}