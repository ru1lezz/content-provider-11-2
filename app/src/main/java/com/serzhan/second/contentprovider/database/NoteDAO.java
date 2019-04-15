package com.serzhan.second.contentprovider.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.serzhan.second.contentprovider.entity.Note;

import java.util.List;

@Dao
public interface NoteDAO {

    @Query("SELECT * FROM note WHERE id = :id")
    Note getNoteByID(long id);

    @Query("SELECT * FROM note")
    List<Note> getNotes();

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Note note);

    @Insert(onConflict = OnConflictStrategy.FAIL)
    void insert(Note note);

    @Delete
    void delete(Note note);

    @Query("DELETE FROM note WHERE id = :id")
    void deleteById(long id);
}
