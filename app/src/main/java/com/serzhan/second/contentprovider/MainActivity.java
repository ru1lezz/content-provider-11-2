package com.serzhan.second.contentprovider;

import android.app.Activity;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.serzhan.second.contentprovider.database.NoteDatabase;
import com.serzhan.second.contentprovider.entity.Note;
import com.serzhan.second.contentprovider.util.ConvertUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private static final int SET_DATA = 0;
    private static final int NOTE_SETTINGS = 1;
    private static final String AUTHORITY = "com.serzhan.practice.contentprovider.content_provider.MyContentProvider";
    private static final String NOTES_TABLE = "notes";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + NOTES_TABLE);

    private FloatingActionButton mAddNoteButton;
    private RecyclerView mRecyclerView;
    private NoteAdapter mAdapter;
    private ExecutorService executorService;
    private ContentObserver mContentObserver;
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SET_DATA: {
                    mAdapter.setNotes((ArrayList<Note>) msg.obj);
                }
                default:
                    super.handleMessage(msg);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initListeners();
        initRecyclerView();
        executorService = Executors.newSingleThreadExecutor();
        initContentObserver();
    }

    private void initViews() {
        mAddNoteButton = findViewById(R.id.add_note_floating_action_button);
        mRecyclerView = findViewById(R.id.recyclerView);
    }

    private void initListeners() {
        mAddNoteButton.setOnClickListener((view) -> {
            startActivity(AddNoteActivity.newIntent(MainActivity.this));
        });
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false));
        mRecyclerView.addItemDecoration(new NoteDecoration(MainActivity.this));
        mAdapter = new NoteAdapter(MainActivity.this);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initContentObserver() {
        mContentObserver = new NoteContentObserver(new Handler(Looper.getMainLooper()), this::getNotes);
    }

    private void getNotes() {
        executorService.execute(() -> {
            Cursor cursor = getContentResolver().query(
                    CONTENT_URI,
                    new String[] {
                            "id",
                            "content"
                    },
                    null,
                    null,
                    null
            );
            Message message = mHandler.obtainMessage(SET_DATA, ConvertUtils.parseCursorToNotes(cursor));
            message.sendToTarget();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getContentResolver().registerContentObserver(CONTENT_URI, true, mContentObserver);
        getNotes();
    }

    @Override
    protected void onPause() {
        super.onPause();
        getContentResolver().unregisterContentObserver(mContentObserver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.activity_settings_menu_settings: {
                startActivityForResult(SettingsActivity.newIntent(MainActivity.this), NOTE_SETTINGS);
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case NOTE_SETTINGS:
                if (resultCode == Activity.RESULT_OK) {
                    mAdapter.updateUi();
                }
        }
    }
}
