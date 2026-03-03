package xiao.armorscaling.compat.forge;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;
import xiao.armorscaling.ArmorScaling;
import xiao.battleroyale.api.common.McSide;

@Mod(ArmorScaling.MOD_ID)
public class ArmorScalingForge {

    public ArmorScalingForge() {
        Dist dist = FMLLoader.getDist();
        McSide mcSide = dist.isClient() ? McSide.CLIENT : McSide.DEDICATED_SERVER;

        ArmorScaling.init(mcSide);

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    }
}