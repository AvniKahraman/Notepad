package com.example.notpad;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.notpad.databinding.ActivityNotepadBinding;

public class Notepad extends AppCompatActivity {

    private ActivityNotepadBinding binding;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotepadBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        database = this.openOrCreateDatabase("Note", MODE_PRIVATE, null);

        Button saveButton = findViewById(R.id.save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });

        Intent intent = getIntent();
        String info = intent.getStringExtra("info");
        if (info != null && info.equals("new")) {
            binding.title.setText("");
            binding.type.setText("");
            binding.save.setVisibility(View.VISIBLE);
        } else {
            int noteId = intent.getIntExtra("noteId", 0);
            loadNoteData(noteId);
        }

        binding.title.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showDeleteConfirmationDialog();
                return true;
            }
        });
    }

    private void loadNoteData(int noteId) {
        try {
            Cursor cursor = database.rawQuery("SELECT * FROM note WHERE id = ?", new String[]{String.valueOf(noteId)});
            int notetitleIx = cursor.getColumnIndex("title");
            int notetypeIx = cursor.getColumnIndex("type");

            if (cursor.moveToFirst()) {
                binding.title.setText(cursor.getString(notetitleIx));
                binding.type.setText(cursor.getString(notetypeIx));
            }

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Note");
        builder.setMessage("Are you sure you want to delete this note?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteNote();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void deleteNote() {
        int noteId = getIntent().getIntExtra("noteId", 0);
        if (noteId > 0) {
            try {
                database.execSQL("DELETE FROM note WHERE id = ?", new Object[]{noteId});
                Intent intent = new Intent(Notepad.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void save() {
        String title = binding.title.getText().toString();
        String type = binding.type.getText().toString();

        try {
            database.execSQL("CREATE TABLE IF NOT EXISTS note (id INTEGER PRIMARY KEY ,title VARCHAR,type VARCHAR)");

            String sqlSting = "INSERT INTO note(title,type) VALUES(?,?)";
            SQLiteStatement sqLiteStatement = database.compileStatement(sqlSting);
            sqLiteStatement.bindString(1, title);
            sqLiteStatement.bindString(2, type);
            sqLiteStatement.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(Notepad.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
