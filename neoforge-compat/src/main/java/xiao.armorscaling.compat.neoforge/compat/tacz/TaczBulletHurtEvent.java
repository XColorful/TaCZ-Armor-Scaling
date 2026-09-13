package xiao.armorscaling.compat.neoforge.compat.tacz;

import dev.xcolorful.customgun.core.api.common.McLogicalSide;
import dev.xcolorful.customgun.core.api.event.projectile.ProjectileHitEntityEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import xiao.armorscaling.api.compat.tacz.IBulletHurtEvent;
import xiao.battleroyale.api.common.McSide;

public class TaczBulletHurtEvent implements IBulletHurtEvent {

    protected ProjectileHitEntityEvent bulletHurtEvent;

    public TaczBulletHurtEvent(ProjectileHitEntityEvent entityHurtByGunEvent) {
        this.bulletHurtEvent = entityHurtByGunEvent;
    }

    @Override
    public McSide getMcSide() {
        return this.bulletHurtEvent.getLogicalSide() == McLogicalSide.CLIENT ? McSide.CLIENT : McSide.DEDICATED_SERVER;
    }

    @Override
    public float getBaseDamage() {
        return bulletHurtEvent.context.getBaseDamage();
    }

    @Override
    public void setBaseDamage(float damage) {
        bulletHurtEvent.context.setBaseDamage(damage);
    }

    @Override
    public boolean isHeadShot() {
        return bulletHurtEvent.context.isHeadshot();
    }

    @Override
    public float getHeadShotMultiplier() {
        return bulletHurtEvent.context.getHeadshotMultiplier();
    }

    @Override
    public void setHeadShotMultiplier(float multiplier) {
        bulletHurtEvent.context.setHeadshotMultiplier(multiplier);
    }

    @Override
    public Entity getBullet() {
        return bulletHurtEvent.getGunProjectile();
    }

    @Override
    public Entity getHurtEntity() {
        return bulletHurtEvent.getHitResult_VictimEntity();
    }

    @Override
    public Entity getAttacker() {
        return bulletHurtEvent.context.getCausingEntity();
    }

    @Override
    public ResourceLocation getGunId() {
        return bulletHurtEvent.getGunLocation();
    }
}
