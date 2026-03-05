package xiao.armorscaling.compat.forge.init;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import xiao.armorscaling.ArmorScaling;
import xiao.armorscaling.init.CommonSetup;
import xiao.battleroyale.api.init.ICommonSetup;

@Mod.EventBusSubscriber(modid = ArmorScaling.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ForgeCommonSetup {

    private static final ICommonSetup COMMON_SETUP = CommonSetup.get();

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(COMMON_SETUP::onCommonSetup);
    }
}
