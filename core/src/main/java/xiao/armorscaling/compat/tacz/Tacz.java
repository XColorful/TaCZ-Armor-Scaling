package xiao.armorscaling.compat.tacz;

import xiao.battleroyale.compat.AbstractCompatMod;

public class Tacz extends AbstractCompatMod {

    @Override
    public String getModId() {
        return "tacz";
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
        Class<?> hurtEventClass = Class.forName("com.tacz.guns.api.event.common.EntityHurtByGunEvent");

        hurtEventClass.getMethod("getBullet");
        hurtEventClass.getMethod("getHurtEntity");
        hurtEventClass.getMethod("getAttacker");
        hurtEventClass.getMethod("getGunId");
        hurtEventClass.getMethod("getBaseAmount");
        hurtEventClass.getMethod("isHeadShot");
        hurtEventClass.getMethod("getHeadshotMultiplier");
        hurtEventClass.getMethod("getLogicalSide");

        Class<?> preEventClass = Class.forName("com.tacz.guns.api.event.common.EntityHurtByGunEvent$Pre");

        preEventClass.getMethod("setBaseAmount", float.class);
        preEventClass.getMethod("setHeadshotMultiplier", float.class);
        preEventClass.getMethod("setHeadshot", boolean.class);
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
