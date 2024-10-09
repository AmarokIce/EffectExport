package cn.mcmod.effect_export;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

@Mod(EffectExport.MODID)
public class EffectExport {
    public static final String MODID = "effect_export";
    public static final Logger LOGGER = LogManager.getLogger(MODID);

    public EffectExport() {
        NeoForge.EVENT_BUS.register(this);
    }

    boolean init = false;
    private void register(CommandDispatcher<CommandSourceStack> event) {
        commandEffectExport(event);
    }

    @SubscribeEvent
    public void registerCommand(ServerStartedEvent event) {
        if (init) return;
        register(event.getServer().getCommands().getDispatcher());
        init = true;
    }

    @SubscribeEvent
    public void register(RegisterCommandsEvent event) {
        if (init) register(event.getDispatcher());
    }

    @SubscribeEvent
    public void onServerStopped(ServerStoppedEvent event) {
        init = false;
    }

    public static void commandEffectExport(CommandDispatcher<CommandSourceStack> event) {
        event.register(Commands.literal("effectexport").then(Commands.argument("modid", StringArgumentType.string()).executes(export -> {
            try {
                new ImgHelper(StringArgumentType.getString(export, "modid"));
                export.getSource().getPlayerOrException().sendSystemMessage(Component.literal("Successful!"));
            } catch (IOException e) {
                export.getSource().getPlayerOrException().sendSystemMessage(Component.literal("Fail!"));
                throw new RuntimeException(e);
            }
            return 0;
        })));
    }
}
