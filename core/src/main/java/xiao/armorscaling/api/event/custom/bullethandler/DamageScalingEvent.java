package xiao.armorscaling.api.event.custom.bullethandler;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xiao.armorscaling.api.compat.tacz.IBulletHurtEvent;
import xiao.battleroyale.api.event.CustomEvent;

public class DamageScalingEvent extends CustomEvent {

    private @Nullable IBulletHurtEvent bulletHurtEvent;
    private final @NotNull LivingEntity victim;
    private final float baseDamage;
    private float damageScale = 1;
    private boolean damageScaleChanged = false;
    private final boolean isHeadShot;
    private final @Nullable ResourceLocation weaponRl;

    public DamageScalingEvent(@NotNull LivingEntity livingEntity, IBulletHurtEvent event) {
        this(livingEntity, event.getBaseDamage(), event.isHeadShot(), event.getGunId());
        this.bulletHurtEvent = event;
    }
    public DamageScalingEvent(@NotNull LivingEntity victim, float baseDamage, boolean isHeadShot, ResourceLocation weaponRl) {
        this.victim = victim;
        this.baseDamage = baseDamage;
        this.isHeadShot = isHeadShot;
        this.weaponRl = weaponRl;
    }

    public @NotNull LivingEntity getVictim() {
        return this.victim;
    }
    public float getBaseDamage() {
        return this.baseDamage;
    }
    public void setDamageScale(float scale) {
        this.damageScale = scale;
        this.damageScaleChanged = true;
    }
    public boolean isDamageScaleChanged() {
        return this.damageScaleChanged;
    }
    public float getDamageScale() {
        return this.damageScale;
    }
    public boolean isHeadShot() {
        return this.isHeadShot;
    }
    public @Nullable ResourceLocation getWeaponRl() {
        return this.weaponRl;
    }

    public @Nullable IBulletHurtEvent getBulletHurtEvent() {
        return this.bulletHurtEvent;
    }
}
