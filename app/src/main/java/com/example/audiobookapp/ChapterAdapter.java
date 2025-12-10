package com.example.audiobookapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.audiobookapp.model.Book;
import com.example.audiobookapp.model.Chapter;

import java.io.Serializable;
import java.util.List;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ChapterViewHolder> {

    private Context context;
    private List<Chapter> chapterList;
    private Book book;

    public ChapterAdapter(Context context, List<Chapter> chapterList, Book book) {
        this.context = context;
        this.chapterList = chapterList;
        this.book = book;
    }

    @NonNull
    @Override
    public ChapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_chapter, parent, false);
        return new ChapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterViewHolder holder, int position) {
        Chapter chapter = chapterList.get(position);
        holder.chapterTitle.setText(chapter.getTitle());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ChapterActivity.class);
            intent.putExtra("book", book);
            // Pass the full list of chapters and the clicked position
            intent.putExtra("chapters", (Serializable) chapterList);
            intent.putExtra("position", position);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return chapterList.size();
    }

    static class ChapterViewHolder extends RecyclerView.ViewHolder {
        TextView chapterTitle;

        public ChapterViewHolder(@NonNull View itemView) {
            super(itemView);
            chapterTitle = itemView.findViewById(R.id.chapter_title);
        }
    }
}
