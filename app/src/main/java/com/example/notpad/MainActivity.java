package com.example.notpad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.notpad.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    NoteAdapter noteAdapter;
    ArrayList<note> noteArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        FloatingActionButton fab = findViewById(R.id.fab);

        noteArrayList = new ArrayList<>();

        binding.recylerview.setLayoutManager(new LinearLayoutManager(this));
        noteAdapter = new NoteAdapter(noteArrayList);
        binding.recylerview.setAdapter(noteAdapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Notepad.class);
                intent.putExtra("info", "new");
                startActivity(intent);
                getData();
            }
        });

        getData();
    }

    private void getData() {
        try {
            SQLiteDatabase database = this.openOrCreateDatabase("Note", MODE_PRIVATE, null);
            Log.d("Database", "Database created successfully");


            Cursor cursor = database.rawQuery("SELECT * FROM note", null);
            int titleIx = cursor.getColumnIndex("title");
            int idIx = cursor.getColumnIndex("id");

            while (cursor.moveToNext()) {
                String title = cursor.getString(titleIx);
                int id = cursor.getInt(idIx);
                note note = new note(title, id);
                noteArrayList.add(note);
            }
            noteAdapter.notifyDataSetChanged();
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
