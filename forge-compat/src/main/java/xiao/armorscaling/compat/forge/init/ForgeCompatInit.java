package xiao.armorscaling.compat.forge.init;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import xiao.armorscaling.ArmorScaling;
import xiao.armorscaling.init.CompatInit;

@Mod.EventBusSubscriber(modid = ArmorScaling.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ForgeCompatInit {

    private static final CompatInit COMPAT_INIT = CompatInit.get();

    @SubscribeEvent
    public static void onLoadComplete(FMLLoadCompleteEvent event) {
        COMPAT_INIT.onLoadComplete();
    }
}
