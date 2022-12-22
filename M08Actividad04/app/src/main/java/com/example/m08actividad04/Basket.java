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
    Canvas canvas;

    public Basket(Juego juego, Canvas canvas) {
        this.juego = juego;
        this.canvas = canvas;
        this.image = BitmapFactory.decodeResource(juego.getResources(), R.drawable.basket);;

        this.radius = 100;
        this.posX = canvas.getWidth() / 2;
        this.posY = radius + 50;

        posX = canvas.getWidth() / 2;
        posY = radius + 50;
        rectangle = new RectF((posX-radius),(posY-radius),(posX+radius),(posY+radius));
    }

    public void move(MotionEvent event) {
        posX = (int)event.getX();
    }

    public void update() {
        rectangle = new RectF((posX-radius),(posY-radius),(posX+radius),(posY+radius));
        canvas.drawBitmap(image, null, rectangle, null);
    }
}
