package com.example.m08actividad04;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class Juego extends View {

    public Juego(Context context) {
        super(context);
    }
    public Juego(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public int score = 0;
    public int fails = 0;

    Bitmap fruits = BitmapFactory.decodeResource(getResources(), R.drawable.fruits);
    Fruit fruit = new Fruit(this, fruits);

    Bitmap basketImage = BitmapFactory.decodeResource(getResources(), R.drawable.basket);
    Basket basket = new Basket(this, basketImage);

    //Sección que capta los eventos del usuario
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // you may need the x/y location
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                basket.move(event);
        }
        return true;
    }
    public Juego(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //Definimos los objetos a pintar
        Paint fondo = new Paint();
        //Paint cesta = new Paint();

        //Paint moneda = new Paint();
        Paint posicionMoneda = new Paint();
        Paint posicionCesta = new Paint();
        Paint hit = new Paint();
        Paint lbScore = new Paint();
        Paint lbFails = new Paint();

        // Fondo
        fondo.setColor(Color.BLACK);
        fondo.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawRect(new Rect(0,0,(this.getWidth()),(this.getHeight())),fondo);

        // Update fruta y cesta
        basket.update(canvas);
        fruit.update(canvas);

        // Si colisiona fruta y cesta suma punta y resetea fruta
        if(checkCollision(basket, fruit)) {
            fruit.reset(canvas);
            score += 1;
        }

        // Texto: Posicion x, y de fruta
        posicionMoneda.setTextSize(50);
        posicionMoneda.setColor(Color.RED);
        canvas.drawText(fruit.posX + ", " + fruit.posY, 0, 100, posicionMoneda);


        // Texto: Posicion x, y de cesta
        posicionCesta.setTextSize(50);
        posicionCesta.setColor(Color.YELLOW);
        canvas.drawText(basket.posY + ", " + basket.posX, 0, 160, posicionCesta);

        // Texto: Flag de colisión
        hit.setTextSize(50);
        hit.setColor(Color.GREEN);
        canvas.drawText("HIT: " + checkCollision(basket, fruit), 0, 220, hit);

        // Texto: Score
        lbScore.setTextSize(50);
        lbScore.setColor(Color.BLUE);
        canvas.drawText("Puntos: " + score, this.getWidth() - 250, 100, lbScore);

        // Texto: Fails
        lbFails.setTextSize(50);
        lbFails.setColor(Color.MAGENTA);
        canvas.drawText("FAILS: " + fails, this.getWidth() - 250, 160, lbFails);
    }

    private boolean checkCollision(GameObject o1, GameObject o2) {
        double distance = Math.sqrt(Math.pow(o1.posX - o2.posX, 2) + Math.pow(o1.posY - o2.posY, 2));
        double centerToCenter = o1.radius + o2.radius;

        if(distance < centerToCenter) {
            return true;
        } else {
            return false;
        }
    }
}
