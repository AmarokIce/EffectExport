package cn.mcmod.effect_export;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

@Mod(EffectExport.MODID)
public class EffectExport {
    public static final String MODID = "effect_export";
    public static final Logger LOGGER = LogManager.getLogger(MODID);

    public EffectExport() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    boolean init = false;
    private void register(CommandDispatcher<CommandSourceStack> event) {
        CommandEffectExport(event);
    }

    @SubscribeEvent
    public void registerCommand(ServerStartedEvent event) {
        if (!init) {
            register(event.getServer().getCommands().getDispatcher());
            init = true;
        }
    }

    @SubscribeEvent
    public void register(RegisterCommandsEvent event) {
        if (init) register(event.getDispatcher());
    }

    @SubscribeEvent
    public void onServerStopped(ServerStoppedEvent event) {
        init = false;
    }

    public static void CommandEffectExport(CommandDispatcher<CommandSourceStack> event) {
        event.register(Commands.literal("effectexport").then(Commands.argument("modid", StringArgumentType.string()).executes(export -> {
            try {
                new ImgHelper(StringArgumentType.getString(export, "modid"));
                export.getSource().getPlayerOrException().sendSystemMessage(Component.literal("Successful!"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return 0;
        })));
    }
}
