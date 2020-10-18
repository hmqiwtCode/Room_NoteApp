package com.quy.room.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.quy.room.R;
import com.quy.room.activities.CreateNoteActivity;
import com.quy.room.activities.MainActivity;
import com.quy.room.database.NotesDatabase;
import com.quy.room.entities.Note;
import com.quy.room.filter.SearchNote;
import com.quy.room.listeners.NoteListener;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> implements Filterable {
    Context context;
    public ArrayList<Note> notes;
    NoteListener noteListener;
    List<Note> filterResult;
    SearchNote searchNote;
    public NoteAdapter(Context context,ArrayList<Note> notes, NoteListener noteListener){
        this.context = context;
        this.notes = notes;
        this.noteListener = noteListener;
        filterResult = this.notes;
    }



    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_container_note,parent,false);
        Log.d("Howwork","1");
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, final int position) {
        final Note note = notes.get(position);
        Log.d("Howwork","2");
        holder.setText(note);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noteListener.noteOnClicked(note,position);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new AlertDialog.Builder(context).setTitle("Delete.").setMessage("Do you want to delete?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                class HandleDelete extends AsyncTask<Void,Void,ArrayList>{
                                    @Override
                                    protected ArrayList<Note> doInBackground(Void... voids) {
                                        NotesDatabase.getDatabase(context).noteDAO().delNote(note);
                                        return notes;
                                    }
                                    @Override
                                    protected void onPostExecute(ArrayList arrayList) {
                                        super.onPostExecute(arrayList);
                                        notes.remove(note);
                                        ((MainActivity) noteListener).noteAdapter.notifyDataSetChanged();
                                    }
                                }
                                new HandleDelete().execute();

                            }
                        }).show();
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    @Override
    public Filter getFilter() {
        if (searchNote == null){
            searchNote = new SearchNote(filterResult,this);
        }
        return searchNote;
    }

    class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle,tvSubTitle,tvDateTime;
        LinearLayout linearLayout;
        ImageView imageDis;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvSubTitle = itemView.findViewById(R.id.tvSubTitle);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
            linearLayout = itemView.findViewById(R.id.llNote);
            imageDis = itemView.findViewById(R.id.imageDis);




        }

        void setText(Note note){
            tvTitle.setText(note.getTitle());
            tvSubTitle.setText(note.getSubTitle());
            tvDateTime.setText(note.getDateTime());

            GradientDrawable gradientDrawable = (GradientDrawable) linearLayout.getBackground();
            if (note.getColor() != null){
                gradientDrawable.setColor(Color.parseColor(note.getColor()));
            }else{
                gradientDrawable.setColor(Color.parseColor("#333333"));
            }

            if(note.getImagePath() != null){
                imageDis.setImageBitmap(BitmapFactory.decodeFile(note.getImagePath()));
                imageDis.setVisibility(View.VISIBLE);
            }else{
                imageDis.setVisibility(View.GONE);
            }

        }
    }
}
