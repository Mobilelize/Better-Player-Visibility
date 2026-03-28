package net.mobilelize.betterplayervisibility.client.utils;

import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.util.Objects;

public class BlockRenderUtils {

    public static void barrier(){
        ResourceManagerHelper.registerBuiltinResourcePack(
                //Didn't know if this was going to work, but it did
                Objects.requireNonNull(Identifier.fromNamespaceAndPath("betterplayervisibility", "barrier")),
                //new Identifier("betterplayervisibility", "barrier"),
                FabricLoader.getInstance().getModContainer("betterplayervisibility").orElseThrow(),
                Component.literal("Connected Barriers"),
                ResourcePackActivationType.NORMAL
        );
    }
}
