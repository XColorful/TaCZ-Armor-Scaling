package xiao.armorscaling.command.sub;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import xiao.armorscaling.data.io.TempDataManager;

import static xiao.armorscaling.api.data.TempDataTag.TACZ_ARMOR_SCALING;
import static xiao.armorscaling.api.data.TempDataTag.TURN_DAMAGE_SCALING;
import static xiao.armorscaling.command.CommandArg.*;

public class DamageScalingCommand {

    public static LiteralArgumentBuilder<CommandSourceStack> get() {
        return Commands.literal(DAMAGE_SCALING)
                .then(Commands.argument(BOOL, BoolArgumentType.bool())
                        .executes(DamageScalingCommand::turnDamageScaling)
                );
    }

    private static int turnDamageScaling(CommandContext<CommandSourceStack> context) {
        boolean turn = BoolArgumentType.getBool(context, BOOL);
        TempDataManager tempDataManager = TempDataManager.get();
        tempDataManager.writeBool(TACZ_ARMOR_SCALING, TURN_DAMAGE_SCALING, turn);
        tempDataManager.saveTempData();
        if (turn) {
            context.getSource().sendSuccess(() -> Component.translatable("armorscaling.message.enable_armor_ignore"), false);
        } else {
            context.getSource().sendSuccess(() -> Component.translatable("armorscaling.message.disable_armor_ignore"), false);
        }
        return Command.SINGLE_SUCCESS;
    }
}
