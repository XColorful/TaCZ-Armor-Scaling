package xiao.armorscaling.api.event.custom.bullethandler;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xiao.armorscaling.api.compat.tacz.IBulletHurtEvent;
import xiao.battleroyale.api.event.CustomEvent;

public class DurabilityScalingEvent extends CustomEvent {

    private @Nullable IBulletHurtEvent bulletHurtEvent;
    private final @NotNull LivingEntity victim;
    private final float baseDamage;
    private final float damageScale;
    private final boolean isHeadShot;
    private final float headShotMultiplier;
    private final @Nullable ResourceLocation weaponRl;

    public DurabilityScalingEvent(@NotNull LivingEntity victim, IBulletHurtEvent event, float damageScale) {
        this(victim, event.getBaseDamage(), damageScale, event.isHeadShot(), event.getHeadShotMultiplier(), event.getGunId());
        this.bulletHurtEvent = event;
    }
    public DurabilityScalingEvent(@NotNull LivingEntity victim, float baseDamage, float damageScale, boolean isHeadShot, float headShotMultiplier, ResourceLocation weaponRl) {
        this.victim = victim;
        this.baseDamage = baseDamage;
        this.damageScale = damageScale;
        this.isHeadShot = isHeadShot;
        this.headShotMultiplier = headShotMultiplier;
        this.weaponRl = weaponRl;
    }

    public @NotNull LivingEntity getVictim() {
        return this.victim;
    }
    public float getBaseDamage() {
        return this.baseDamage;
    }
    public float getDamageScale() {
        return this.damageScale;
    }
    public boolean isHeadShot() {
        return this.isHeadShot;
    }
    public float getHeadShotMultiplier() {
        return this.headShotMultiplier;
    }
    public @Nullable ResourceLocation getWeaponRl() {
        return this.weaponRl;
    }

    public @Nullable IBulletHurtEvent getBulletHurtEvent() {
        return this.bulletHurtEvent;
    }
}
