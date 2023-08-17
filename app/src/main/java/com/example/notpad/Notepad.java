    package com.example.notpad;

    import androidx.activity.result.ActivityResultLauncher;
    import androidx.appcompat.app.AppCompatActivity;

    import android.content.Intent;
    import android.database.Cursor;
    import android.database.sqlite.SQLiteDatabase;
    import android.database.sqlite.SQLiteStatement;
    import android.graphics.Bitmap;
    import android.graphics.BitmapFactory;
    import android.opengl.Visibility;
    import android.os.Bundle;
    import android.view.View;
    import android.widget.Button;

    import com.example.notpad.databinding.ActivityNotepadBinding;

    public class Notepad extends AppCompatActivity {

        private ActivityNotepadBinding binding;
        ActivityResultLauncher<Intent> activityResultLauncher ;
        ActivityResultLauncher<String> permissionLauncher;
        Bitmap selectedImage;
        SQLiteDatabase database;




        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_notepad);

            binding =ActivityNotepadBinding.inflate(getLayoutInflater());
            View view =binding.getRoot();
            setContentView(view);
            database = this.openOrCreateDatabase("Note",MODE_PRIVATE,null);
            Button saveButton = findViewById(R.id.save);
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    save(v);
                }
            });

            Intent intent =getIntent();
            String info =intent.getStringExtra("info");
            if(info.equals("new"))
            {
                binding.title.setText("");
                binding.type.setText("");
                binding.save.setVisibility(view.VISIBLE);


            }else {

                int noteId =intent.getIntExtra("noteId",0);

                try {
                    Cursor cursor = database.rawQuery("SELECT * FROM  note WHERE id = ?",new String[]{String.valueOf(noteId)});
                    int notetitleIx = cursor.getColumnIndex("title");
                    int notetypeIx   = cursor.getColumnIndex("type");

                    while (cursor.moveToNext()){
                        binding.title.setText(cursor.getString(notetitleIx));
                        binding.type.setText(cursor.getString(notetypeIx));



                    }

                    cursor.close();

                }catch (Exception e)
                {
                e.printStackTrace();
                }

            }



        }


    public void save(View view){
    //Kullanıcının yazdığı verileri String formatına çevirme.

    String title = binding.title.getText().toString();
    String type =   binding.type.getText().toString();



    try {
        database.execSQL("CREATE TABLE IF NOT EXISTS note (id INTEGER PRIMARY KEY ,title VARCHAR,type VARCHAR)");

        String sqlSting= "INSERT INTO note(title,type) VALUES(?,?)";
        SQLiteStatement sqLiteStatement = database.compileStatement(sqlSting);
        sqLiteStatement.bindString(1,title);
        sqLiteStatement.bindString(2,type);
        sqLiteStatement.execute();


    }catch (Exception e )
    {
        e.printStackTrace();
    }
        Intent intent = new Intent(Notepad.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);


    }





    }