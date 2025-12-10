package net.mobilelize.betterplayervisibility.client.utils;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Objects;

public class BlockRenderUtils {

    public static void barrier(){
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.BARRIER, RenderLayer.getCutout());

        ResourceManagerHelper.registerBuiltinResourcePack(
                //Didn't know if this was going to work, but it did
                Objects.requireNonNull(Identifier.tryParse("betterplayervisibility", "barrier")),
                //new Identifier("betterplayervisibility", "barrier"),
                FabricLoader.getInstance().getModContainer("betterplayervisibility").orElseThrow(),
                Text.literal("Connected Barriers"),
                ResourcePackActivationType.NORMAL
        );
    }
}
