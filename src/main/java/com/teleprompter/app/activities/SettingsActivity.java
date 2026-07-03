package com.teleprompter.app.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import com.teleprompter.app.R;

public class SettingsActivity extends AppCompatActivity {

    private Switch darkModeSwitch, screenOnSwitch;
    private SeekBar brightnessSlider, defaultSpeedSlider;
    private TextView themeLabel, brightnessLabel, speedLabel;
    private ImageButton backBtn;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        preferences = getSharedPreferences("teleprompter_prefs", MODE_PRIVATE);
        editor = preferences.edit();

        initializeViews();
        loadSettings();
        setupListeners();
    }

    private void initializeViews() {
        darkModeSwitch = findViewById(R.id.darkModeSwitch);
        screenOnSwitch = findViewById(R.id.screenOnSwitch);
        brightnessSlider = findViewById(R.id.brightnessSlider);
        defaultSpeedSlider = findViewById(R.id.defaultSpeedSlider);
        themeLabel = findViewById(R.id.themeLabel);
        brightnessLabel = findViewById(R.id.brightnessLabel);
        speedLabel = findViewById(R.id.speedLabel);
        backBtn = findViewById(R.id.backBtn);

        brightnessSlider.setMin(1);
        brightnessSlider.setMax(100);

        defaultSpeedSlider.setMin(1);
        defaultSpeedSlider.setMax(300);
    }

    private void loadSettings() {
        boolean isDarkMode = preferences.getBoolean("dark_mode", false);
        boolean isScreenOn = preferences.getBoolean("screen_on", false);
        int brightness = preferences.getInt("brightness", 100);
        int defaultSpeed = preferences.getInt("default_speed", 100);

        darkModeSwitch.setChecked(isDarkMode);
        screenOnSwitch.setChecked(isScreenOn);
        brightnessSlider.setProgress(brightness);
        defaultSpeedSlider.setProgress(defaultSpeed);

        updateLabels();
    }

    private void setupListeners() {
        darkModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editor.putBoolean("dark_mode", isChecked);
            editor.apply();
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

        screenOnSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editor.putBoolean("screen_on", isChecked);
            editor.apply();
        });

        brightnessSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    editor.putInt("brightness", progress);
                    editor.apply();
                    updateLabels();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        defaultSpeedSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    editor.putInt("default_speed", progress);
                    editor.apply();
                    updateLabels();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        backBtn.setOnClickListener(v -> onBackPressed());
    }

    private void updateLabels() {
        brightnessLabel.setText("Brightness: " + brightnessSlider.getProgress() + "%");
        float speed = defaultSpeedSlider.getProgress() / 100.0f;
        speedLabel.setText(String.format("Default Speed: %.1fx", speed));
    }
}
