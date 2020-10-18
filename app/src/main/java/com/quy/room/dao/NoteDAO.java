package com.quy.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.quy.room.entities.Note;

import java.util.List;

@Dao
public interface NoteDAO {

    @Query("SELECT * FROM notes ORDER BY id DESC")
    public List<Note> getNotes();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertNote(Note note);

    @Delete
    public void delNote(Note note);



}
