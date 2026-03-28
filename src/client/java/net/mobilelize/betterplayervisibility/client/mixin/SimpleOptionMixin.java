package net.mobilelize.betterplayervisibility.client.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.mobilelize.betterplayervisibility.client.utils.ISimpleOption;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Objects;
import java.util.function.Consumer;

@Mixin(OptionInstance.class)
public class SimpleOptionMixin<T> implements ISimpleOption<T> {
    @Shadow
    T value;

    @Shadow @Final private Consumer<T> onValueUpdate;

    @Override
    public void betterPlayerVisibility$forceSetValue(T newValue) {
        if(!Minecraft.getInstance().isRunning())
        {
            value = newValue;
            return;
        }

        if(!Objects.equals(value, newValue))
        {
            value = newValue;
            onValueUpdate.accept(value);
        }
    }
}
