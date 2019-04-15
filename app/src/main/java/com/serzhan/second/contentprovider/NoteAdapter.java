package com.serzhan.second.contentprovider;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.serzhan.second.contentprovider.entity.Note;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteHolder> {

    private final Context mContext;
    private List<Note> mNotes;
    private SharedPrefHelper sharedPrefHelper;
    private int textColor;
    private int textSize;

    public NoteAdapter(Context mContext) {
        this.mContext = mContext;
        sharedPrefHelper = new SharedPrefHelper(mContext);
        mNotes = new ArrayList<>();
        updateUi();
    }

    public void updateUi() {
        textColor = sharedPrefHelper.getTextColor();
        textSize = sharedPrefHelper.getTextSize();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.note_item, parent, false);

        return new NoteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        holder.bind(mNotes.get(position), textColor, textSize);
    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }

    void setNotes(List<Note> notes) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new NoteDiffCallback(mNotes, notes));
        diffResult.dispatchUpdatesTo(this);
        mNotes.clear();
        mNotes.addAll(notes);
    }

    public static class NoteHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private Note mNote;
        private TextView mDateTextView;
        private TextView mContentTextView;

        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            initViews(itemView);
            itemView.setOnClickListener(this);
        }

        private void initViews(View itemView) {
            mDateTextView = itemView.findViewById(R.id.note_item_date);
            mContentTextView = itemView.findViewById(R.id.note_item_content);
        }

        public void bind(Note note, int textColor, int textSize) {
            mNote = note;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = simpleDateFormat.format(note.getCreationDate());
            mDateTextView.setText(dateString);
            mDateTextView.setTextColor(textColor);
            mContentTextView.setText(note.getContent());
            mContentTextView.setTextColor(textColor);
            mContentTextView.setTextSize(textSize);
        }

        @Override
        public void onClick(View v) {
            Intent intent = NoteActivity.newIntent(v.getContext(), mNote.getId());
            v.getContext().startActivity(intent);
        }
    }
}
