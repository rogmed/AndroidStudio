package com.example.m08actividad04;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;

import java.util.Random;

public class Fruit extends GameObject {

    int speed;
    int points = 10;
    public Bitmap image;

    public Fruit(Canvas canvas, Bitmap image, int speed, int points) {
        this.image = image;

        this.points = points;
        this.speed = speed;
        this.radius = 100;

        this.posY = canvas.getHeight();
        this.posX = radius + new Random().nextInt(canvas.getWidth() - radius*2);
        this.rectangle = new RectF((posX-radius),(posY-radius),(posX+radius),(posY+radius));
    }

    /*
    public void update() {
        posY -= speed;
        rectangle.set((posX-radius),(posY-radius),(posX+radius),(posY+radius));
        canvas.drawBitmap(image, null, rectangle, null);
    }

     */
}
