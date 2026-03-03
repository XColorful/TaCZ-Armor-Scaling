package xiao.armorscaling.command.sub;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import xiao.armorscaling.ArmorScaling;
import xiao.armorscaling.config.common.armorscaling.ArmorScalingConfigManager;
import xiao.battleroyale.api.config.IConfigSubManager;
import xiao.battleroyale.command.CommandArg;

import javax.annotation.Nullable;

import static xiao.armorscaling.command.CommandArg.ARMOR_SCALING;

public class SaveCommand {

    public static LiteralArgumentBuilder<CommandSourceStack> get() {
        return Commands.literal(CommandArg.SAVE)
                .executes(SaveCommand::saveAllConfigs)
                .then(Commands.literal(ARMOR_SCALING)
                        .executes(context -> saveArmorScalingConfigs(context, null))
                );
    }

    private static int saveAllConfigs(CommandContext<CommandSourceStack> context) {
        ArmorScaling.getModConfigManager().saveAllConfigs();
        context.getSource().sendSuccess(() -> Component.translatable("armorscaling.message.all_config_saved"), true);
        ArmorScaling.LOGGER.info("Saved all {} configs", ArmorScaling.MOD_ID);
        return Command.SINGLE_SUCCESS;
    }

    private static int saveArmorScalingConfigs(CommandContext<CommandSourceStack> context, @Nullable String subType) {
        IConfigSubManager<?> armorScalingConfigManager = ConfigUtils.getConfigSubManager(context, ArmorScalingConfigManager.get().getNameKey());
        if (armorScalingConfigManager == null) return 0;

        int success;
        String messageKey;
        if (subType == null) {
            success = armorScalingConfigManager.saveAllConfigs();
            messageKey = "armorscaling.message.armor_scaling_config_saved";
        } else {
            messageKey = "";
            success = 0;
        }
        context.getSource().sendSuccess(() -> Component.translatable(messageKey), true);
        ArmorScaling.LOGGER.info("Saved {} configs via command", subType != null ? subType : "all armorscaling");
        return Command.SINGLE_SUCCESS;
    }

}