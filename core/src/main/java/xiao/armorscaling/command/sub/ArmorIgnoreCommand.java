package xiao.armorscaling.command.sub;


import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import xiao.armorscaling.ArmorScaling;
import xiao.armorscaling.api.scaling.armorignore.IArmorIgnoreManager;

import static xiao.armorscaling.command.CommandArg.*;

public class ArmorIgnoreCommand {

    public static LiteralArgumentBuilder<CommandSourceStack> get() {
        return Commands.literal(ARMOR_IGNORE)
                .executes(ArmorIgnoreCommand::checkArmorIgnore)
                .then(Commands.argument(BOOL, BoolArgumentType.bool())
                        .executes(ArmorIgnoreCommand::turnArmorIgnore)
                )
                .then(Commands.literal(SCALE)
                        .executes(ArmorIgnoreCommand::checkArmorIgnoreScale)
                        .then(Commands.argument(RATIO, DoubleArgumentType.doubleArg(0, 1))
                                .executes(ArmorIgnoreCommand::setArmorIgnoreScale)
                        )
                );
    }

    private static int turnArmorIgnore(CommandContext<CommandSourceStack> context) {
        boolean turn = BoolArgumentType.getBool(context, BOOL);
        IArmorIgnoreManager armorIgnoreManager = ArmorScaling.getArmorScalingManager().getArmorIgnoreManager();
        armorIgnoreManager.setEnabled(turn);
        if (turn) {
            context.getSource().sendSuccess(() -> Component.translatable("armorscaling.message.enable_armor_ignore"), true);
        } else {
            context.getSource().sendSuccess(() -> Component.translatable("armorscaling.message.disable_armor_ignore"), true);
        }
        return Command.SINGLE_SUCCESS;
    }
    private static int checkArmorIgnore(CommandContext<CommandSourceStack> context) {
        if (ArmorScaling.getArmorScalingManager().getArmorIgnoreManager().isEnabled()) {
            context.getSource().sendSuccess(() -> Component.translatable("armorscaling.message.armor_ignore_enabled"), false);
        } else {
            context.getSource().sendSuccess(() -> Component.translatable("armorscaling.message.armor_ignore_disabled"), false);
        }
        return Command.SINGLE_SUCCESS;
    }

    private static int setArmorIgnoreScale(CommandContext<CommandSourceStack> context) {
        float ratio = (float) DoubleArgumentType.getDouble(context, RATIO);
        IArmorIgnoreManager armorIgnoreManager = ArmorScaling.getArmorScalingManager().getArmorIgnoreManager();
        armorIgnoreManager.setArmorIgnoreScale(ratio);
        context.getSource().sendSuccess(() -> Component.translatable("armorscaling.message.set_armor_ignore_scale", armorIgnoreManager.getArmorIgnoreScale()), true);
        return Command.SINGLE_SUCCESS;
    }
    private static int checkArmorIgnoreScale(CommandContext<CommandSourceStack> context) {
        float ratio = ArmorScaling.getArmorScalingManager().getArmorIgnoreManager().getArmorIgnoreScale();
        context.getSource().sendSuccess(() -> Component.translatable("armorscaling.message.armor_ignore_ratio", ratio), false);
        return Command.SINGLE_SUCCESS;
    }
}
