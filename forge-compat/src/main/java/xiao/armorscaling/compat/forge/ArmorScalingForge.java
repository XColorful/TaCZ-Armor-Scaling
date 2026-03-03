package xiao.armorscaling.compat.forge;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;
import xiao.armorscaling.ArmorScaling;
import xiao.armorscaling.compat.forge.compat.tacz.TaczEventRegister;
import xiao.battleroyale.api.common.McSide;

@Mod(ArmorScaling.MOD_ID)
public class ArmorScalingForge {

    public static ArmorScaling.CompatApi compatApi;

    public ArmorScalingForge() {
        Dist dist = FMLLoader.getDist();
        McSide mcSide = dist.isClient() ? McSide.CLIENT : McSide.DEDICATED_SERVER;

        ArmorScalingForge.compatApi = new ArmorScaling.CompatApi(TaczEventRegister.get());
        ArmorScaling.init(mcSide,
                ArmorScalingForge.compatApi);

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    }
}