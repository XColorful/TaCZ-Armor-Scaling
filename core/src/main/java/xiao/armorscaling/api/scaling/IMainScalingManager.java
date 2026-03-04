package xiao.armorscaling.api.scaling;

import org.jetbrains.annotations.NotNull;
import xiao.armorscaling.api.scaling.armorignore.IArmorIgnoreManager;
import xiao.armorscaling.api.scaling.damage.IDamageScalingManager;
import xiao.armorscaling.api.scaling.durability.IDurabilityScalingManager;

public interface IMainScalingManager extends IScalingSubManager {

    boolean setArmorIgnoreManager(@NotNull IArmorIgnoreManager armorIgnoreManager);
    boolean setDamageScalingManager(@NotNull IDamageScalingManager damageScalingManager);
    boolean setDurabilityScalingManager(@NotNull IDurabilityScalingManager durabilityScalingManager);

    @NotNull IArmorIgnoreManager getArmorIgnoreManager();
    @NotNull IDamageScalingManager getDamageScalingManager();
    @NotNull IDurabilityScalingManager getDurabilityScalingManager();
}
