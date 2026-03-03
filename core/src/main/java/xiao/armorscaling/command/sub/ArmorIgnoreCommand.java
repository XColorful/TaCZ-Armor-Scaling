package xiao.armorscaling.command.sub;


import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import xiao.armorscaling.data.io.TempDataManager;

import static xiao.armorscaling.api.data.TempDataTag.*;
import static xiao.armorscaling.command.CommandArg.*;

public class ArmorIgnoreCommand {

    public static LiteralArgumentBuilder<CommandSourceStack> get() {
        return Commands.literal(ARMOR_IGNORE)
                .then(Commands.argument(BOOL, BoolArgumentType.bool())
                        .executes(ArmorIgnoreCommand::turnArmorIgnore)
                )
                .then(Commands.literal(SCALE)
                        .then(Commands.argument(RATIO, DoubleArgumentType.doubleArg(0, 1))
                                .executes(ArmorIgnoreCommand::setArmorIgnoreScale)
                        )
                );
    }

    private static int turnArmorIgnore(CommandContext<CommandSourceStack> context) {
        boolean turn = BoolArgumentType.getBool(context, BOOL);
        TempDataManager tempDataManager = TempDataManager.get();
        tempDataManager.writeBool(TACZ_ARMOR_SCALING, TURN_ARMOR_IGNORE, turn);
        tempDataManager.saveTempData();
        if (turn) {
            context.getSource().sendSuccess(() -> Component.translatable("armorscaling.message.enable_armor_ignore"), true);
        } else {
            context.getSource().sendSuccess(() -> Component.translatable("armorscaling.message.disable_armor_ignore"), true);
        }
        return Command.SINGLE_SUCCESS;
    }

    private static int setArmorIgnoreScale(CommandContext<CommandSourceStack> context) {
        double ratio = DoubleArgumentType.getDouble(context, RATIO);
        TempDataManager tempDataManager = TempDataManager.get();
        tempDataManager.writeDouble(TACZ_ARMOR_SCALING, ARMOR_IGNORE_SCALE, ratio);
        tempDataManager.saveTempData();
        context.getSource().sendSuccess(() -> Component.translatable("armorscaling.message.set_armor_ignore_scale", ratio), true);
        return Command.SINGLE_SUCCESS;
    }
}
