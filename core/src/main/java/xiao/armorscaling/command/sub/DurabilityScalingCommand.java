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
                .executes(DurabilityScalingCommand::checkDurabilityScaling)
                .then(Commands.argument(BOOL, BoolArgumentType.bool())
                        .executes(DurabilityScalingCommand::turnDurabilityScaling)
                )
                .then(Commands.literal(KEEP_ITEM_GAME_ID)
                        .executes(DurabilityScalingCommand::checkKeepItemGameId)
                        .then(Commands.argument(BOOL, BoolArgumentType.bool())
                                .executes(DurabilityScalingCommand::setKeepItemGameId)
                        )
                )
                .then(Commands.literal(REPLACE_ITEM_REMOVE_GAME_ID)
                        .executes(DurabilityScalingCommand::checkReplaceItemRemoveGameId)
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
            context.getSource().sendSuccess(() -> Component.translatable("armorscaling.message.enable_durability_scaling"), true);
        } else {
            context.getSource().sendSuccess(() -> Component.translatable("armorscaling.message.disable_durability_scaling"), true);
        }
        return Command.SINGLE_SUCCESS;
    }
    private static int checkDurabilityScaling(CommandContext<CommandSourceStack> context) {
        if (ArmorScaling.getArmorScalingManager().getDurabilityScalingManager().isEnabled()) {
            context.getSource().sendSuccess(() -> Component.translatable("armorscaling.message.durability_scaling_enabled"), false);
        } else {
            context.getSource().sendSuccess(() -> Component.translatable("armorscaling.message.durability_scaling_disabled"), false);
        }
        return Command.SINGLE_SUCCESS;
    }

    private static int setKeepItemGameId(CommandContext<CommandSourceStack> context) {
        boolean shouldKeep = BoolArgumentType.getBool(context, BOOL);
        IDurabilityScalingManager durabilityScalingManager = ArmorScaling.getArmorScalingManager().getDurabilityScalingManager();
        durabilityScalingManager.setKeepItemGameId(shouldKeep);
        if (shouldKeep) {
            context.getSource().sendSuccess(() -> Component.translatable("armorscaling.message.enable_durability_scaling_keep_gameid"), true);
        } else {
            context.getSource().sendSuccess(() -> Component.translatable("armorscaling.message.disable_durability_scaling_keep_gameid"), true);
        }
        return Command.SINGLE_SUCCESS;
    }
    private static int checkKeepItemGameId(CommandContext<CommandSourceStack> context) {
        if (ArmorScaling.getArmorScalingManager().getDurabilityScalingManager().keepItemGameId()) {
            context.getSource().sendSuccess(() -> Component.translatable("armorscaling.message.durability_keep_gameid_enabled"), false);
        } else {
            context.getSource().sendSuccess(() -> Component.translatable("armorscaling.message.durability_keep_gameid_disabled"), false);
        }
        return Command.SINGLE_SUCCESS;
    }

    private static int setReplaceItemRemoveGameId(CommandContext<CommandSourceStack> context) {
        boolean shouldRemove = BoolArgumentType.getBool(context, BOOL);
        IDurabilityScalingManager durabilityScalingManager = ArmorScaling.getArmorScalingManager().getDurabilityScalingManager();
        durabilityScalingManager.setReplaceItemRemoveGameId(shouldRemove);
        if (shouldRemove) {
            context.getSource().sendSuccess(() -> Component.translatable("armorscaling.message.enable_durability_scaling_remove_gameid"), true);
        } else {
            context.getSource().sendSuccess(() -> Component.translatable("armorscaling.message.disable_durability_scaling_remove_gameid"), true);
        }
        return Command.SINGLE_SUCCESS;
    }
    private static int checkReplaceItemRemoveGameId(CommandContext<CommandSourceStack> context) {
        if (ArmorScaling.getArmorScalingManager().getDurabilityScalingManager().replaceItemRemoveGameId()) {
            context.getSource().sendSuccess(() -> Component.translatable("armorscaling.message.durability_remove_gameid_enabled"), false);
        } else {
            context.getSource().sendSuccess(() -> Component.translatable("armorscaling.message.durability_remove_gameid_disabled"), false);
        }
        return Command.SINGLE_SUCCESS;
    }
}
