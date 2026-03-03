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

public class BackupCommand {

    public static LiteralArgumentBuilder<CommandSourceStack> get() {
        return Commands.literal(CommandArg.BACKUP)
                .executes(BackupCommand::backupAllConfigs)
                .then(Commands.literal(ARMOR_SCALING)
                        .executes(context -> backupArmorScalingConfigs(context, null))
                );
    }

    private static int backupAllConfigs(CommandContext<CommandSourceStack> context) {
        if (ArmorScaling.getModConfigManager().backupAllConfigs() > 0) {
            context.getSource().sendSuccess(() -> Component.translatable("armorscaling.message.all_config_backed_up"), true);
            ArmorScaling.LOGGER.info("Backed up all {} configs", ArmorScaling.MOD_ID);
            return Command.SINGLE_SUCCESS;
        } else {
            context.getSource().sendFailure(Component.translatable("armorscaling.message.no_config_manager_available", ArmorScaling.MOD_ID));
            return 0;
        }
    }

    private static int backupArmorScalingConfigs(CommandContext<CommandSourceStack> context, @Nullable String subType) {
        IConfigSubManager<?> armorScalingConfigManager = ConfigUtils.getConfigSubManager(context, ArmorScalingConfigManager.get().getNameKey());
        if (armorScalingConfigManager == null) return 0;

        int success;
        String messageKey;
        if (subType == null) {
            success = armorScalingConfigManager.backupAllConfigs();
            messageKey = "armorscaling.message.armor_scaling_config_backed_up";
        } else {
            messageKey = "";
            success = 0;
        }
        context.getSource().sendSuccess(() -> Component.translatable(messageKey), true);
        ArmorScaling.LOGGER.info("Backed up {} configs via command", subType != null ? subType : "all armorscaling");
        return Command.SINGLE_SUCCESS;
    }
}
