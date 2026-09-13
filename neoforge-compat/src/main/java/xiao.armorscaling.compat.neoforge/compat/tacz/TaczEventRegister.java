package xiao.armorscaling.compat.neoforge.compat.tacz;

import dev.xcolorful.customgun.CustomGun;
import dev.xcolorful.customgun.core.api.entity.IGunProjectile;
import dev.xcolorful.customgun.core.api.event.CustomEventType;
import dev.xcolorful.customgun.core.api.event.ICustomEvent;
import dev.xcolorful.customgun.core.api.event.ICustomEventHandler;
import dev.xcolorful.customgun.core.api.event.projectile.ProjectileHitEntityEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import xiao.armorscaling.ArmorScaling;
import xiao.armorscaling.api.compat.tacz.ITaczEventRegister;
import xiao.armorscaling.api.event.custom.bullethandler.ArmorIgnoreEvent;
import xiao.armorscaling.compat.tacz.TaczBulletHandler;
import xiao.battleroyale.BattleRoyale;

public class TaczEventRegister implements ITaczEventRegister {

    private static class TaczEventRegisterHolder {
        private static final TaczEventRegister INSTANCE = new TaczEventRegister();
    }

    public static TaczEventRegister get() {
        return TaczEventRegister.TaczEventRegisterHolder.INSTANCE;
    }

    private TaczEventRegister() {}

    private final BulletEventListener bulletListener = new BulletEventListener();

    @Override
    public boolean registerBulletHandler() {
        CustomGun.getEventRegister().register(bulletListener, CustomEventType.PROJECTILE_HIT_ENTITY_EVENT);
        ArmorScaling.LOGGER.debug("Registered TaczBulletHandler");
        return true;
    }

    @Override
    public boolean unregisterBulletHandler() {
        CustomGun.getEventRegister().unregister(bulletListener, CustomEventType.PROJECTILE_HIT_ENTITY_EVENT);
        ArmorScaling.LOGGER.debug("Unregistered TaczBulletHandler");
        return true;
    }

    private static class BulletEventListener implements ICustomEventHandler {

        @Override
        public String getEventHandlerName() {
            return "BulletEventListener";
        }

        @Override
        public void handleEvent(CustomEventType eventType, ICustomEvent event) {
            if (eventType == CustomEventType.PROJECTILE_HIT_ENTITY_EVENT) {
                ProjectileHitEntityEvent hitEvent = (ProjectileHitEntityEvent) event;
                TaczBulletHandler.get().onBulletHurt(new TaczBulletHurtEvent(hitEvent));

                // 原 _ProjectileHitMixin 的职责:
                // CGC 在本事件之后按 armorIgnorePercent 拆分普通/穿甲伤害, 所以把 ArmorIgnoreEvent 的结果写回枪射物
                ArmorIgnoreEvent armorIgnoreEvent = new ArmorIgnoreEvent(hitEvent.getHitResult_VictimEntity());
                if (BattleRoyale.getEventPoster().postCustomEvent(armorIgnoreEvent) && armorIgnoreEvent.isArmorIgnoreChanged()) {
                    IGunProjectile iGunProjectile = hitEvent.getIGunProjectile();
                    Entity gunProjectile = hitEvent.getGunProjectile();
                    if (iGunProjectile != null && gunProjectile != null) {
                        iGunProjectile.setArmorIgnorePercent(gunProjectile,
                                Mth.clamp(armorIgnoreEvent.getArmorIgnorePercent(), 0.0F, 1.0F));
                    }
                }
            } else {
                onReceiveWrongEvent(eventType);
            }
        }
    }
}