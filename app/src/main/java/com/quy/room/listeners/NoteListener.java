package com.quy.room.listeners;

import com.quy.room.entities.Note;

public interface NoteListener {
    void noteOnClicked(Note note, int position);
}
