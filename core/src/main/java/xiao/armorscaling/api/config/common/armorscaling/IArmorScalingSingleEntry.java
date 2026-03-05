package xiao.armorscaling.api.config.common.armorscaling;

import xiao.armorscaling.config.common.armorscaling.type.DamageScalingEntry;
import xiao.armorscaling.config.common.armorscaling.type.DurabilityScalingEntry;
import xiao.battleroyale.api.config.sub.IConfigSingleEntry;

public interface IArmorScalingSingleEntry extends IConfigSingleEntry {

    DamageScalingEntry getDamageScalingEntry();

    DurabilityScalingEntry getDurabilityScalingEntry();
}
