package com.hnb.imageinimage2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class CustomCameraView extends View 
{
		
	Bitmap bmOut;
	Bitmap bmOut2;
	GlobalVariable gvar;

	public CustomCameraView(Context context) 
	{
		super(context);
		gvar = (GlobalVariable)context.getApplicationContext();
		init();
	}
	
	public CustomCameraView(Context context, AttributeSet att)
	{
		super(context, att);
		gvar = (GlobalVariable)context.getApplicationContext();
		init();
	}
	
	public void init()
	{		
		Bitmap bmp = gvar.backGroundBmp;
		
		int width = bmp.getWidth();
        int height = bmp.getHeight();
              
        bmOut = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        
        for(int x = 0; x < width; ++x) 
        {
            for(int y = 0; y < height; ++y) 
            {
            	//int alpha = (int)(Color.alpha(bmp.getPixel(x, y)));
            	//int color = pngTestBM.getPixel(myX, myY);
            	//boolean transparent = (color & 0xff000000) == 0x0;          	
                if (bmp.getPixel(x, y) == Color.GREEN) /*Color.alpha(pixel) == 100*/
                {
					bmOut.setPixel(x, y, Color.TRANSPARENT);
                    
                }
                else
                {
                	bmOut.setPixel(x, y, bmp.getPixel(x, y));
                }


            }
        }

	}
	
	
	@Override
	protected void onDraw(Canvas canvas) 
	{
		canvas.drawBitmap(bmOut,0,0, new Paint());
		super.onDraw(canvas);
	}

}
