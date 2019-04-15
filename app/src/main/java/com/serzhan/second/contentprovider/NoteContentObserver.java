package com.serzhan.second.contentprovider;

import android.database.ContentObserver;
import android.os.Handler;

public class NoteContentObserver extends ContentObserver {

    private Runnable mRunnable;

    public NoteContentObserver(Handler handler, Runnable runnable) {
        super(handler);
        mRunnable = runnable;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        mRunnable.run();
    }
}
