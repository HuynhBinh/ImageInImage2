package com.hnb.imageinimage2;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

public class FinalView extends Activity 
{
	//Facebook facebook;
	
	ImageView img;
	GlobalVariable gvar;
	
	Bitmap backGround ;
	Bitmap huMan;
	
	byte[] byteImage;
	
	@Override
	protected void onDestroy() 
	{
		
		super.onDestroy();
		backGround.recycle();
		huMan.recycle();
	}
	
	@Override
	protected void onPause() 
	{
		
		
		/*facebook.authorize(FinalView.this, new String[]{"publish_stream"}, new DialogListener()
        {

			@Override
			public void onComplete(Bundle values) 
			{
				postPixToFacebook(values.getString(Facebook.TOKEN));
			}

			@Override
			public void onFacebookError(FacebookError e) 
			{
				
			}

			@Override
			public void onError(DialogError e) 
			{
			
			}

			@Override
			public void onCancel() 
			{
			}
		});*/
	
		super.onPause();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.finalview);
		
		//facebook  = new Facebook("175729095772478");
		
		img = (ImageView)findViewById(R.id.finalView);
		
		gvar = (GlobalVariable)getApplicationContext();
		

		
		backGround = gvar.backGroundBmp.copy(Bitmap.Config.ARGB_8888, true);
		huMan = gvar.humanBmp.copy(Bitmap.Config.ARGB_8888, true);
		                
		
        
        Canvas canvas = new Canvas(huMan);
        canvas.drawBitmap(backGround, 0, 0, new Paint());
        
        
        
        img.setImageBitmap(huMan);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        huMan.compress(CompressFormat.PNG, 100, baos); //bm is the bitmap object
        byteImage = baos.toByteArray();  
        
        try
        {
        	baos.flush();
        	baos.close();
        }
        catch(Exception ex)
        {
        	
        }
        
		Matrix matrix = new Matrix();
        matrix.postScale(2,2);
        
        Bitmap saveBmp = Bitmap.createBitmap(huMan, 0, 0, huMan.getWidth() , huMan.getHeight()  , matrix , true);
        
		//     
        long time = System.currentTimeMillis();
        
        StoreByteImage(getApplicationContext(), saveBmp, 100, "A2I_" + time);
        saveBmp.recycle();
        
        
	}
	
	// post picture to FB
    /*public void postPixToFacebook(String accessToken)
	{
		
		try
		{
			Bundle params = new Bundle();
	
			// neu post image thi` mo comment dong nay ra
	        params.putByteArray("picture", byteImage );
	        params.putString("description", "This picture is taken from ImageInImage App :)");
            params.putString("caption","My Picture ^^!");
	        
	        params.putString(Facebook.TOKEN, accessToken);
	        
	        String response = facebook.request("me/photos", params, "POST");


        } 
        catch (Exception e) 
        {
       
        } 
	}*/
    
    
    //post comment to FB
    /*public void updateStatus(String accessToken)
    {
    	try 
    	{
			Bundle bundle = new Bundle();
			bundle.putString("message", "Hello android!!");
			bundle.putString(Facebook.TOKEN,accessToken);
			String response = facebook.request("me/feed",bundle,"POST");
			Log.d("UPDATE RESPONSE",""+response);
		} 
    	catch (Exception e) 
		{
    		
		} 
    }*/
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) 
    {
        super.onActivityResult(requestCode, resultCode, data);
        //facebook.authorizeCallback(requestCode, resultCode, data);
    }
	
	
    public static Bitmap doColorFilter(Bitmap src, double red, double green, double blue) 
    {
        // image size
        int width = src.getWidth();
        int height = src.getHeight();
        // create output bitmap
        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
        // color information
        int A, R, G, B;
        int pixel;

        // scan through all pixels
        for(int x = 0; x < width; ++x) 
        {
            for(int y = 0; y < height; ++y) 
            {
                // get pixel color
                pixel = src.getPixel(x, y);
                // apply filtering on each channel R, G, B
                A = Color.alpha(pixel);
                R = (int)(Color.red(pixel) * red);
                G = (int)(Color.green(pixel) * green);
                B = (int)(Color.blue(pixel) * blue);
                // set new color pixel to output bitmap
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }

        // return final image
        return bmOut;
    }
    
	public static boolean StoreByteImage(Context mContext, Bitmap imageData, int quality, String expName) 
	{
        File sdImageMainDirectory = new File("/mnt/sdcard");
		FileOutputStream fileOutputStream = null;
		String nameFile = "/" + expName + ".jpg"; 
		
		try 
		{			
			Bitmap myImage = imageData;

			fileOutputStream = new FileOutputStream(sdImageMainDirectory.toString() + nameFile);
							
			BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);

			myImage.compress(CompressFormat.JPEG, quality, bos);

			bos.flush();
			bos.close();
		} 
		catch (Exception e)
		{
			
		}
		
		return true;
	}

}

