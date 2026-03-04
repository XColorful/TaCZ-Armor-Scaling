package xiao.armorscaling.compat.forge.mixin.tacz;

import com.tacz.guns.entity.EntityKineticBullet;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xiao.armorscaling.api.event.custom.bullethandler.ArmorIgnoreEvent;
import xiao.battleroyale.BattleRoyale;

@Mixin(value = EntityKineticBullet.class)
public class EntityKineticBulletMixin {

    @Inject(method = "tacAttackEntity", at = @At("HEAD"), cancellable = true)
    private void tacAttackEntity(EntityKineticBullet.MaybeMultipartEntity parts, float damage, Pair<DamageSource, DamageSource> sources,
                                 CallbackInfo ci) {
        ArmorIgnoreEvent armorIgnoreEvent = new ArmorIgnoreEvent(parts.hitPart());
        if (BattleRoyale.getEventPoster().postCustomEvent(armorIgnoreEvent) && armorIgnoreEvent.isArmorIgnoreChanged()) {
            /**
             * 下同 {@link EntityKineticBullet#tacAttackEntity}
             */

            var source1 = sources.getLeft();
            var source2 = sources.getRight();
            // 穿甲伤害和普通伤害的比例计算
            float armorDamagePercent = Mth.clamp(armorIgnoreEvent.getArmorIgnorePercent(), 0.0F, 1.0F);
            float normalDamagePercent = 1 - armorDamagePercent;
            // 取消无敌时间
            parts.core().invulnerableTime = 0;
            // 普通伤害
            parts.hitPart().hurt(source1, damage * normalDamagePercent);
            // 取消无敌时间
            parts.core().invulnerableTime = 0;
            // 穿甲伤害
            parts.hitPart().hurt(source2, damage * armorDamagePercent);

            ci.cancel();
        }
    }
}
