package com.quy.room.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.quy.room.R;
import com.quy.room.database.NotesDatabase;
import com.quy.room.entities.Note;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class CreateNoteActivity extends AppCompatActivity {

    private EditText title,content_title,real;
    private TextView tvDate;
    private ImageView btnSave, btnBack, imageNote;
    private final int REQUEST_EXTERNAL_STORAGE = 1;
    private TextView tvWebUrl;
    private LinearLayout llPopup;
    private AlertDialog alertDialog;
    String color,fileImage;
    private Note noteAvailable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        tvDate = findViewById(R.id.date_title);
        title = findViewById(R.id.title);
        content_title = findViewById(R.id.content_title);
        real = findViewById(R.id.real);
        btnSave = findViewById(R.id.btnSave);
        btnBack = findViewById(R.id.btnBack);
        imageNote = findViewById(R.id.imageNote);
        tvWebUrl = findViewById(R.id.tvWebUrl);
        llPopup = findViewById(R.id.llPopup);

        color = "#333333";
        changeColor(color);

        tvDate.setText(new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm a", Locale.getDefault()).format(new Date()));

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNote();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();
        if (intent.getBooleanExtra("isViewOrUpdate",false) == true){
            noteAvailable = (Note) intent.getSerializableExtra("note");
            title.setText(noteAvailable.getTitle());
            tvDate.setText(noteAvailable.getDateTime());
            content_title.setText(noteAvailable.getSubTitle());
            real.setText(noteAvailable.getNoteText());

            if (noteAvailable.getImagePath() != null){
                if (noteAvailable.getImagePath() != ""){
                    imageNote.setImageBitmap(BitmapFactory.decodeFile(noteAvailable.getImagePath()));
                    imageNote.setVisibility(View.VISIBLE);
                    findViewById(R.id.imageRemoveWebImage).setVisibility(View.VISIBLE);
                    fileImage = noteAvailable.getImagePath();
                }

            }

            if (noteAvailable.getWebLink() != null){
                tvWebUrl.setText(noteAvailable.getWebLink());
                llPopup.setVisibility(View.VISIBLE);
            }
        }
        findViewById(R.id.imageRemoveWebImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fileImage = "";
                imageNote.setVisibility(View.GONE);
                findViewById(R.id.imageRemoveWebImage).setVisibility(View.GONE);
            }
        });

        findViewById(R.id.imageRemoveWebURL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llPopup.setVisibility(View.GONE);
                tvWebUrl.setText("");

            }
        });

        initMescelleaneous();
        pickColorTreeBlack();
        pickColorTreeBlue();
        pickColorTreeRed();
        pickColorTreeWhite();
        pickColorTreeYellow();



    }

    private void saveNote(){
        if(title.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "Title can't be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if (content_title.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Content title can't be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (real.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Real content can't be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        final Note note = new Note();

        note.setTitle(title.getText().toString().trim());
        note.setDateTime(tvDate.getText().toString());
        note.setSubTitle(content_title.getText().toString().trim());
        note.setNoteText(real.getText().toString().trim());
        note.setColor(color);
        note.setImagePath(fileImage);
        if (!tvWebUrl.getText().toString().isEmpty()){
            note.setWebLink(tvWebUrl.getText().toString());
        }
        if (noteAvailable != null){
            note.setId(noteAvailable.getId());
        }

        class SaveNoteTask extends AsyncTask<Void,Void,Void>{

            @Override
            protected Void doInBackground(Void... voids) {
                NotesDatabase.getDatabase(getApplicationContext()).noteDAO().insertNote(note);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Intent intent = new Intent();
                setResult(RESULT_OK,intent);
                finish();
            }
        }
        new SaveNoteTask().execute();
    }

    private void initMescelleaneous(){
        LinearLayout mescell = findViewById(R.id.mescell);
        final BottomSheetBehavior<LinearLayout> bottemSheet = BottomSheetBehavior.from(mescell);
        mescell.findViewById(R.id.textMescell).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bottemSheet.getState() == BottomSheetBehavior.STATE_EXPANDED){
                    bottemSheet.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }else{
                    bottemSheet.setState(BottomSheetBehavior.STATE_EXPANDED );
                }
            }
        });

        mescell.findViewById(R.id.chooseImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(CreateNoteActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_EXTERNAL_STORAGE);
                }else{
                    selectImage();
                }
            }
        });

        mescell.findViewById(R.id.chooseURL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
                bottemSheet.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        if (noteAvailable != null && noteAvailable.getColor() != null){
            if (noteAvailable.getColor().equalsIgnoreCase("#333333")){
                pickColorTreeBlack();
                mescell.findViewById(R.id.imageColor1).performClick();
            }

            if (noteAvailable.getColor().equalsIgnoreCase("#FDDB3A")){
                pickColorTreeYellow();
                mescell.findViewById(R.id.imageColor2).performClick();
            }

            if (noteAvailable.getColor().equalsIgnoreCase("#EC0101")){
                pickColorTreeRed();
                boolean f = mescell.findViewById(R.id.imageColor3).callOnClick();
            }

            if (noteAvailable.getColor().equalsIgnoreCase("#07689F")){
                pickColorTreeBlue();
                mescell.findViewById(R.id.imageColor4).performClick();
            }

            if (noteAvailable.getColor().equalsIgnoreCase("#FFFFFF")){
                pickColorTreeWhite();
                mescell.findViewById(R.id.imageColor5).performClick();
            }
        }



    }

    private void selectImage(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if(intent.resolveActivity(getPackageManager()) != null){//check xem co app nao nhan intent da gui hay khong, neu ko co thi startActivity khong the work va dan den crash app
            startActivityForResult(intent,2);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_EXTERNAL_STORAGE){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                selectImage();
            }
            else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("vochua1");
        if (requestCode == 2 && resultCode == RESULT_OK){
            System.out.println("vochua2");
            if (data != null){
                System.out.println("vochua3");
                Uri uri = data.getData();
                if(uri != null){
                    System.out.println("vochua4");
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(uri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        imageNote.setImageBitmap(bitmap);
                        imageNote.setVisibility(View.VISIBLE);
                        fileImage = getPathFromUri(uri);
                        findViewById(R.id.imageRemoveWebImage).setVisibility(View.VISIBLE);

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void pickColorTreeBlack(){
        final ImageView imageView1 = findViewById(R.id.imageColor1);
        final ImageView imageView2 = findViewById(R.id.imageColor2);
        final ImageView imageView3 = findViewById(R.id.imageColor3);
        final ImageView imageView4 = findViewById(R.id.imageColor4);
        final ImageView imageView5 = findViewById(R.id.imageColor5);

        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView1.setImageResource(0);
                imageView2.setImageResource(0);
                imageView3.setImageResource(0);
                imageView4.setImageResource(0);
                imageView5.setImageResource(0);
                imageView1.setImageResource(R.drawable.ic_done);
                color = "#333333";
                changeColor(color);

            }
        });
    }
    private void pickColorTreeYellow(){

        final ImageView imageView1 = findViewById(R.id.imageColor1);
        final ImageView imageView2 = findViewById(R.id.imageColor2);
        final ImageView imageView3 = findViewById(R.id.imageColor3);
        final ImageView imageView4 = findViewById(R.id.imageColor4);
        final ImageView imageView5 = findViewById(R.id.imageColor5);
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView1.setImageResource(0);
                imageView2.setImageResource(0);
                imageView3.setImageResource(0);
                imageView4.setImageResource(0);
                imageView5.setImageResource(0);
                imageView2.setImageResource(R.drawable.ic_done);
                color = "#FDDB3A";
                changeColor(color);

            }
        });
    }
    private void pickColorTreeRed(){
        final ImageView imageView1 = findViewById(R.id.imageColor1);
        final ImageView imageView2 = findViewById(R.id.imageColor2);
        final ImageView imageView3 = findViewById(R.id.imageColor3);
        final ImageView imageView4 = findViewById(R.id.imageColor4);
        final ImageView imageView5 = findViewById(R.id.imageColor5);

        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView1.setImageResource(0);
                imageView2.setImageResource(0);
                imageView3.setImageResource(0);
                imageView4.setImageResource(0);
                imageView5.setImageResource(0);
                imageView3.setImageResource(R.drawable.ic_done);
                color = "#EC0101";
                changeColor(color);

            }
        });
    }
    private void pickColorTreeBlue(){
        final ImageView imageView1 = findViewById(R.id.imageColor1);
        final ImageView imageView2 = findViewById(R.id.imageColor2);
        final ImageView imageView3 = findViewById(R.id.imageColor3);
        final ImageView imageView4 = findViewById(R.id.imageColor4);
        final ImageView imageView5 = findViewById(R.id.imageColor5);

        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView1.setImageResource(0);
                imageView2.setImageResource(0);
                imageView3.setImageResource(0);
                imageView4.setImageResource(0);
                imageView5.setImageResource(0);
                imageView4.setImageResource(R.drawable.ic_done);
                color = "#07689F";
                changeColor(color);

            }
        });
    }
    private void pickColorTreeWhite(){
        final ImageView imageView1 = findViewById(R.id.imageColor1);
        final ImageView imageView2 = findViewById(R.id.imageColor2);
        final ImageView imageView3 = findViewById(R.id.imageColor3);
        final ImageView imageView4 = findViewById(R.id.imageColor4);
        final ImageView imageView5 = findViewById(R.id.imageColor5);
        imageView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView1.setImageResource(0);
                imageView2.setImageResource(0);
                imageView3.setImageResource(0);
                imageView4.setImageResource(0);
                imageView5.setImageResource(0);
                imageView5.setImageResource(R.drawable.ic_done);
                color = "#FFFFFF";
                changeColor(color);

            }
        });
    }



    private void changeColor(String cl){
        ImageView imageView = findViewById(R.id.cay);
        GradientDrawable gradientDrawable = (GradientDrawable) imageView.getBackground();
        gradientDrawable.setColor(Color.parseColor(cl));
    }


    private String getPathFromUri(Uri uri){
        String filePath = "";
        Cursor cursor = getContentResolver().query(uri,null,null,null,null);
        if (cursor == null)
            filePath = uri.getPath();
        else{
            cursor.moveToNext();
            int index = cursor.getColumnIndex("_data");
            filePath = cursor.getString(index);
            cursor.close();
        }
        return filePath;
    }



    private void showDialog(){
        View view = LayoutInflater.from(this).inflate(R.layout.layout_url,null);

        alertDialog = new AlertDialog.Builder(this).setView(view).show();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        final EditText link = view.findViewById(R.id.edtEnterUrl);
        view.findViewById(R.id.btnOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(link.getText().toString().trim().isEmpty()){
                    Toast.makeText(CreateNoteActivity.this, "URL not null", Toast.LENGTH_SHORT).show();
                }else if(!Patterns.WEB_URL.matcher(link.getText().toString()).matches()){
                    Toast.makeText(CreateNoteActivity.this, "URL not valid", Toast.LENGTH_SHORT).show();
                }else{
                    tvWebUrl.setText(link.getText().toString());
                    llPopup.setVisibility(View.VISIBLE);
                    alertDialog.dismiss();
                }
            }
        });
        view.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }


}