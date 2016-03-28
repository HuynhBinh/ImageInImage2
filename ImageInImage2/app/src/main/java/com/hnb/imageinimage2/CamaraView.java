package com.hnb.imageinimage2;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;

public class CamaraView extends Activity implements SurfaceHolder.Callback, OnClickListener
{

    static final int FOTO_MODE = 0;

    Camera mCamera;

    boolean mPreviewRunning = false;

    private Context mContext;// = this;

    private SurfaceView mSurfaceView;

    private SurfaceHolder mSurfaceHolder;

    CustomCameraView cView;

    GlobalVariable gvar;

    public void onCreate(Bundle icicle)
    {

        super.onCreate(icicle);

        mContext = getApplicationContext();

        getWindow().setFormat(PixelFormat.TRANSLUCENT);

        // remove title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // remove notify bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // layout
        setContentView(R.layout.cameraview);

        cView = (CustomCameraView) findViewById(R.id.customCamview);

        mSurfaceView = (SurfaceView) findViewById(R.id.surface_camera);

        mSurfaceView.setOnClickListener(this);

        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);

        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        gvar = (GlobalVariable) getApplicationContext();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {


        mCamera.takePicture(null, mPictureCallback, mPictureCallback);
        return super.onTouchEvent(event);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {

        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onResume()
    {

        super.onResume();
    }

    @Override
    protected void onStop()
    {

        super.onStop();
    }

    Camera.PictureCallback mPictureCallback = new Camera.PictureCallback()
    {

        public void onPictureTaken(byte[] imageData, Camera c)
        {

            if (imageData != null)
            {
                // Intent mIntent = new Intent();
                // Calendar c1 = Calendar.getInstance();
                // String name ="image_"+ c1.get(Calendar.SECOND); // file image
                // dc luu trong SDCard
                // va co ten file la image_second.jpg
                // byte[] tmp = BitmapDecodeFactory.CropImage(imageData, 130,
                // 30, 165, 60);

                // StoreByteImage(mContext, imageData, 100, name);

                // BitmapFactory.Options options = new BitmapFactory.Options();
                // options.inSampleSize = 1;

                Bitmap myImage = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);

                int xx = myImage.getWidth();
                int yy = myImage.getHeight();

                float x = gvar.displayWidth / xx;
                float y = gvar.displayHeight / yy;

                Matrix matrix = new Matrix();
                matrix.postScale(x, y);

                Bitmap saveBmp = Bitmap.createBitmap(myImage, 0, 0, myImage.getWidth(), myImage.getHeight(), matrix, true);
                //

                gvar.backGroundBmp = cView.bmOut;
                gvar.humanBmp = saveBmp;

                Intent intent = new Intent(mContext, FinalView.class);
                startActivity(intent);

                //mCamera.startPreview();
                //setResult(FOTO_MODE, mIntent);
            }
        }
    };

    public void onClick(View view)
    {


        mCamera.takePicture(null, mPictureCallback, mPictureCallback);
    }

    public void surfaceCreated(SurfaceHolder holder)
    {

        try
        {
            mCamera = Camera.open();
        }
        catch (Exception ex)
        {

        }
    }

    private void setCamFocusMode()
    {

        if (null == mCamera)
        {
            return;
        }

        /* Set Auto focus */
        Camera.Parameters parameters = mCamera.getParameters();
        List<String> focusModes = parameters.getSupportedFocusModes();
        if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE))
        {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }
        else if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO))
        {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        }

        mCamera.setParameters(parameters);
    }


    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h)
    {

        // XXX stopPreview() will crash if preview is not running
        if (mPreviewRunning)
        {
            mCamera.stopPreview();
        }
    /*else
    {
	    mCamera.startPreview();
		mPreviewRunning = true;
	    
	}*/

        Camera.Parameters p = mCamera.getParameters();
        //
        List<Size> sizes = p.getSupportedPreviewSizes();
        for (int i = 0; i < sizes.size(); i++)
        {
            Log.e(i + ": ", sizes.get(i).width + "-" + sizes.get(i).height);
        }
        Size optimalSize = sizes.get(sizes.size() - 1);
        p.setPreviewSize(optimalSize.width, optimalSize.height);
        //p.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        //


        List<String> focusModes = p.getSupportedFocusModes();
        if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE))
        {
            p.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }
        else if(focusModes.contains(Camera.Parameters.FOCUS_MODE_INFINITY))
        {
            p.setFocusMode(Camera.Parameters.FOCUS_MODE_INFINITY);
        }
        else if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO))
        {
            p.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        }


        //p.setPreviewSize((int) gvar.displayWidth, (int) gvar.displayHeight);
        mCamera.setParameters(p);

        try
        {
            mCamera.setPreviewDisplay(holder);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        int rotation = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation();

        int degrees = 0;

        switch (rotation)
        {
            case Surface.ROTATION_0:
                degrees = 90;
                break;
            case Surface.ROTATION_90:
                degrees = 0;
                break;
            case Surface.ROTATION_180:
                degrees = 270;
                break;
            case Surface.ROTATION_270:
                degrees = 180;
                break;

        }

        mCamera.setDisplayOrientation(degrees);

        mCamera.startPreview();
        mPreviewRunning = true;
    }

    public void surfaceDestroyed(SurfaceHolder holder)
    {

        mCamera.stopPreview();
        if (mCamera != null)
        {
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }

}
