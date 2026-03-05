package xiao.armorscaling.command.sub;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import xiao.armorscaling.ArmorScaling;
import xiao.armorscaling.api.scaling.damage.IDamageScalingManager;

import static xiao.armorscaling.command.CommandArg.*;

public class DamageScalingCommand {

    public static LiteralArgumentBuilder<CommandSourceStack> get() {
        return Commands.literal(DAMAGE_SCALING)
                .executes(DamageScalingCommand::checkDamageScaling)
                .then(Commands.argument(BOOL, BoolArgumentType.bool())
                        .executes(DamageScalingCommand::turnDamageScaling)
                );
    }

    private static int turnDamageScaling(CommandContext<CommandSourceStack> context) {
        boolean turn = BoolArgumentType.getBool(context, BOOL);
        IDamageScalingManager damageScalingManager = ArmorScaling.getArmorScalingManager().getDamageScalingManager();
        damageScalingManager.setEnabled(turn);
        if (turn) {
            context.getSource().sendSuccess(() -> Component.translatable("armorscaling.message.enable_armor_damage_scaling"), true);
        } else {
            context.getSource().sendSuccess(() -> Component.translatable("armorscaling.message.disable_armor_damage_scaling"), true);
        }
        return Command.SINGLE_SUCCESS;
    }
    private static int checkDamageScaling(CommandContext<CommandSourceStack> context) {
        if (ArmorScaling.getArmorScalingManager().getDamageScalingManager().isEnabled()) {
            context.getSource().sendSuccess(() -> Component.translatable("armorscaling.message.damage_scaling_enabled"), false);
        } else {
            context.getSource().sendSuccess(() -> Component.translatable("armorscaling.message.damage_scaling_disabled"), false);
        }
        return Command.SINGLE_SUCCESS;
    }
}
