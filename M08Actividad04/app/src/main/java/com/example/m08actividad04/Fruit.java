package com.example.m08actividad04;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;

import java.util.Random;

public class Fruit extends GameObject {

    int speed;
    int points;
    public Bitmap image;

    public Fruit(int gameWidth, int gameHeight, Bitmap image, int speed, int points) {
        this.image = image;

        this.points = points;
        this.speed = speed;
        this.radius = gameWidth / 10;

        this.posY = gameHeight;
        this.posX = radius + new Random().nextInt(gameWidth - radius*2);
        this.rectangle = new RectF((posX-radius),(posY-radius),(posX+radius),(posY+radius));
    }
}
