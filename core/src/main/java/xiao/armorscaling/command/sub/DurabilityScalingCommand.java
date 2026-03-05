package xiao.armorscaling.command.sub;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import xiao.armorscaling.ArmorScaling;
import xiao.armorscaling.api.scaling.durability.IDurabilityScalingManager;

import static xiao.armorscaling.command.CommandArg.*;

public class DurabilityScalingCommand {

    public static LiteralArgumentBuilder<CommandSourceStack> get() {
        return Commands.literal(DURABILITY_SCALING)
                .then(Commands.argument(BOOL, BoolArgumentType.bool())
                        .executes(DurabilityScalingCommand::turnDurabilityScaling)
                )
                .then(Commands.literal(KEEP_ITEM_GAME_ID)
                        .then(Commands.argument(BOOL, BoolArgumentType.bool())
                                .executes(DurabilityScalingCommand::setKeepItemGameId)
                        )
                )
                .then(Commands.literal(REPLACE_ITEM_REMOVE_GAME_ID)
                        .then(Commands.argument(BOOL, BoolArgumentType.bool())
                                .executes(DurabilityScalingCommand::setReplaceItemRemoveGameId)
                        )
                );
    }

    private static int turnDurabilityScaling(CommandContext<CommandSourceStack> context) {
        boolean turn = BoolArgumentType.getBool(context, BOOL);
        IDurabilityScalingManager durabilityScalingManager = ArmorScaling.getArmorScalingManager().getDurabilityScalingManager();
        durabilityScalingManager.setEnabled(turn);
        if (turn) {
            context.getSource().sendSuccess(() -> Component.translatable("armorscaling.message.enable_durability_scaling"), false);
        } else {
            context.getSource().sendSuccess(() -> Component.translatable("armorscaling.message.disable_durability_scaling"), false);
        }
        return Command.SINGLE_SUCCESS;
    }

    private static int setKeepItemGameId(CommandContext<CommandSourceStack> context) {
        boolean shouldKeep = BoolArgumentType.getBool(context, BOOL);
        IDurabilityScalingManager durabilityScalingManager = ArmorScaling.getArmorScalingManager().getDurabilityScalingManager();
        durabilityScalingManager.setKeepItemGameId(shouldKeep);
        if (shouldKeep) {
            context.getSource().sendSuccess(() -> Component.translatable("armorscaling.message.enable_durability_scaling_keep_gameid"), false);
        } else {
            context.getSource().sendSuccess(() -> Component.translatable("armorscaling.message.disable_durability_scaling_keep_gameid"), false);
        }
        return Command.SINGLE_SUCCESS;
    }

    private static int setReplaceItemRemoveGameId(CommandContext<CommandSourceStack> context) {
        boolean shouldRemove = BoolArgumentType.getBool(context, BOOL);
        IDurabilityScalingManager durabilityScalingManager = ArmorScaling.getArmorScalingManager().getDurabilityScalingManager();
        durabilityScalingManager.setReplaceItemRemoveGameId(shouldRemove);
        if (shouldRemove) {
            context.getSource().sendSuccess(() -> Component.translatable("armorscaling.message.enable_durability_scaling_remove_gameid"), false);
        } else {
            context.getSource().sendSuccess(() -> Component.translatable("armorscaling.message.disable_durability_scaling_remove_gameid"), false);
        }
        return Command.SINGLE_SUCCESS;
    }
}
