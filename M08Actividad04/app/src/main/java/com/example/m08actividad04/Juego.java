package com.example.m08actividad04;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.MediaPlayer;
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

    public MainActivity MA;

    private int score = 0;
    private int lifes = 3;
    private int passedFruits = 0;
    private boolean isGameOver;

    // Mensaje con la puntuacion que se muestra al coger una fruta
    private String message = "";
    private int messageX, messageY, sizeMessage, messageColor;

    Fruit fruit;
    // Velocidad inicial de las frutas
    private int speed;
    private int acceleration = 1;
    private Basket basket;

    private Bitmap bmAllFruits = BitmapFactory.decodeResource(getResources(), R.drawable.fruitstransparent);
    private Bitmap bmBasket = BitmapFactory.decodeResource(getResources(), R.drawable.basket);
    private Bitmap bmBackground = BitmapFactory.decodeResource(getResources(), R.drawable.beach_dunes);

    // Fondo y texto en pantalla
    Paint fondo = new Paint();
    Paint lbTotalScore = new Paint();
    Paint lbPlusScore = new Paint();
    Paint lbFails = new Paint();

    // Efectos de sonido
    private MediaPlayer mpSfx;

    // Musica
    private MediaPlayer mpMusic = MediaPlayer.create(getContext(), R.raw.game_music);

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
        this.basket = new Basket(this.getWidth(), bmBasket);
        this.speed = speed;
        mpMusic.start();

    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            // Fondo
        /*
            fondo.setColor(Color.BLACK);
            fondo.setStyle(Paint.Style.FILL_AND_STROKE);
            canvas.drawRect(new Rect(0,0,(this.getWidth()),(this.getHeight())),fondo);

         */
            canvas.drawBitmap(bmBackground, null, new Rect(0,0,(this.getWidth()),(this.getHeight())), null);

            // Update fruta y cesta
            basket.rectangle = new RectF((basket.posX-basket.radius),(basket.posY-basket.radius),(basket.posX+basket.radius),(basket.posY+basket.radius));
            canvas.drawBitmap(basket.image, null, basket.rectangle, null);

            // Si no hay fruta genera una
            if (fruit == null && !isGameOver) {
                fruit = createFruit(canvas);
            }

            // Si la fruta deja la pantalla reseteala y resta una vida (excepto si era un caramelo)
            if (fruit.posY < 0 && !isGameOver) {
                if (fruit.points > 0) {
                    playSound("fruit_out");
                    lifes--;
                }

                if (lifes == 0) {
                    gameOver(canvas);
                } else {
                    fruit = createFruit(canvas);
                }
            }

            // Si colisiona fruta y cesta => suma puntuacion, modifica mensaje y genera fruta nueva
            if(checkCollision(basket, fruit) && !isGameOver) {
                messageX = fruit.posX;
                messageY = fruit.posY;
                messageColor = (fruit.points > 0)? Color.GREEN : Color.RED;
                message = (fruit.points > 0)? "+" + fruit.points : fruit.points + "!!!";
                sizeMessage = 100;

                score += fruit.points;

                playSound((fruit.points>0)? "fruit_collision" : "candy_collision");

                fruit = createFruit(canvas);
            }

            // Actualiza la posicion de la fruta
            if (!isGameOver) {
                fruit.posY -= speed;
                fruit.rectangle.set((fruit.posX-fruit.radius),(fruit.posY-fruit.radius),(fruit.posX+fruit.radius),(fruit.posY+fruit.radius));
                canvas.drawBitmap(fruit.image, null, fruit.rectangle, null);
            }

            // Texto: Puntos sumados
            if(sizeMessage > 0) {
                sizeMessage--;
            }
            lbPlusScore.setTextSize(sizeMessage);
            lbPlusScore.setColor(messageColor);
            canvas.drawText(message, messageX, messageY, lbPlusScore);

            // Texto: Score
            lbTotalScore.setTextSize(50);
            lbTotalScore.setColor(Color.BLUE);
            canvas.drawText("Puntos: " + score, 10, 100, lbTotalScore);

            // Texto: Fails
            lbFails.setTextSize(50);
            lbFails.setColor(Color.MAGENTA);
            canvas.drawText("VIDAS: " + lifes, 10, 160, lbFails);
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

        Bitmap image;
        int score;
        int currentSpeed = speed;
        speed += acceleration;

        boolean isCandy = new Random().nextBoolean();

        if (isCandy) {
            image = BitmapFactory.decodeResource(getResources(), R.drawable.candy);
            score = -50;
        } else {
            int x = new Random().nextInt(7);
            int y = new Random().nextInt(4);
            image = randomizeImage(bmAllFruits,x, y);
            score = (1 + x + y) * 5;
        }

        passedFruits++;
        if(passedFruits % 10 == 0) {
            speed = 10;
            acceleration++;
        }

        return new Fruit(this.getWidth(), this.getHeight(), image, currentSpeed, score);
    }

    private void gameOver(Canvas canvas) {
        mpMusic.stop();
        playSound("gameover");
        isGameOver = true;
        speed = 0;
        MA.setContentView(R.layout.game_over);
        MA.setFinalScore(score);
    }

    private void playSound(String option) {

        switch (option) {
            case "fruit_collision":
                mpSfx = MediaPlayer.create(getContext(), R.raw.fruit_collision);
                break;
            case "fruit_out":
                mpSfx = MediaPlayer.create(getContext(), R.raw.fruit_out);
                break;
            case "candy_collision":
                mpSfx = MediaPlayer.create(getContext(), R.raw.candy_collision);
                break;
            case "gameover":
                mpSfx = MediaPlayer.create(getContext(), R.raw.gameover);
                break;
        }

        mpSfx.start();
    }

}
