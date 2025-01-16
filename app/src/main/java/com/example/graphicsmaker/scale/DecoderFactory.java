package com.example.graphicsmaker.scale;

import java.lang.reflect.InvocationTargetException;

public interface DecoderFactory<T> {
    T make() throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException;
}
