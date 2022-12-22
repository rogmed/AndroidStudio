package com.example.m08actividad04;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.MotionEvent;

public class Basket extends GameObject {

    Bitmap image;

    public Basket(Juego juego, Bitmap image) {
        this.juego = juego;
        this.image = image;;

        this.radius = 100;
        this.posX = juego.getWidth() / 2;
        this.posY = radius + 50;

        posX = juego.getWidth() / 2;
        posY = radius + 50;
        rectangle = new RectF((posX-radius),(posY-radius),(posX+radius),(posY+radius));
    }

    public void move(MotionEvent event) {
        posX = (int)event.getX();
    }
}
