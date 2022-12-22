package com.example.m08actividad04;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.ViewTreeObserver;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    public Juego juego;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        juego = (Juego) findViewById(R.id.Pantalla);
        ViewTreeObserver obs = juego.getViewTreeObserver();

        obs.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Sólo se puede averiguar el ancho y alto una vez ya se ha pintado el layout. Por eso se calcula en este listener
                juego.basket.draw();
                juego.fruit.draw();
            }
        });

        //Ejecutamos cada 20 milisegundos
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        //Cada x segundos movemos la moneda 10dp
                        //juego.posMonedaY -= juego.speed;

                        //refreca la pantalla y llama al draw
                        juego.invalidate();
                    }
                });
            }
        }, 0, 20);
    }
}
