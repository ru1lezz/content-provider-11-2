package com.serzhan.second.contentprovider.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.serzhan.second.contentprovider.converters.DateConverter;
import com.serzhan.second.contentprovider.entity.Note;

@Database(entities = {Note.class}, version = 1)
@TypeConverters({DateConverter.class})
public abstract class NoteDatabase extends RoomDatabase {

    public abstract NoteDAO getNoteDao();

    private static NoteDatabase db;

    public static NoteDatabase getInstance(Context context) {
        if(null == db) {
            db = buildDatabase(context);
        }
        return db;
    }

    private static NoteDatabase buildDatabase(Context context) {
        return Room.databaseBuilder(context,
                NoteDatabase.class, "note_db")
                .fallbackToDestructiveMigration()
                .build();
    }
}