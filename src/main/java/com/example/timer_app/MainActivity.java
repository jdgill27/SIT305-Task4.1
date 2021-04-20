package com.example.timer_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    double time;
    Timer timer;
    TimerTask timerTask;
    TextView timer_view;
    TextView prev_view;
    EditText input_area;
    SharedPreferences shared;

    boolean timer_on;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timer_view = findViewById(R.id.timer_view);
        prev_view = findViewById(R.id.prev_view);
        input_area = findViewById(R.id.input_area);

        timer = new Timer();

        shared = getSharedPreferences("com.example.timer_app", MODE_PRIVATE);
        checkShared();

        if (savedInstanceState != null) {
            time = savedInstanceState.getDouble("time");
            timer_view.setText(getTimerText());
            startTimer();
        }
    }

    private void checkShared() {

        String input = shared.getString("input", "");
        String timer_text = shared.getString("timer", "");
        prev_view.setText("You spent " + timer_text + " time on " + input + " last time.");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putDouble("time", time);
    }

    public void stop_click(View view) {
        SharedPreferences.Editor editor = shared.edit();
        editor.putString("input", input_area.getText().toString());
        editor.putString("timer", getTimerText());
        editor.apply();

        if(timerTask != null)
        {
            timerTask.cancel();
            time = 0.0;
            timer_on = false;
            timer_view.setText(formatTime(0,0,0));

        }

    }

    public void play_click(View view) {
        timer_on = true;
        startTimer();
    }

    public void pause_click(View view) {
        timer_on = false;
        timerTask.cancel();

    }

    private void startTimer()
    {
        timerTask = new TimerTask()
        {
            @Override
            public void run()
            {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        time++;
                        timer_view.setText(getTimerText());
                    }
                });
            }

        };
        timer.scheduleAtFixedRate(timerTask, 0 ,1000);
    }


    private String getTimerText()
    {
        int rounded = (int) Math.round(time);

        int seconds = ((rounded % 86400) % 3600) % 60;
        int minutes = ((rounded % 86400) % 3600) / 60;
        int hours = ((rounded % 86400) / 3600);

        return formatTime(seconds, minutes, hours);
    }

    private String formatTime(int seconds, int minutes, int hours)
    {
        return String.format("%02d",hours) + ":" + String.format("%02d",minutes) + ":" + String.format("%02d",seconds);
    }
}