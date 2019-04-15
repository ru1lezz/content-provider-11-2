package com.serzhan.second.contentprovider;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SharedPrefHelper {
    public static final String SHARED_PREF_NAME = "shared_pref";
    public static final String TEXT_SIZE = "text_size";
    public static final String TEXT_COLOR = "text_color";
    public static final int DEFAULT_COLOR = 0xFFFFFFFF;
    public static final int DEFAULT_SIZE = 10;

    private Context context;
    private ExecutorService executorService;

    public SharedPrefHelper(Context context) {
        this.context = context;
        executorService = Executors.newSingleThreadExecutor();
    }

    public void setTextSize(int textSize) {
        executorService.execute(() -> {
            SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(TEXT_SIZE, textSize);
            editor.commit();
        });
    }

    public void setTextColor(int textColor) {
        executorService.execute(() -> {
            SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(TEXT_COLOR, textColor);
            editor.commit();
        });
    }

    public int getTextSize() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(TEXT_SIZE, DEFAULT_SIZE);
    }

    public int getTextColor() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(TEXT_COLOR, DEFAULT_COLOR);
    }
}
