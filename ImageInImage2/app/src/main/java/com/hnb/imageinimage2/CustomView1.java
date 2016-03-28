package com.hnb.imageinimage2;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
		

	
	float DisplayWidth = 0;
	float DisplayHeight = 0;
	
	float scaleX = 0;
	float scaleY = 0;
	
	GlobalVariable gvar;
	
	DrawView drawView;
 
	
	@Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        gvar = (GlobalVariable)getApplicationContext();
        
        Display display = ((WindowManager) this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay(); 
        DisplayWidth = display.getWidth();
      	DisplayHeight = display.getHeight();
      	
      	gvar.displayWidth = DisplayWidth;
      	gvar.displayHeight = DisplayHeight;

      	
        drawView = new DrawView(this);
        
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

        private Paint bmPaint = new Paint();
        private Paint drawPaint = new Paint();
        private Path path = new Path();

        private Canvas cv = null;
        private Bitmap bm = null;
        private boolean firstTimeThru = true;

        public Bitmap resizedBmp;


        public DrawView(Context context)
        {
            super(context);
            init();
        }

        public DrawView(Context context, AttributeSet attrs)
        {
            super(context, attrs);
            init();
        }


        public void init()
        {
            setFocusable(true);
            setFocusableInTouchMode(true);
            this.setOnTouchListener(this);

            bm = gvar.backGroundBmp;

            int width = bm.getWidth();
            int height = bm.getHeight();

            scaleX = DisplayWidth/width;
            scaleY = DisplayHeight/height;

            Matrix matrix = new Matrix();
            matrix.postScale(scaleX, scaleY);

            resizedBmp = Bitmap.createBitmap(bm, 0, 0, width , height , matrix , true);
            
        }

        @Override
        public void onDraw(Canvas canvas)
        {
            // Set everything up the first time anything gets drawn:
            if (firstTimeThru)
            {
                firstTimeThru = false;

                // Just quickly fill the view with a black mask:
                //canvas.drawColor(Color.BLACK);

                // Create a new bitmap and canvas and fill it with a black mask:
                //bm = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
                //cv = new Canvas();
                //cv.setBitmap(bm);
                //cv.drawColor(Color.WHITE);

                cv = new Canvas(resizedBmp);

                // Specify that painting will be with fat strokes:

                drawPaint.setPathEffect(new DiscretePathEffect(10f, 8f));
                drawPaint.setColor(Color.GREEN);
                drawPaint.setStyle(Paint.Style.FILL_AND_STROKE);
                drawPaint.setStrokeWidth(canvas.getWidth()/15);


                // Specify that painting will clear the pixels instead of paining new ones:
                //drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            }

            cv.drawPath(path, drawPaint);
            canvas.drawBitmap(resizedBmp, 0, 0, bmPaint);

            super.onDraw(canvas);
        }

        public boolean onTouch(View view, MotionEvent event)
        {
            float xPos = event.getX();
            float yPos = event.getY();

            switch (event.getAction())
            {

                // Set the starting position of a new line:
                case MotionEvent.ACTION_DOWN:
                    path.moveTo(xPos, yPos);
                    return true;

                // Draw a line to the ending position:
                case MotionEvent.ACTION_MOVE:
                    path.lineTo(xPos, yPos);
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
