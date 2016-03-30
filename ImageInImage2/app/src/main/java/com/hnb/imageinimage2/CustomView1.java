package com.hnb.imageinimage2;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposePathEffect;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.DiscretePathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.SumPathEffect;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class CustomView1 extends Activity
{
    private GlobalVariable gvar;

    private DrawView drawView;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        gvar = (GlobalVariable) getApplicationContext();

        Display display = ((WindowManager) this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        float DisplayWidth = display.getWidth();
        float DisplayHeight = display.getHeight();

        gvar.displayWidth = DisplayWidth;
        gvar.displayHeight = DisplayHeight;

        drawView = new DrawView(this, DisplayWidth, DisplayHeight);

        setContentView(drawView);

    }

    @Override
    protected void onPause()
    {
        gvar.backGroundBmp = drawView.resizedBmp;

        Intent i = new Intent(getApplicationContext(), CamaraView.class);
        startActivity(i);
        super.onPause();
    }


    class DrawView extends View implements View.OnTouchListener
    {
        private Context ctx;

        private Paint defaultPaint = new Paint();

        private Canvas cv = null;

        private Bitmap bm = null;

        private Bitmap resizedBmp;

        private Bitmap pointerBmp;

        private float screenWidth;
        private float screenHeight;

        private GlobalVariable gvar;

        private float x;
        private float y;

        private int pointerBmpWidth;
        private int pointerBmpHeight;


        public DrawView(Context context)
        {
            super(context);
            this.ctx = context;
            init();
        }

        public DrawView(Context context, AttributeSet attrs)
        {
            super(context, attrs);
            this.ctx = context;
            init();
        }

        public DrawView(Context context, float screenWidth, float screenHeight)
        {
            super(context);
            this.ctx = context;
            this.screenWidth = screenWidth;
            this.screenHeight = screenHeight;
            this.gvar = (GlobalVariable) getApplicationContext();
            init();
        }


        public void init()
        {
            setFocusable(true);
            setFocusableInTouchMode(true);
            setOnTouchListener(this);

            this.bm = this.gvar.backGroundBmp;

            int width = bm.getWidth();
            int height = bm.getHeight();

            float scaleX = this.screenWidth / width;
            float scaleY = this.screenHeight / height;

            Matrix matrix = new Matrix();
            matrix.postScale(scaleX, scaleY);

            this.resizedBmp = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
            this.bm.recycle();

            this.pointerBmp = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.icon);
            this.pointerBmpWidth = pointerBmp.getWidth();
            this.pointerBmpHeight = pointerBmp.getHeight();

            this.cv = new Canvas(resizedBmp);

        }

        @Override
        public void onDraw(Canvas canvas)
        {

            if (x != 0 && y != 0)
            {
                cv.drawBitmap(pointerBmp, x - (this.pointerBmpWidth/2), y - (this.pointerBmpHeight/2), defaultPaint);
            }

            canvas.drawBitmap(resizedBmp, 0, 0, defaultPaint);

            super.onDraw(canvas);
        }

        public boolean onTouch(View view, MotionEvent event)
        {
            float xPos = event.getX();
            float yPos = event.getY();

            switch (event.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    return true;
                // Draw a line to the ending position:
                case MotionEvent.ACTION_MOVE:
                    x = xPos;
                    y = yPos;
                    break;

                default:
                    return false;
            }

            // Call onDraw() to redraw the whole view:
            invalidate();
            return true;
        }
    }


}
