package xiao.armorscaling.api.event.custom.bullethandler;

import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xiao.battleroyale.BattleRoyale;
import xiao.battleroyale.api.event.CustomEvent;
import xiao.battleroyale.api.event.CustomEventType;
import xiao.battleroyale.api.event.ICustomEvent;
import xiao.battleroyale.api.event.ICustomEventHandler;
import xiao.battleroyale.api.minecraft.CommandLevel;
import xiao.battleroyale.event.EventDispatcher;

public class ArmorIgnoreEvent extends CustomEvent {

    private final @Nullable Entity victimEntity;
    private final @Nullable LivingEntity victim;
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

    @Override
    public @Nullable CommandSourceStack createCommandSourceStack(@Nullable CommandSource source) {
        if (victimEntity == null) return null;
        Level level = victimEntity.level();
        if (level != null && level.isClientSide()) return null;
        return new CommandSourceStack(
                source != null ? source : CommandSource.NULL,
                victimEntity.position(),
                victimEntity.getRotationVector(),
                (ServerLevel) level,
                CommandLevel.permission(4),
                this.getTextName(),
                this.getDisplayName(),
                level.getServer(),
                victimEntity
        );
    }
    @Override public String getTextName() {
        return victimEntity != null ? victimEntity.getName().getString() : "ArmorIgnoreEvent";
    }
    @Override public Component getDisplayName() {
        return victimEntity != null ? victimEntity.getDisplayName() : Component.literal(getTextName());
    }

    @SuppressWarnings("UnstableApiUsage")
    private static final EventDispatcher<ICustomEventHandler, ICustomEvent, CustomEventType> _EVENT_DISPATCHER = BattleRoyale.getEventPoster().getEventDispatcher(ArmorIgnoreEvent.class);
    @SuppressWarnings("UnstableApiUsage")
    @Override public @NotNull EventDispatcher<ICustomEventHandler, ICustomEvent, CustomEventType> getEventDispatcher() {
        return _EVENT_DISPATCHER;
    }
}
