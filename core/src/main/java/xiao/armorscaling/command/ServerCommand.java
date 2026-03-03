package xiao.armorscaling.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import xiao.armorscaling.command.sub.ArmorIgnoreCommand;
import xiao.armorscaling.command.sub.DamageScalingCommand;
import xiao.armorscaling.command.sub.DurabilityScalingCommand;

import static xiao.armorscaling.command.CommandArg.MOD_ID;
import static xiao.armorscaling.command.CommandArg.MOD_NAME_SHORT;

public class ServerCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(get(MOD_ID));
        dispatcher.register(get(MOD_NAME_SHORT));
    }

    public static LiteralArgumentBuilder<CommandSourceStack> get(String rootName) {
        LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal(rootName);
        root.then(DamageScalingCommand.get()
                .requires(source -> source.hasPermission(2)));
        root.then(DurabilityScalingCommand.get()
                .requires(source -> source.hasPermission(2)));
        root.then(ArmorIgnoreCommand.get()
                .requires(source -> source.hasPermission(2)));

        return root;
    }
}
