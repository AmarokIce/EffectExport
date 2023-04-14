package cn.mcmod.effect_export;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.text.LiteralText;


import java.io.IOException;

import static net.minecraft.server.command.CommandManager.*;

public class EffectExporet implements ModInitializer {
    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((source, registryAccess) ->
                source.register(literal("effectexport").then(argument("modid", StringArgumentType.string()).executes(context -> {
                    try {
                        new ImgHelper(StringArgumentType.getString(context, "modid"));
                        context.getSource().sendFeedback(new LiteralText("Success!"), false);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    return 1;
                }))));
    }
}
