package com.example.graphicsmaker.scale;

import android.graphics.Bitmap.Config;
import androidx.annotation.NonNull;
import java.lang.reflect.InvocationTargetException;

public class CompatDecoderFactory<T> implements DecoderFactory<T> {
    private Config bitmapConfig;
    private Class<? extends T> clazz;

    public CompatDecoderFactory(@NonNull Class<? extends T> clazz) {
        this(clazz, null);
    }

    public CompatDecoderFactory(@NonNull Class<? extends T> clazz, Config bitmapConfig) {
        this.clazz = clazz;
        this.bitmapConfig = bitmapConfig;
    }

    public T make() throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        if (this.bitmapConfig == null) {
            return this.clazz.newInstance();
        }
        return this.clazz.getConstructor(new Class[]{Config.class}).newInstance(new Object[]{this.bitmapConfig});
    }
}
