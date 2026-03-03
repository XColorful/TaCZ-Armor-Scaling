package xiao.armorscaling.init;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import xiao.armorscaling.command.ClientCommand;
import xiao.armorscaling.command.ServerCommand;
import xiao.battleroyale.api.init.ICommandRegistry;

public class CommandRegistry implements ICommandRegistry {

    private static final CommandRegistry INSTANCE = new CommandRegistry();

    public static CommandRegistry get() {
        return INSTANCE;
    }

    private CommandRegistry() {}

    @Override
    public void registerServerCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
        ServerCommand.register(dispatcher);
    }

    @Override
    public void registerClientCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
        ClientCommand.register(dispatcher);
    }
}
