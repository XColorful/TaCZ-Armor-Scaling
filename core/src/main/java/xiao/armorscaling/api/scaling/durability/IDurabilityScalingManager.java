package xiao.armorscaling.api.scaling.durability;

import xiao.armorscaling.api.scaling.IScalingSubManager;

public interface IDurabilityScalingManager extends IScalingSubManager {

    boolean keepItemGameId();
    void setKeepItemGameId(boolean shouldKeep);

    boolean replaceItemRemoveGameId();
    void setReplaceItemRemoveGameId(boolean shouldRemove);
}
