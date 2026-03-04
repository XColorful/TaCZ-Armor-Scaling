package xiao.armorscaling.api.event.custom.bullethandler;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import xiao.battleroyale.api.event.CustomEvent;

public class ArmorIgnoreEvent extends CustomEvent {

    private @Nullable Entity victimEntity;
    private @Nullable LivingEntity victim;
    private float armorIgnorePercent = 0;
    private boolean armorIgnoreChanged = false;

    public ArmorIgnoreEvent(@Nullable Entity victimEntity) {
        this.victimEntity = victimEntity;
        this.victim = victimEntity instanceof LivingEntity livingEntity ? livingEntity : null;
    }

    public @Nullable Entity getVictimEntity() {
        return this.victimEntity;
    }
    public @Nullable LivingEntity getVictim() {
        return this.victim;
    }
    public float getArmorIgnorePercent() {
        return this.armorIgnorePercent;
    }
    public void setArmorIgnorePercent(float percent) {
        this.armorIgnorePercent = percent;
        this.armorIgnoreChanged = true;
    }
    public boolean isArmorIgnoreChanged() {
        return this.armorIgnoreChanged;
    }

    /**
     * 取消事件作为 "已处理" 的信号
     */
    public void setHandled() {
        if (isArmorIgnoreChanged()) super.setCanceled(true);
    }
}
