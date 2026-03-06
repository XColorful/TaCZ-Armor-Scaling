package xiao.armorscaling.compat.neoforge;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLLoader;
import xiao.armorscaling.ArmorScaling;
import xiao.armorscaling.compat.neoforge.compat.tacz.TaczEventRegister;
import xiao.battleroyale.api.common.McSide;

@Mod(ArmorScaling.MOD_ID)
public class ArmorScalingNeoforge {

    public static ArmorScaling.CompatApi compatApi;

    public ArmorScalingNeoforge(IEventBus modEventBus) {
        Dist dist = FMLLoader.getDist();
        McSide mcSide = dist.isClient() ? McSide.CLIENT : McSide.DEDICATED_SERVER;

        ArmorScaling.CompatApi compatApi = new ArmorScaling.CompatApi(TaczEventRegister.get());
        ArmorScaling.init(mcSide,
                ArmorScalingNeoforge.compatApi);
    }
}