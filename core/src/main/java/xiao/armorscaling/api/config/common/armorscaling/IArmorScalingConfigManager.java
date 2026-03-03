package xiao.armorscaling.api.config.common.armorscaling;

import xiao.armorscaling.command.CommandArg;
import xiao.battleroyale.api.config.IConfigSubManager;

public interface IArmorScalingConfigManager<T extends IArmorScalingSingleEntry> extends IConfigSubManager<T> {

    @Override
    default String getNameKey() {
        return CommandArg.ARMOR_SCALING;
    }
}
