package com.serzhan.second.contentprovider;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.serzhan.second.contentprovider.database.NoteDatabase;
import com.serzhan.second.contentprovider.entity.Note;
import com.serzhan.second.contentprovider.util.ConvertUtils;

import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddNoteActivity extends AppCompatActivity {

    private EditText mContentEditText;
    private Button mAddButton;
    private Button mCancelButton;
    private NoteDatabase db;

    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        initViews();
        initListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        executorService = Executors.newSingleThreadExecutor();
        db = NoteDatabase.getInstance(AddNoteActivity.this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        executorService.shutdown();
    }

    private void initViews() {
        mContentEditText = findViewById(R.id.activity_add_note_content_edit_text);
        mAddButton = findViewById(R.id.activity_note_add_button);
        mCancelButton = findViewById(R.id.activity_note_cancel_button);
    }

    private void initListeners() {
        mAddButton.setOnClickListener(view -> {
            addNote();
            finish();
        });
        mCancelButton.setOnClickListener(view -> {
            finish();
        });
    }

    private void addNote() {
        executorService.execute(new AddNoteTask(AddNoteActivity.this, getNote()));
    }

    private Note getNote() {
        Note note = new Note();
        note.setContent(mContentEditText.getText().toString());
        return note;
    }

    private static class AddNoteTask implements Runnable {

        private WeakReference<AddNoteActivity> activityReference;
        private Note note;

        public AddNoteTask(AddNoteActivity context, Note note) {
            activityReference = new WeakReference<>(context);
            this.note = note;
        }

        @Override
        public void run() {
            activityReference.get()
                    .getContentResolver()
                    .insert(
                            MainActivity.CONTENT_URI,
                            ConvertUtils.getNoteContentValues(note)
                    );
        }
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, AddNoteActivity.class);
    }
}
