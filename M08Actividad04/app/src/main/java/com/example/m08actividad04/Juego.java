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

import java.util.Random;

public class Juego extends View {

    public Juego(Context context) {
        super(context);
    }
    public Juego(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public int score = 0;
    public int fails = 0;
    String message = "";
    int messageX, messageY, sizeMessage;

    Fruit fruit;
    private int speed;
    Basket basket;

    Bitmap allFruits = BitmapFactory.decodeResource(getResources(), R.drawable.fruitstransparent);

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

    public void setup(int speed) {
        this.speed = speed;
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            //Definimos los objetos a pintar
            Paint fondo = new Paint();
            Paint lbTotalScore = new Paint();
            Paint lbPlusScore = new Paint();
            Paint lbFails = new Paint();

            Paint fruitPosition = new Paint();

            // Fondo
            fondo.setColor(Color.BLACK);
            fondo.setStyle(Paint.Style.FILL_AND_STROKE);
            canvas.drawRect(new Rect(0,0,(this.getWidth()),(this.getHeight())),fondo);

            // Update fruta y cesta
            if (basket == null) {
                basket = new Basket(this, canvas);
            } else {
                basket.update();
            }

            boolean fruitIsOut = fruit != null && fruit.posY < 0;
            // Si no hay fruta genera una, si hay fruta actualiza su posicion
            if (fruit == null || fruitIsOut) {
                fruit = createFruit(canvas);
                if(fruitIsOut) { fails++; }
            } else {

                fruit.posY -= speed;
                fruit.rectangle.set((fruit.posX-fruit.radius),(fruit.posY-fruit.radius),(fruit.posX+fruit.radius),(fruit.posY+fruit.radius));
                canvas.drawBitmap(fruit.image, null, fruit.rectangle, null);
            }

            // Si colisiona fruta y cesta => suma puntuacion y genera fruta nueva
            if(checkCollision(basket, fruit)) {
                messageX = fruit.posX;
                messageY = fruit.posY;
                message = "+" + fruit.points;
                sizeMessage = 100;

                score += fruit.points;

                fruit = createFruit(canvas);
            }

            // Texto: Puntos sumados
            if(sizeMessage > 0) {
                sizeMessage--;
            }
            lbPlusScore.setTextSize(sizeMessage);
            lbPlusScore.setColor(Color.BLUE);
            canvas.drawText(message, messageX, messageY, lbPlusScore);

            // Texto: Score
            lbTotalScore.setTextSize(50);
            lbTotalScore.setColor(Color.BLUE);
            canvas.drawText("Puntos: " + score, 10, 100, lbTotalScore);

            // Texto: Fails
            lbFails.setTextSize(50);
            lbFails.setColor(Color.MAGENTA);
            canvas.drawText("FAILS: " + fails, 10, 160, lbFails);

            // Texto: Fruit position
            fruitPosition.setTextSize(50);
            fruitPosition.setColor(Color.RED);
            if (fruit != null) {
                canvas.drawText("Fruit: " + fruit.posX + ", " + fruit.posY, 10, 220, fruitPosition);
            }
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

    private Bitmap randomizeImage(Bitmap image, int x, int y) {
        int width = image.getWidth()/7;
        int height = image.getHeight()/4;
        Bitmap tempImage = Bitmap.createBitmap(image, x* width, y* height, width, height);

        return tempImage;
    }

    private Fruit createFruit(Canvas canvas) {
        int x = new Random().nextInt(7);
        int y = new Random().nextInt(4);
        int fruitScore = (1 + x + y) * 5;
        Bitmap image = randomizeImage(allFruits,x, y);

        return new Fruit(canvas, image, speed, fruitScore);
    }
}
