package com.serzhan.second.contentprovider.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

@Entity
public class Note {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String content;

    @ColumnInfo(name = "creation_date")
    private Date creationDate;

    public Note() {
        creationDate = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault()).getTime();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
