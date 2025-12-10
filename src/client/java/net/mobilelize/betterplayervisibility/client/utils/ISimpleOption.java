package net.mobilelize.betterplayervisibility.client.utils;

import net.minecraft.client.option.SimpleOption;

public interface ISimpleOption<T>{
    void betterPlayerVisibility$forceSetValue(T newValue);

    /**
     * Returns the given SimpleOption object as an ISimpleOption, allowing you
     * to access the forceSetValue() method.
     */
    @SuppressWarnings("unchecked")
    static <T> ISimpleOption<T> get(SimpleOption<T> option)
    {
        return (ISimpleOption<T>)(Object)option;
    }
}
