package xiao.armorscaling.compat.tacz;

import net.minecraft.world.entity.LivingEntity;
import xiao.armorscaling.ArmorScaling;
import xiao.armorscaling.api.compat.tacz.IBulletHurtEvent;
import xiao.armorscaling.api.event.custom.bullethandler.DamageScalingEvent;
import xiao.armorscaling.api.event.custom.bullethandler.DurabilityScalingEvent;
import xiao.battleroyale.BattleRoyale;
import xiao.battleroyale.api.common.McSide;
import xiao.battleroyale.api.event.ICustomEventPoster;

/**
 * 处理盔甲对 TaCZ 子弹的比例减伤和比例破损
 */
public class TaczBulletHandler {

    private static class TaczBulletHandlerHolder {
        private static final TaczBulletHandler INSTANCE = new TaczBulletHandler();
    }

    public static TaczBulletHandler get() {
        return TaczBulletHandler.TaczBulletHandlerHolder.INSTANCE;
    }

    private TaczBulletHandler() {}

    private static boolean isRegistered = false;

    public void register() {
        if (isRegistered) return;
        isRegistered = ArmorScaling.getCompatApi().taczEventRegister().registerBulletHandler();
    }

    public void unregister() {
        if (!isRegistered) return;
        ArmorScaling.getCompatApi().taczEventRegister().unregisterBulletHandler();
        isRegistered = false;
    }

    public void onBulletHurt(IBulletHurtEvent event) {
        if (event.getMcSide() == McSide.CLIENT) return;
        if (!(event.getHurtEntity() instanceof LivingEntity victim)) return;

        ICustomEventPoster eventPoster = BattleRoyale.getEventPoster();

        // 伤害缩放事件
        DamageScalingEvent damageScalingEvent = new DamageScalingEvent(victim, event);
        eventPoster.postCustomEvent(damageScalingEvent);
        float damageScale = damageScalingEvent.getDamageScale();

        // 护甲耐久缩放事件
        DurabilityScalingEvent durabilityScalingEvent = new DurabilityScalingEvent(victim, event, damageScale);
        eventPoster.postCustomEvent(durabilityScalingEvent);

        // 集中处理
        if (!damageScalingEvent.isCanceled() && damageScalingEvent.isDamageScaleChanged()) {
            event.setBaseDamage(event.getBaseDamage() * damageScale);
        }
    }
}
