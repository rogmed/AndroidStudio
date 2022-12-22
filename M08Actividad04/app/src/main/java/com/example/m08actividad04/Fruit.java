package com.example.m08actividad04;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;

import java.util.Random;

public class Fruit extends GameObject {

    int speed;
    Bitmap allFruits;
    Bitmap singleFruit;

    public Fruit(Juego juego, Bitmap allFruits) {
        this.juego = juego;
        this.allFruits = allFruits;
        this.singleFruit = randomizeImage(allFruits, 7, 4);

        this.speed = 10;
        this.radius = 100;

        this.paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    public void draw() {
        this.posY = juego.getHeight();
        this.posX = new Random().nextInt(juego.getWidth());
        this.rectangle = new RectF((posX-radius),(posY-radius),(posX+radius),(posY+radius));
    }

    public void update(Canvas canvas) {

        if (posY > 0) {
            posY -= speed;

        } else {
            reset(canvas);
            juego.fails += 1;
        }

        rectangle.set((posX-radius),(posY-radius),(posX+radius),(posY+radius));
        //canvas.drawOval(rectangle, paint);
        canvas.drawBitmap(singleFruit, null, rectangle, null);

    }

    public void reset(Canvas canvas) {
        this.singleFruit = randomizeImage(allFruits,7, 4);
        posY = canvas.getHeight();
        posX = new Random().nextInt(canvas.getWidth());
        speed += 1;
    }

    private Bitmap randomizeImage(Bitmap image, int columns, int rows) {
        int x = new Random().nextInt(columns);
        int y = new Random().nextInt(rows);

        int width = image.getWidth()/columns;
        int height = image.getHeight()/rows;
        Bitmap tempImage = Bitmap.createBitmap(image, x* width, y* height, width, height);

        return tempImage;
    }
}
