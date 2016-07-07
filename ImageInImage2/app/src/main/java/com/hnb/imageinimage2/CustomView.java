package com.hnb.imageinimage2;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class CustomView extends Activity
{
		
	private Paint mPaint;
	
	float DisplayWidth = 0;
	float DisplayHeight = 0;
	
	float scaleX = 0;
	float scaleY = 0;
	
	GlobalVariable gvar;
	
	MyView myView;
 
	
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
      	
      	mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.GREEN);
        
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.MITER);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(55);
      	
        myView = new MyView(this);
        
        setContentView(myView);
             
    }
	
	@Override
	protected void onPause() 
	{	
		gvar.backGroundBmp = myView.resizedBmp;
		
		Intent i = new Intent(getApplicationContext(), CamaraView.class);
		startActivity(i);
		super.onPause();	
	}
	
	
    
    
    public class MyView extends View 
    {
             
        private Bitmap  mBitmap;
        private Canvas  mCanvas;
        private Path    mPath;
        
        public Bitmap resizedBmp;
        
        public MyView(Context c) 
        {
            super(c);
                        
            mBitmap = gvar.backGroundBmp;
            
            int width = mBitmap.getWidth();
            int height = mBitmap.getHeight();
            
            scaleX = DisplayWidth/width;
            scaleY = DisplayHeight/height;
            
            Matrix matrix = new Matrix();
            matrix.postScale(scaleX, scaleY);
            
            resizedBmp = Bitmap.createBitmap(mBitmap, 0, 0, width , height , matrix , true);
               
            mCanvas = new Canvas(resizedBmp);
            mPath = new Path();
            
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) 
        {
            super.onSizeChanged(w, h, oldw, oldh);
        }
        
        @Override
        protected void onDraw(Canvas canvas) 
        {
                      
            canvas.drawBitmap(resizedBmp, 0, 0, new Paint());
            
            canvas.drawPath(mPath, mPaint);
            
        }
        
        private float mX, mY;
        private static final float TOUCH_TOLERANCE = 4;
        
        private void touch_start(float x, float y) 
        {
            mPath.reset();
            mPath.moveTo(x, y);
            mX = x;
            mY = y;
        }
        private void touch_move(float x, float y) 
        {
            float dx = Math.abs(x - mX);
            float dy = Math.abs(y - mY);
            if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) 
            {
                mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
                mX = x;
                mY = y;
            }
        }
        private void touch_up() 
        {
            mPath.lineTo(mX, mY);
            // commit the path to our offscreen
            mCanvas.drawPath(mPath, mPaint);
            // kill this so we don't double draw
            mPath.reset();
        }
        
        @Override
        public boolean onTouchEvent(MotionEvent event) 
        {
            float x = event.getX();
            float y = event.getY();
            
            switch (event.getAction()) 
            {
                case MotionEvent.ACTION_DOWN:
                    touch_start(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    touch_move(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    touch_up();
                    invalidate();
                    break;
            }
            return true;
        }
             
    }
}
