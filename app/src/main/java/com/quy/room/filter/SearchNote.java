package com.quy.room.filter;

import android.content.Context;
import android.util.Log;
import android.widget.Filter;

import com.quy.room.activities.MainActivity;
import com.quy.room.adapter.NoteAdapter;
import com.quy.room.entities.Note;
import com.quy.room.listeners.NoteListener;

import java.util.ArrayList;
import java.util.List;

public class SearchNote extends Filter { List<Note> listNoteOriginal;

    NoteAdapter noteAdapter;
    public SearchNote(List<Note> listNoteOriginal,NoteAdapter noteAdapter){
        this.listNoteOriginal = listNoteOriginal;
        this.noteAdapter = noteAdapter;
    }


    @Override
    protected FilterResults performFiltering(CharSequence charSequence) {
        List<Note> listNotesResult;
        if (charSequence.length() == 0){
            listNotesResult = listNoteOriginal;
        }else{
            listNotesResult = getFilterResult(charSequence.toString().toLowerCase());
        }
        FilterResults results = new FilterResults();
        results.count = listNotesResult.size();
        results.values = listNotesResult;
        return results;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
        noteAdapter.notes = (ArrayList<Note>) filterResults.values;
        for (Note n : (List<Note>) filterResults.values){
            Log.d("VALUE",n.getTitle());
        }
        noteAdapter.notifyDataSetChanged();

    }

    protected List<Note> getFilterResult(String constraint){
        List<Note> result = new ArrayList<>();
        for (Note note : listNoteOriginal){
            if (note.getTitle().toLowerCase().contains(constraint))
                result.add(note);
        }
        return result;
    }
}
