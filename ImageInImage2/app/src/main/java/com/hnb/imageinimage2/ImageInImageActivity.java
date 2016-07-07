package com.hnb.imageinimage2;

import java.io.File;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class ImageInImageActivity extends Activity 
{
	
	GlobalVariable Gvar;
    
	protected String path = Environment.getExternalStorageDirectory() + "/capturepic.jpg";
	protected boolean taken;
	protected static final String PHOTO_TAKEN = "photo_taken";
	
	String selectedImagePath = "";
	
	Button btnCapture;
	Button btnGuide;
	Button btnGallery;
	
	private void initControl()
	{
		btnCapture = (Button)findViewById(R.id.btnCapture);
		
		btnGuide = (Button)findViewById(R.id.btnGuide);
		
		btnGallery = (Button)findViewById(R.id.btnGallery);
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        initControl();
        
        Gvar = (GlobalVariable)getApplicationContext();
        
        btnGuide.setOnClickListener(new OnClickListener() 
        {
			
			@Override
			public void onClick(View v) 
			{
				Intent intent = new Intent(getApplicationContext(), GuideActivity.class);
				startActivity(intent);
				
			}
		});
        
        
        btnCapture.setOnClickListener(new OnClickListener() 
        {
			
			@Override
			public void onClick(View v) 
			{
				 startCameraActivity();
			}
		});
        
        
        btnGallery.setOnClickListener(new OnClickListener() 
        {
			
			@Override
			public void onClick(View v) 
			{
				// To open up a gallery browser
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(Intent.createChooser(intent, "Select Picture"),1);
				
			}
		});
                         
    }
    
    
    
    protected void startCameraActivity()
    {
        File file = new File(path);
        Uri outputFileUri = Uri.fromFile(file);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE );
        intent.putExtra( MediaStore.EXTRA_OUTPUT, outputFileUri );

        startActivityForResult( intent, 0 );
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
    	if(requestCode == 1)
    	{
    		if(resultCode == -1)
    		{
    			BitmapFactory.Options options = new BitmapFactory.Options();
    	        options.inSampleSize = 2;
    	        
    			 Uri selectedImageUri = data.getData();
    			 selectedImagePath = getPath(selectedImageUri);
    			 
    			 Log.i("AAAAAAAAAAAAAAAAAAA", selectedImagePath);
    			 
    			 Gvar.backGroundBmp = BitmapFactory.decodeFile( selectedImagePath, options );
                 
    		        //Gvar.backGroundBmp = bitmap;
    		        
    		        //bitmap.recycle();
    		            
    		        Intent intent = new Intent(getApplicationContext(), CustomView.class);
    		        startActivity(intent);
    		}
    	}
    	else
    	{
	        Log.i( "MakeMachine", "resultCode: " + resultCode );
	        switch( resultCode )
	        {
	        	case 0:
	        		Log.i( "MakeMachine", "User cancelled" );
	        		break;
	
	        	case -1:
	        		onPhotoTaken();
	        		break;
	        }
    	}
    }
    
    public String getPath(Uri uri) 
    {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    
    protected void onPhotoTaken()
    {
        taken = true;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        
        Gvar.backGroundBmp = BitmapFactory.decodeFile( path, options );
        //int w = Gvar.backGroundBmp.getWidth();
        //int h = Gvar.backGroundBmp.getHeight();
        
        //Log.i("WWWWWHHHHH", w + "-" + h);
             
        //Log.i("BBBBBBBBBBBB", path);
        //Gvar.backGroundBmp = bitmap;
        
            
        Intent intent = new Intent(getApplicationContext(), CustomView.class);
        startActivity(intent);
        
    }
    
    @Override
    protected void onSaveInstanceState( Bundle outState ) 
    {
        outState.putBoolean( ImageInImageActivity.PHOTO_TAKEN, taken );
    }
    
    @Override
    protected void onRestoreInstanceState( Bundle savedInstanceState)
    {
        Log.i( "MakeMachine", "onRestoreInstanceState()");
        if( savedInstanceState.getBoolean( ImageInImageActivity.PHOTO_TAKEN ) ) 
        {
        	onPhotoTaken();
        }
    }
}