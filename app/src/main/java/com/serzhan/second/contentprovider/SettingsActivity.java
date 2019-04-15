package com.serzhan.second.contentprovider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {

    public static final int TEXT_SIZE_MINIMUM = 10;
    public static final int TEXT_SIZE_RANGE = 16;

    private Spinner mSpinner;
    private SeekBar mSeekBar;
    private TextView mTextSizeTextView;
    private SharedPrefHelper sharedPrefHelper;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initViews();
        sharedPrefHelper = new SharedPrefHelper(SettingsActivity.this);
        initSpinner();
        initSeekBar();
        mButton.setOnClickListener((v) -> {
            setResult(Activity.RESULT_OK);
            finish();
        });
    }

    private void initViews() {
        mSeekBar = findViewById(R.id.seekBar);
        mSpinner = findViewById(R.id.spinner);
        mTextSizeTextView = findViewById(R.id.size_text_view);
        mButton = findViewById(R.id.apply_button);
    }

    private void initSpinner() {
        String[] options = getResources().getStringArray(R.array.color_options);
        int[] values = getResources().getIntArray(R.array.color_values);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, options);
        mSpinner.setAdapter(adapter);
        mSpinner.setSelection(getCurrentSelection());
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sharedPrefHelper.setTextColor(values[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                sharedPrefHelper.setTextColor(SharedPrefHelper.DEFAULT_COLOR);
            }

        });
    }

    private int getCurrentSelection() {
        int currentColor = sharedPrefHelper.getTextColor();
        int[] colors = getResources().getIntArray(R.array.color_values);
        for(int i = 0; i < colors.length; i++) {
            if(colors[i] == currentColor) {
                return i;
            }
        }
        return 0;
    }

    private void initSeekBar() {
        mSeekBar.setMax(TEXT_SIZE_RANGE);
        int currentSize = sharedPrefHelper.getTextSize();
        mTextSizeTextView.setText(String.valueOf(currentSize));
        mSeekBar.setProgress(currentSize - TEXT_SIZE_MINIMUM);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mTextSizeTextView.setText(String.valueOf(TEXT_SIZE_MINIMUM + progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                sharedPrefHelper.setTextSize(seekBar.getProgress() + TEXT_SIZE_MINIMUM);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        setResult(Activity.RESULT_OK);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(Activity.RESULT_OK);
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, SettingsActivity.class);
    }
}
