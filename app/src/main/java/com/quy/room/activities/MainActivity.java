package com.quy.room.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.quy.room.R;
import com.quy.room.adapter.NoteAdapter;
import com.quy.room.database.NotesDatabase;
import com.quy.room.entities.Note;
import com.quy.room.listeners.NoteListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NoteListener {

    private final static int REQUEST_CODE_ADD_NOTE = 1;
    private final static int REQUEST_CODE_UPDATE_NOTE = 2;
    Button iconButtonAdd;
    private List<Note> notess;
    public NoteAdapter noteAdapter;
    private RecyclerView listNote;
    private int noteClickPosition = -1;

    private EditText inputSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iconButtonAdd = findViewById(R.id.iconButtonAdd);
        listNote = findViewById(R.id.notesList);
        inputSearch = findViewById(R.id.inputSearch);
        listNote.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        notess = new ArrayList<>();
        getNotes();
        noteAdapter = new NoteAdapter(MainActivity.this, (ArrayList<Note>) notess, this);
        listNote.setAdapter(noteAdapter);



        iconButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,CreateNoteActivity.class));
            }
        });

        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Toast.makeText(MainActivity.this, "work", Toast.LENGTH_SHORT).show();
                noteAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getNotes();
    }

    void getNotes(){
        class HelperGet extends AsyncTask<Void,Void, List<Note>>{

            @Override
            protected List<Note> doInBackground(Void... voids) {
                return NotesDatabase.getDatabase(MainActivity.this).noteDAO().getNotes();
            }

            @Override
            protected void onPostExecute(List<Note> notes) {
                super.onPostExecute(notes);
                notess.clear();
                notess.addAll(notes);
                noteAdapter.notifyDataSetChanged();
            }
        }
        new HelperGet().execute();
    }


    @Override
    public void noteOnClicked(Note note, int position) {
        noteClickPosition = position;
        Intent intent = new Intent(MainActivity.this,CreateNoteActivity.class);
        intent.putExtra("isViewOrUpdate",true);
        intent.putExtra("note",note);
        startActivityForResult(intent,REQUEST_CODE_UPDATE_NOTE);
    }
}