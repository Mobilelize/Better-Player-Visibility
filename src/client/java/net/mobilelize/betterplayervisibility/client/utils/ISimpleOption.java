package net.mobilelize.betterplayervisibility.client.utils;

import net.minecraft.client.OptionInstance;

public interface ISimpleOption<T>{
    void betterPlayerVisibility$forceSetValue(T newValue);

    /**
     * Returns the given SimpleOption object as an ISimpleOption, allowing you
     * to access the forceSetValue() method.
     */
    @SuppressWarnings("unchecked")
    static <T> ISimpleOption<T> get(OptionInstance<T> option)
    {
        return (ISimpleOption<T>)(Object)option;
    }
}
