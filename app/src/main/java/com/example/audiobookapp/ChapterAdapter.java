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
import com.example.audiobookapp.Chapter;
import java.util.List;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ChapterViewHolder> {

    private final List<Chapter> chapterList;
    private final Book currentBook;

    public ChapterAdapter(Book book) {
        this.currentBook = book;
        this.chapterList = book.getChapters();
    }

    @NonNull
    @Override
    public ChapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chapter, parent, false);
        return new ChapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterViewHolder holder, int position) {
        Chapter chapter = chapterList.get(position);
        int chapterNumber = position + 1;
        String formattedTitle = "Chapter " + chapterNumber + ": " + chapter.getTitle();
        holder.tvChapterTitle.setText(formattedTitle);

        holder.itemView.setOnClickListener(v -> {
            Context context = holder.itemView.getContext();
            Intent intent = new Intent(context, ChapterActivity.class);
            // Pass the entire book and the specific chapter
            intent.putExtra("book", currentBook);
            intent.putExtra(ChapterActivity.EXTRA_CHAPTER, chapter);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return chapterList.size();
    }

    public static class ChapterViewHolder extends RecyclerView.ViewHolder {
        TextView tvChapterTitle;

        public ChapterViewHolder(@NonNull View itemView) {
            super(itemView);
            tvChapterTitle = itemView.findViewById(R.id.tv_chapter_title);
        }
    }
}