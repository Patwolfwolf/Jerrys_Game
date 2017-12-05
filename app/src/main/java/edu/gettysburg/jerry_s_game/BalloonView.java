package edu.gettysburg.jerry_s_game;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class BalloonView extends View {
    private ArrayList<Rect> ranRect = new ArrayList<>();
    private int[] colors = new int[999999];

    private Handler handler = new Handler();
    private Timer timer = new Timer();

    boolean isGameOver = false;

    static final Paint whitePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    static final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public final Drawable[] balloonsDrawable = new Drawable[9];
    public final Bitmap[] bitmaps = new Bitmap[9];

    int balloonHeight, balloonWidth, totalLine = 0;

    Canvas canvas;
    Rect srcRect;
    Random rand = new Random();

    //black, purple, blue, green, yellow, orange, red
    public static final Integer[] imageResIds = new Integer[]{0, R.drawable.black,
            R.drawable.purple, R.drawable.blue, R.drawable.green, R.drawable.yellow,
            R.drawable.orange, R.drawable.red, R.drawable.death};

    Resources res = null;

    static {
        whitePaint.setColor(Color.WHITE);
        whitePaint.setStyle(Paint.Style.FILL);
    }

    public BalloonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        res = context.getResources();
        init();
    }

    private void init() {
        isGameOver = false;

        int width = 1080;
        int height = 1200;
        Log.i("width", "" + width);
        Log.i("height", "" + height);

        for (int i = 1; i < imageResIds.length; i++) {
            balloonsDrawable[i] = res.getDrawable(imageResIds[i]);
            bitmaps[i] = BitmapFactory.decodeResource(res, imageResIds[i]);
        }

        balloonHeight = (bitmaps[1].getHeight() - 1) / 3;
        balloonWidth = (bitmaps[1].getWidth() - 1) / 3;

        srcRect = new Rect(0, 0, bitmaps[1].getWidth() - 1, bitmaps[1].getHeight() - 1);
        generateNew(width, height);
    }

    private void generateNew(int width, int height) {
        System.out.println("456789132456789456123456");
        int total = 0;
        int limit = 2000;
        while (total < limit) {
            int per_line = rand.nextInt(4) + 2;
            if (total + per_line > limit) per_line = limit - total;
            int[] colorsBall = new int[per_line];
            int length = width - balloonWidth * per_line;
            for (int i = 0; i < per_line; i++) {
                int ranLeft = rand.nextInt(length);
                if (i > 0) colorsBall[i] = ranLeft + colorsBall[i - 1] + balloonWidth * i;
                else colorsBall[i] = ranLeft;
                length -= ranLeft;
            }
            for (int i = 0; i < per_line; i++) {
                int deviateHeight = balloonHeight * totalLine + rand.nextInt(balloonHeight * 2);
                Rect ran = new Rect(colorsBall[i], height + deviateHeight,
                        colorsBall[i] + balloonWidth, height + balloonHeight + deviateHeight);
                colors[total + i] = rand.nextInt(8) + 1;
                ranRect.add(ran);
            }
            total += per_line;
            totalLine++;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(0, 0, getWidth(), getHeight(), whitePaint);

        this.canvas = canvas;
        for (int i = 0; i < ranRect.size(); i++) {
            System.out.println(ranRect.get(i));
            this.canvas.drawBitmap(bitmaps[colors[i]], srcRect, ranRect.get(i), paint);
        }

        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        changePos();
                        invalidate();
                    }
                });
            }
        }, 5, Integer.MAX_VALUE);
    }

    public void changePos() {
        Log.i("changpos", "change");
        for (int i = 0; i < ranRect.size(); i++) {
            if (ranRect.get(i).bottom < 0)
                ranRect.get(i).set(ranRect.get(i).left, ranRect.get(i).top + totalLine * balloonHeight,
                        ranRect.get(i).right, ranRect.get(i).bottom + totalLine * balloonHeight);
            else
                ranRect.get(i).set(ranRect.get(i).left, ranRect.get(i).top - 5, ranRect.get(i).right, ranRect.get(i).bottom - 5);
        }
    }
}