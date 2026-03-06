package xiao.armorscaling.common.scaling.durability;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xiao.armorscaling.ArmorScaling;
import xiao.armorscaling.api.event.custom.bullethandler.DurabilityScalingEvent;
import xiao.armorscaling.api.scaling.durability.IDurabilityScalingManager;
import xiao.armorscaling.common.scaling.AbstractScalingManager;
import xiao.armorscaling.config.common.armorscaling.ArmorScalingConfigManager;
import xiao.armorscaling.config.common.armorscaling.type.DurabilityScalingEntry;
import xiao.armorscaling.data.io.TempDataManager;
import xiao.battleroyale.BattleRoyale;
import xiao.battleroyale.api.common.McSide;
import xiao.battleroyale.api.config.common.loot.ILootEntry;
import xiao.battleroyale.api.event.CustomEventType;
import xiao.battleroyale.api.event.ICustomEvent;
import xiao.battleroyale.api.event.ICustomEventHandler;
import xiao.battleroyale.api.game.IGameManager;
import xiao.battleroyale.api.minecraft.IMcRegistry;
import xiao.battleroyale.common.loot.LootGenerator;
import xiao.battleroyale.config.common.loot.type.EmptyEntry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static xiao.armorscaling.api.data.TempDataTag.*;

public class DurabilityScalingManager extends AbstractScalingManager implements IDurabilityScalingManager, ICustomEventHandler {

    private static class DurabilityScalingManagerHolder {
        private static final DurabilityScalingManager INSTANCE = new DurabilityScalingManager();
    }

    public static DurabilityScalingManager get() {
        return DurabilityScalingManagerHolder.INSTANCE;
    }

    protected DurabilityScalingManager() {
        TempDataManager tempDataManager = TempDataManager.get();
        Boolean enable = tempDataManager.getBool(TACZ_ARMOR_SCALING, TURN_DURABILITY_SCALING);
        this.isEnabled = enable != null ? enable : false;
        Boolean keepGameId= tempDataManager.getBool(TACZ_ARMOR_SCALING, KEEP_ITEM_GAME_ID);
        this.keepItemGameId = keepGameId != null ? keepGameId : true;
        Boolean removeGameId = tempDataManager.getBool(TACZ_ARMOR_SCALING, REPLACE_ITEM_REMOVE_GAME_ID);
        this.replaceItemRemoveGameId = removeGameId != null ? removeGameId : true;
    }

    public static void init(McSide mcSide) {
    }

    protected float healthToDurabilityRatio = 5;
    protected boolean useAbsorbedDamageOnly = true;
    protected boolean headShotMultiplierScaling = true;
    protected final Map<String, DurabilityData> durabilityScaleData = new HashMap<>();
    protected final Map<Integer, @NotNull ILootEntry> lootDataEntries = new HashMap<>();
    public record DurabilityData(float maxDurabilityPercent, int lootDataId, @NotNull ILootEntry replaceItemEntry) {}
    protected boolean keepItemGameId;
    protected boolean replaceItemRemoveGameId;

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
        this.healthToDurabilityRatio = 5;
        durabilityScaleData.clear();
        lootDataEntries.clear();
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
    protected void saveEnabled(boolean isEnabled) {
        TempDataManager tempDataManager = TempDataManager.get();
        tempDataManager.writeBool(TACZ_ARMOR_SCALING, TURN_DURABILITY_SCALING, isEnabled);
        tempDataManager.saveTempData();
    }

    @Override
    public boolean keepItemGameId() {
        return this.keepItemGameId;
    }

    @Override
    public void setKeepItemGameId(boolean shouldKeep) {
        this.keepItemGameId = shouldKeep;
        TempDataManager tempDataManager = TempDataManager.get();
        tempDataManager.writeBool(TACZ_ARMOR_SCALING, KEEP_ITEM_GAME_ID, this.keepItemGameId);
        tempDataManager.saveTempData();
    }

    @Override
    public boolean replaceItemRemoveGameId() {
        return this.replaceItemRemoveGameId;
    }

    @Override
    public void setReplaceItemRemoveGameId(boolean shouldRemove) {
        this.replaceItemRemoveGameId = shouldRemove;
        TempDataManager tempDataManager = TempDataManager.get();
        tempDataManager.writeBool(TACZ_ARMOR_SCALING, REPLACE_ITEM_REMOVE_GAME_ID, this.replaceItemRemoveGameId);
        tempDataManager.saveTempData();
    }

    @Override
    public void reloadConfig(ArmorScalingConfigManager.ArmorScalingConfig config) {
        clearConfig();

        IMcRegistry mcRegistry = BattleRoyale.getMcRegistry();
        DurabilityScalingEntry entry = config.getDurabilityScalingEntry();
        this.healthToDurabilityRatio = entry.healthToDurabilityRatio;
        this.useAbsorbedDamageOnly = entry.useAbsorbedDamageOnly;
        this.headShotMultiplierScaling = entry.headShotMultiplierScaling;

        // 先 lootData
        for (DurabilityScalingEntry.LootDataEntry lootDataEntry : entry.replaceItemLootData) {
            if (lootDataEntry.lootDataId >= 0) {
                this.lootDataEntries.put(lootDataEntry.lootDataId, lootDataEntry.lootEntry);
            }
        }
        // 之后就可以索引
        for (DurabilityScalingEntry.DurabilityScaleEntry durabilityScale : entry.durabilityScaleEntries) {
            ResourceLocation itemRl = mcRegistry.createResourceLocation(durabilityScale.itemRl);
            if (itemRl != null) {
                // 优先取词条里的
                @Nullable ILootEntry replaceLoot = durabilityScale.replaceItemLoot;
                // 取不到再去索引
                if (replaceLoot == null && durabilityScale.lootDataId >= 0) {
                    replaceLoot = this.lootDataEntries.get(durabilityScale.lootDataId);
                }
                if (replaceLoot == null) {
                    replaceLoot = new EmptyEntry(EmptyEntry.TYPE_ITEM);
                }
                this.durabilityScaleData.put(itemRl.toString(), new DurabilityData(durabilityScale.damagePercent, durabilityScale.lootDataId, replaceLoot));
            }
        }
    }

    protected void onDurabilityScaling(DurabilityScalingEvent event) {
        if (!isEnabled()) return;

        EquipmentSlot armorSlot = event.isHeadShot() ? EquipmentSlot.HEAD : EquipmentSlot.CHEST;
        @NotNull LivingEntity victim = event.getVictim();
        ItemStack armor = victim.getItemBySlot(armorSlot);
        @Nullable ResourceLocation armorRl = BattleRoyale.getMcRegistry().getItemRl(armor.getItem());
        if (armorRl == null) return;
        String armorRlString = armorRl.toString();

        DurabilityData data = this.durabilityScaleData.get(armorRlString);
        if (data == null) return;

        // ----耐久度比例损耗计算----

        // 吸收伤害
        float absorbedDamage = this.useAbsorbedDamageOnly
                ? event.getBaseDamage() * (1 - event.getDamageScale())
                : event.getBaseDamage();
        if (this.headShotMultiplierScaling && event.isHeadShot()) {
            absorbedDamage *= event.getHeadShotMultiplier();
        }
        // 损害的耐久度比例
        float durabilityLossPercent = absorbedDamage * healthToDurabilityRatio;
        // 换算到 Minecraft 物品耐久损耗量
        int maxArmorDamage = armor.getMaxDamage();
        int armorDamageLoss = data.maxDurabilityPercent() != 0
                ? (int) (maxArmorDamage * (durabilityLossPercent / data.maxDurabilityPercent()))
                : maxArmorDamage;

        // ----碎甲----

        // 原版碎甲机制, 兼容附魔和记分
        IGameManager gameManager = BattleRoyale.getGameManager();
        @Nullable UUID armorGameId = gameManager.getGameIdReadApi().getGameId(armor);
        armor.hurtAndBreak(armorDamageLoss,
                victim,
                armorSlot
        );
        // 替换盔甲
        if (armor.getCount() <= 0) {
            LootGenerator.LootContext lootContext = new LootGenerator.LootContext(
                    (ServerLevel) victim.level(),
                    victim.position(),
                    this.keepItemGameId && armorGameId != null ? armorGameId : gameManager.getGameId());
            List<ItemStack> lootItems = LootGenerator.generateLootItem(lootContext, data.replaceItemEntry());
            if (!lootItems.isEmpty()) {
                ItemStack newArmor = lootItems.get(0);
                if (this.replaceItemRemoveGameId) {
                    gameManager.getGameIdWriteApi().removeGameId(newArmor);
                }
                victim.setItemSlot(armorSlot, newArmor);
            }
        }
    }
}