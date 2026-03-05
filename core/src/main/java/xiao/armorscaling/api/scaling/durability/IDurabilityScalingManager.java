package xiao.armorscaling.api.scaling.durability;

import xiao.armorscaling.api.scaling.IScalingSubManager;

public interface IDurabilityScalingManager extends IScalingSubManager {

    void setKeepItemGameId(boolean shouldKeep);

    void setReplaceItemRemoveGameId(boolean shouldRemove);
}
