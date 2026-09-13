package xiao.armorscaling.compat.tacz;

import net.minecraft.world.entity.Entity;
import xiao.battleroyale.compat.AbstractCompatMod;

public class Tacz extends AbstractCompatMod {

    @Override
    public String getModId() {
        return "customgun";
    }

    private static class TaczHolder {
        private static final Tacz INSTANCE = new Tacz();
    }

    public static Tacz get() {
        return Tacz.TaczHolder.INSTANCE;
    }

    private Tacz() {}

    @Override
    public void checkLoaded() {
        super.checkLoaded();
        if (isLoaded()) {
            registerBulletEvent();
        }
    }

    /**
     * 抛出异常后父类会处理为未加载
     * 任一api变动即视为不兼容
     */
    @Override
    protected void onModLoaded() throws Exception {
        Class<?> hitEventClass = Class.forName("dev.xcolorful.customgun.core.api.event.projectile.ProjectileHitEntityEvent");

        hitEventClass.getMethod("getLogicalSide");
        hitEventClass.getMethod("getHitResult_VictimEntity");
        hitEventClass.getMethod("getGunProjectile");
        hitEventClass.getMethod("getGunLocation");
        hitEventClass.getField("context");

        Class<?> stateAccessClass = Class.forName("dev.xcolorful.customgun.core.api.entity.projectile.IGunProjectileStateAccess");

        stateAccessClass.getMethod("getArmorIgnorePercent", Entity.class);
        stateAccessClass.getMethod("setArmorIgnorePercent", Entity.class, float.class);
    }

    /**
     * 监听并修改子弹伤害和盔甲耐久损耗
     */
    public static void registerBulletEvent() {
        if (!get().isLoaded()) {
            return;
        }
        TaczBulletHandler.get().register();
    }
    public static void unregisterBulletEvent() {
        if (!get().isLoaded()) {
            return;
        }
        TaczBulletHandler.get().unregister();
    }
}
