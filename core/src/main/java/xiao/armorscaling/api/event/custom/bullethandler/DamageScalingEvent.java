package xiao.armorscaling.api.event.custom.bullethandler;

import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xiao.armorscaling.api.compat.tacz.IBulletHurtEvent;
import xiao.battleroyale.BattleRoyale;
import xiao.battleroyale.api.event.CustomEvent;
import xiao.battleroyale.api.event.CustomEventType;
import xiao.battleroyale.api.event.ICustomEvent;
import xiao.battleroyale.api.event.ICustomEventHandler;
import xiao.battleroyale.api.minecraft.CommandLevel;
import xiao.battleroyale.event.EventDispatcher;

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

    @Override
    public @Nullable CommandSourceStack createCommandSourceStack(@Nullable CommandSource source) {
        Level level = victim.level();
        if (level != null && level.isClientSide()) return null;
        return new CommandSourceStack(
                source != null ? source : CommandSource.NULL,
                victim.position(),
                victim.getRotationVector(),
                (ServerLevel) level,
                CommandLevel.permission(4),
                this.getTextName(),
                this.getDisplayName(),
                level.getServer(),
                victim
        );
    }
    @Override public String getTextName() {
        return victim.getName().getString();
    }
    @Override public Component getDisplayName() {
        return victim.getDisplayName();
    }

    @SuppressWarnings("UnstableApiUsage")
    private static final EventDispatcher<ICustomEventHandler, ICustomEvent, CustomEventType> _EVENT_DISPATCHER = BattleRoyale.getEventPoster().getEventDispatcher(DamageScalingEvent.class);
    @SuppressWarnings("UnstableApiUsage")
    @Override public @NotNull EventDispatcher<ICustomEventHandler, ICustomEvent, CustomEventType> getEventDispatcher() {
        return _EVENT_DISPATCHER;
    }
}
