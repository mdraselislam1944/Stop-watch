package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView stopwatchTextView;
    private Button startButton, stopButton, resetButton;
    private boolean running;
    private int seconds = 0;
    private Handler handler = new Handler();
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stopwatchTextView = findViewById(R.id.stopwatchTextView);
        startButton = findViewById(R.id.startButton);
        stopButton = findViewById(R.id.stopButton);
        resetButton = findViewById(R.id.resetButton);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startStopwatch();
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopStopwatch();
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetStopwatch();
            }
        });

        // Restore the state if it was saved
        if (savedInstanceState != null) {
            running = savedInstanceState.getBoolean("running");
            if (running) {
                startStopwatch(); // Start the stopwatch if it was running
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the state of the timer
        outState.putBoolean("running", running);
        outState.putInt("seconds", seconds);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore the state of the timer
        running = savedInstanceState.getBoolean("running");
        if (running) {
            startStopwatch(); // Start the stopwatch if it was running
        }
        seconds = savedInstanceState.getInt("seconds");
        updateStopwatchText();
    }

    private void startStopwatch() {
        if (!running) {
            running = true;
            startButton.setEnabled(false);
            stopButton.setEnabled(true);

            runnable = new Runnable() {
                @Override
                public void run() {
                    seconds++;
                    updateStopwatchText();

                    if (running) {
                        handler.postDelayed(this, 1000); // Update every second
                    }
                }
            };

            handler.post(runnable);
        }
    }

    private void stopStopwatch() {
        running = false;
        startButton.setEnabled(true);
        stopButton.setEnabled(false);

        if (runnable != null) {
            handler.removeCallbacks(runnable);
        }
    }

    private void resetStopwatch() {
        running = false;
        seconds = 0;
        updateStopwatchText();
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
    }

    private void updateStopwatchText() {
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int secs = seconds % 60;

        String time = String.format("%02d:%02d:%02d", hours, minutes, secs);
        stopwatchTextView.setText(time);
    }
}
