package com.example.notpad;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notpad.databinding.ActivityRecyclerowBinding;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteHolder> {
    private ArrayList<note> noteArrayList;

    public NoteAdapter(ArrayList<note> noteArrayList) {
        this.noteArrayList = noteArrayList;
    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ActivityRecyclerowBinding binding = ActivityRecyclerowBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new NoteHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        holder.binding.avni.setText(noteArrayList.get(position).title);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int clickedPosition = holder.getAdapterPosition();
                if (clickedPosition != RecyclerView.NO_POSITION) {
                    Intent intent = new Intent(holder.itemView.getContext(), Notepad.class);
                    intent.putExtra("info", "old");
                    intent.putExtra("noteId", noteArrayList.get(clickedPosition).id);
                    holder.itemView.getContext().startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return noteArrayList.size();
    }

    public class NoteHolder extends RecyclerView.ViewHolder {
        ActivityRecyclerowBinding binding;

        public NoteHolder(ActivityRecyclerowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
