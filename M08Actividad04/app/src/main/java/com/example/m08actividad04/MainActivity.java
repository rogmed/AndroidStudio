package com.example.m08actividad04;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    public Juego juego;
    private Handler handler = new Handler();
    private Button btStartGame;
    private Spinner spDifficulty;

    ArrayList<Difficulty> difficultyOptions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        difficultyOptions.add(new Difficulty("Fácil (Velocidad x0.5)", 5));
        difficultyOptions.add(new Difficulty("Normal", 10));
        difficultyOptions.add(new Difficulty("Difícil (Velocidad x1.5)", 15));

        setupActivityMain();
    }

    public void setFinalScore(int score) {
        TextView finalScore = (TextView) findViewById(R.id.scoreText);
        String textScore = finalScore.getText() + String.valueOf(score);
        finalScore.setText(textScore);
    }

    private void setupActivityMain()
    {
        setContentView(R.layout.activity_main);

        btStartGame = (Button) findViewById(R.id.btStartGame);

        spDifficulty = (Spinner) findViewById(R.id.spDifficulty);
        ArrayAdapter<Difficulty> adapter = new ArrayAdapter<>(MainActivity.this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, difficultyOptions);
        spDifficulty.setAdapter(adapter);
        spDifficulty.setSelection(1);

    }

    public void onClickExit(View view) {
        setupActivityMain();
    }

    public void onClickBtStartGame(View view) {
        Difficulty selection = (Difficulty) spDifficulty.getSelectedItem();
        int speed = selection.getSpeed();

        startGame(speed);
    }

    private void startGame(int speed) {
        setContentView(R.layout.game_screen);

        juego = (Juego) findViewById(R.id.GameScreen);
        juego.MA = this;
        ViewTreeObserver obs = juego.getViewTreeObserver();

        obs.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Sólo se pueden averiguar las dimensiones del juego una vez se ha pintado.
                // Por eso se inician los elementos en el listener
                juego.setup(speed);
            }
        });

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        juego.invalidate();
                    }
                });
            }
        }, 0, 20);
    }
}

class Difficulty {
    public String text;
    public int speed;

    public Difficulty(String text, int speed) {
        this.text = text;
        this.speed = speed;
    }

    public int getSpeed() {
        return speed;
    }

    @NonNull
    @Override
    public String toString() {
        return text;
    }
}
