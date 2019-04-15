package com.serzhan.second.contentprovider.util;

import android.content.ContentValues;
import android.database.Cursor;

import com.serzhan.second.contentprovider.entity.Note;

import java.util.ArrayList;
import java.util.List;

public class ConvertUtils {
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CONTENT = "content";
    public static final String COLUMN_CREATED = "created";

    public static List<Note> parseCursorToNotes(Cursor cursor) {
        List<Note> temp = new ArrayList<>();
        while (cursor.moveToNext()) {
            temp.add(parseCursorToNote(cursor));
        }
        return temp;
    }

    public static Note parseCursorToNote(Cursor cursor) {
        Note note = new Note();
        note.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_ID)));
        note.setContent(cursor.getString(cursor.getColumnIndex(COLUMN_CONTENT)));
        return note;
    }

    public static ContentValues getNoteContentValues(Note note) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID, note.getId());
        contentValues.put(COLUMN_CONTENT, note.getContent());
        return contentValues;
    }

    public static Note convertContentValuesToNote(ContentValues contentValues) {
        Note note = new Note();
        note.setId(contentValues.getAsInteger(COLUMN_ID));
        note.setContent(contentValues.getAsString(COLUMN_CONTENT));
        return note;
    }
}
