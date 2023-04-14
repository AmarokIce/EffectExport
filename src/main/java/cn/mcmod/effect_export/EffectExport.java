package cn.mcmod.effect_export;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.ScoreTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.UUID;

@Mod(EffectExport.MODID)
public class EffectExport {
    public static final String MODID = "effect_export";
    public static final Logger LOGGER = LogManager.getLogger(MODID);

    public EffectExport() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    boolean init = false;
    private void register(CommandDispatcher<CommandSource> event) {
        CommandEffectExport(event);
    }

    @SubscribeEvent
    public void registerCommand(FMLServerStartingEvent event) {
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
    public void onServerStopped(FMLServerStoppingEvent event) {
        init = false;
    }

    public static void CommandEffectExport(CommandDispatcher<CommandSource> event) {
        event.register(Commands.literal("effectexport").then(Commands.argument("modid", StringArgumentType.string()).executes(export -> {
            try {
                new ImgHelper(StringArgumentType.getString(export, "modid"));
                export.getSource().getPlayerOrException().sendMessage(new StringTextComponent("Successful!"), UUID.randomUUID());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return 0;
        })));
    }
}
