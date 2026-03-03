package xiao.armorscaling.command.sub;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import xiao.armorscaling.ArmorScaling;
import xiao.armorscaling.config.common.armorscaling.ArmorScalingConfigManager;
import xiao.battleroyale.api.config.IConfigSubManager;
import xiao.battleroyale.api.config.sub.IConfigSingleEntry;
import xiao.battleroyale.command.CommandArg;

import static xiao.armorscaling.command.CommandArg.ARMOR_SCALING;

public class ConfigCommand {

    public static LiteralArgumentBuilder<CommandSourceStack> get() {
        return Commands.literal(CommandArg.CONFIG)
                .then(Commands.literal(ARMOR_SCALING)
                        .then(Commands.argument(CommandArg.ID, IntegerArgumentType.integer(0))
                                .executes(ConfigCommand::applyArmorScalingConfig))
                        .then(Commands.literal(CommandArg.SWITCH)
                                .executes(ConfigCommand::switchNextArmorScalingConfig)
                                .then(Commands.argument(CommandArg.FILE, StringArgumentType.string())
                                        .executes(ConfigCommand::switchArmorScalingConfig)
                                )
                        )
                );
    }

    private static int applyArmorScalingConfig(CommandContext<CommandSourceStack> context) {
        IConfigSubManager<?> armorScalingConfigManager = ConfigUtils.getConfigSubManager(context, ArmorScalingConfigManager.get().getNameKey());
        if (armorScalingConfigManager == null) return 0;

        int id = IntegerArgumentType.getInteger(context, CommandArg.ID);
        IConfigSingleEntry armorScalingConfig = armorScalingConfigManager.getConfigEntry(id);
        if (armorScalingConfig != null) {
            armorScalingConfig.applyDefault();
            armorScalingConfigManager.setLastAppliedConfigId(armorScalingConfig.getConfigId());
            ArmorScaling.LOGGER.info("Applied armor scaling config {} via command", id);
            context.getSource().sendSuccess(() -> Component.translatable("armorscaling.message.armor_scaling_config_applied", id, armorScalingConfig.getName()), true);
            return Command.SINGLE_SUCCESS;
        } else {
            context.getSource().sendFailure(Component.translatable("armorscaling.message.invalid_armor_scaling_config_id", id));
            return 0;
        }
    }
    private static int switchNextArmorScalingConfig(CommandContext<CommandSourceStack> context) {
        IConfigSubManager<?> armorScalingConfigManager = ConfigUtils.getConfigSubManager(context, ArmorScalingConfigManager.get().getNameKey());
        if (armorScalingConfigManager == null) return 0;

        if (armorScalingConfigManager.switchConfigFile()) {
            String currentFileName = armorScalingConfigManager.getCurrentSelectedFileName();
            ArmorScaling.LOGGER.info("Switch armor scaling config file to {} via command", currentFileName);
            context.getSource().sendSuccess(() -> Component.translatable("armorscaling.message.switch_armor_scaling_config_file", currentFileName), true);
            return Command.SINGLE_SUCCESS;
        } else {
            context.getSource().sendFailure(Component.translatable("armorscaling.message.no_armor_scaling_config_available"));
            return 0;
        }
    }
    private static int switchArmorScalingConfig(CommandContext<CommandSourceStack> context) {
        IConfigSubManager<?> armorScalingConfigManager = ConfigUtils.getConfigSubManager(context, ArmorScalingConfigManager.get().getNameKey());
        if (armorScalingConfigManager == null) return 0;

        String currentFileName = StringArgumentType.getString(context, CommandArg.FILE);
        if (armorScalingConfigManager.switchConfigFile(currentFileName)) {
            ArmorScaling.LOGGER.info("Switch armor scaling config file to {} via command", currentFileName);
            context.getSource().sendSuccess(() -> Component.translatable("armorscaling.message.switch_armor_scaling_config_file", currentFileName), true);
            return Command.SINGLE_SUCCESS;
        } else {
            context.getSource().sendFailure(Component.translatable("armorscaling.message.no_armor_scaling_config_file", currentFileName));
            return 0;
        }
    }

}