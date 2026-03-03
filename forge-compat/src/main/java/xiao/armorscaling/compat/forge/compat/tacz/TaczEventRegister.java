package xiao.armorscaling.compat.forge.compat.tacz;

import com.tacz.guns.api.event.common.EntityHurtByGunEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import xiao.armorscaling.ArmorScaling;
import xiao.armorscaling.api.compat.tacz.ITaczEventRegister;
import xiao.armorscaling.compat.tacz.TaczBulletHandler;

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
        MinecraftForge.EVENT_BUS.register(bulletListener);
        ArmorScaling.LOGGER.debug("Registered TaczBulletHandler");
        return true;
    }

    @Override
    public boolean unregisterBulletHandler() {
        MinecraftForge.EVENT_BUS.unregister(bulletListener);
        ArmorScaling.LOGGER.debug("Unregistered TaczBulletHandler");
        return true;
    }

    private static class BulletEventListener {
        @SubscribeEvent
        public void onBulletHurt(EntityHurtByGunEvent.Pre event) {
            TaczBulletHandler.get().onBulletHurt(new TaczBulletHurtEvent(event));
        }
    }
}
