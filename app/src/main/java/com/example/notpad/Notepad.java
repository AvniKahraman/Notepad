    package com.example.notpad;

    import androidx.activity.result.ActivityResultLauncher;
    import androidx.appcompat.app.AppCompatActivity;

    import android.content.Intent;
    import android.database.Cursor;
    import android.database.sqlite.SQLiteDatabase;
    import android.graphics.Bitmap;
    import android.graphics.BitmapFactory;
    import android.opengl.Visibility;
    import android.os.Bundle;
    import android.view.View;

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
            registerLauncher();
            database = this.openOrCreateDatabase("Note",MODE_PRIVATE,null);

            Intent intent =getIntent();
            String info =intent.getStringExtra("info");
            if(info.equals("new"))
            {
                binding.title.setText("");
                binding.type.setText("");
                binding.save.setVisibility(view.VISIBLE);


            }else {

                int noteId =intent.getIntExtra("noteId",0);
                binding.save.setVisibility(view.INVISIBLE);

                try {
                    Cursor cursor = database.rawQuery("SELECT * FROM  note WHERE id = ?",new String[]{String.valueOf(noteId)});
                    int notetitleIx = cursor.getColumnIndex("notetitle");
                    int notetypeIx   = cursor.getColumnIndex("notetype");

                    while (cursor.moveToNext()){
                        binding.title.setText(cursor.getString(notetitleIx));
                        binding.type.setText(cursor.getString(notetypeIx));



                    }


                }catch (Exception e)
                {
                e.printStackTrace();
                }

            }



        }
    }