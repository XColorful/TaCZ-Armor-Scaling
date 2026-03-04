package xiao.armorscaling.common.scaling;

import org.jetbrains.annotations.NotNull;
import xiao.armorscaling.ArmorScaling;
import xiao.armorscaling.api.scaling.IArmorScalingManager;
import xiao.armorscaling.api.scaling.IScalingSubManager;
import xiao.armorscaling.api.scaling.armorignore.IArmorIgnoreManager;
import xiao.armorscaling.api.scaling.damage.IDamageScalingManager;
import xiao.armorscaling.api.scaling.durability.IDurabilityScalingManager;
import xiao.armorscaling.common.scaling.armorignore.ArmorIgnoreManager;
import xiao.armorscaling.common.scaling.damage.DamageScalingManager;
import xiao.armorscaling.common.scaling.durability.DurabilityScalingManager;
import xiao.armorscaling.config.common.armorscaling.ArmorScalingConfigManager;
import xiao.battleroyale.api.common.McSide;
import xiao.battleroyale.api.config.IConfigSubManager;

import java.util.Arrays;
import java.util.Collection;

public class ArmorScalingManager extends AbstractScalingManager implements IArmorScalingManager {

    private static class ArmorScalingManagerHolder {
        private static final ArmorScalingManager INSTANCE = new ArmorScalingManager();
    }

    public static ArmorScalingManager get() {
        return ArmorScalingManagerHolder.INSTANCE;
    }

    protected ArmorScalingManager() {
        this.armorIgnoreManager = ArmorIgnoreManager.get(); this.armorIgnoreManager.registerToMod();
        this.damageScalingManager = DamageScalingManager.get(); this.damageScalingManager.registerToMod();
        this.durabilityScalingManager = DurabilityScalingManager.get(); this.durabilityScalingManager.registerToMod();
    }

    public static void init(McSide mcSide) {
        ArmorIgnoreManager.init(mcSide);
        DamageScalingManager.init(mcSide);
        DurabilityScalingManager.init(mcSide);
    }

    @Override public String getManagerName() {
        return String.format("%s:ArmorScalingManager", ArmorScaling.MOD_ID);
    }

    private @NotNull IArmorIgnoreManager armorIgnoreManager;
    private @NotNull IDamageScalingManager damageScalingManager;
    private @NotNull IDurabilityScalingManager durabilityScalingManager;
    protected void registerNewManager(IScalingSubManager previousManager, IScalingSubManager newManager) {
        if (previousManager != null) {
            if (previousManager.unregisterToMod()) {
                ArmorScaling.LOGGER.debug("Unregister previous IScalingSubManager {} to mod", previousManager.getManagerName());
            } else {
                ArmorScaling.LOGGER.debug("Failed to unregister previous IScalingSubManager {} to mod", previousManager.getManagerName());
            }
        }
        if (newManager.registerToMod()) { // temp
            ArmorScaling.LOGGER.debug("Register new IScalingSubManager {} to mod", newManager.getManagerName());
        } else {
            ArmorScaling.LOGGER.warn("Failed to unregister new IScalingSubManager {} to mod", newManager.getManagerName());
        }
    }
    @Override public boolean setArmorIgnoreManager(@NotNull IArmorIgnoreManager armorIgnoreManager) {
        registerNewManager(this.armorIgnoreManager, armorIgnoreManager);
        this.armorIgnoreManager = armorIgnoreManager;
        return true;
    }
    @Override public boolean setDamageScalingManager(@NotNull IDamageScalingManager damageScalingManager) {
        registerNewManager(this.damageScalingManager, damageScalingManager);
        this.damageScalingManager = damageScalingManager;
        return true;
    }
    @Override public boolean setDurabilityScalingManager(@NotNull IDurabilityScalingManager durabilityScalingManager) {
        registerNewManager(this.durabilityScalingManager, durabilityScalingManager);
        this.durabilityScalingManager = durabilityScalingManager;
        return true;
    }
    @Override public @NotNull IArmorIgnoreManager getArmorIgnoreManager() {
        return armorIgnoreManager;
    }
    @Override public @NotNull IDamageScalingManager getDamageScalingManager() {
        return damageScalingManager;
    }
    @Override public @NotNull IDurabilityScalingManager getDurabilityScalingManager() {
        return durabilityScalingManager;
    }

    @Override
    public void clearConfig() {
        for (IScalingSubManager manager : getSubManagers()) {
            manager.clearConfig();
        }
    }

    @Override
    public boolean registerToMod() {
        IConfigSubManager<?> armorScalingConfigManager = ArmorScaling.getModConfigManager().getConfigSubManager(ArmorScalingConfigManager.get().getNameKey());
        if (armorScalingConfigManager != null
                && armorScalingConfigManager.getConfigEntry(armorScalingConfigManager.getLastAppliedConfigId())
                instanceof ArmorScalingConfigManager.ArmorScalingConfig config) {
            reloadConfig(config);
        }
        return getSubManagers().stream().allMatch(IScalingSubManager::isConfigPrepared);
    }

    @Override
    protected boolean unregisterEvents() {
        return true;
    }

    @Override
    public void reloadConfig(ArmorScalingConfigManager.ArmorScalingConfig config) {
        for (IScalingSubManager manager : getSubManagers()) {
            manager.reloadConfig(config);
        }
    }

    private Collection<IScalingSubManager> getSubManagers() {
        return Arrays.asList(
                getArmorIgnoreManager(),
                getDamageScalingManager(),
                getDurabilityScalingManager()
        );
    }
}
