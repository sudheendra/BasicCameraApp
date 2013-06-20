package com.example.cameraapp;

import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MakePhotoActivity extends Activity {

    private Camera mCamera;
    private CameraPreview mPreview;
    private Camera.Parameters mParams;

    public static final int CAMERA_TYPE = 1;

    private boolean openCamera(int id)
    {
        boolean camOpened = false;
        try {
            releaseCameraandPreview();
            mCamera = Camera.open();
            camOpened = (mCamera != null);
            Log.i("CameraApp", "camera open successful");
            mCamera.setFaceDetectionListener(new MyFaceDetectionListener());
            mParams = mCamera.getParameters();
            List<String> focusModes =  mParams.getSupportedFocusModes();
            if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO))
            {
                Log.i("CameraApp","Auto Focus mode supported");
                mParams.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            }
        }
        catch (Exception ex) {
            Log.e(getString(R.string.app_name), "Failed to open Camera");
            ex.printStackTrace();
        }
        return camOpened;
    }

    private void releaseCameraandPreview()
    {
        // mPreview.setCamera(null);
        Log.i("CameraApp", "Releasing Camera");
        if (mCamera != null)
        {
            mCamera.release();
            mCamera = null;
        }
    }

    private File getOutputMediaFile()
    {
        File mediaStorageDir;
        mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"CameraApp");
        if (mediaStorageDir == null)
        {
            if (!mediaStorageDir.mkdirs())
                return null;
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = null;
        Log.i("CameraApp", mediaStorageDir.getPath());
        mediaFile = new File(mediaStorageDir.getPath(), File.separator + "IMG_" + timeStamp + ".jpg");
        return mediaFile;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("CameraApp", "in OnCreate call");
        setContentView(R.layout.activity_main);
        Button captureButton = (Button) findViewById(R.id.button_capture);

        boolean ret = openCamera(CAMERA_TYPE);
        if (!ret)
        {
            return;
        }

        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);

        final PictureCallback mJpegPictureCallback = new PictureCallback() {
            public void onPictureTaken(byte[] data, Camera camera)
            {
                Log.i("CameraApp", "On Picture Taken");
                File pictureFile = getOutputMediaFile();
                if (pictureFile == null)
                {
                    return;
                }
                Log.i("CameraApp", pictureFile.getAbsolutePath());
                try {
                    FileOutputStream fos = new FileOutputStream(pictureFile);
                    Log.i("CameraApp", "writing data");
                    fos.write(data);
                    fos.close();
                    Log.i("CameraApp", "creating gallery");
                    MediaStore.Images.Media.insertImage(getContentResolver(), pictureFile.getAbsolutePath(), pictureFile.getName(),"CameraApp");
                }
                catch (FileNotFoundException ex)
                {
                    Log.e(getString(R.string.app_name), "File not found");
                    ex.printStackTrace();
                }
                catch (IOException ex)
                {
                    Log.e(getString(R.string.app_name), "Failed to write");
                    ex.printStackTrace();
                }
            }
        };
        captureButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        Log.i("CameraApp", "Capture Button Clicked");
                        mCamera.takePicture(null, null, mJpegPictureCallback);
                    }
                }
        );
    }

    public void onPause()
    {
        super.onPause();
        releaseCameraandPreview();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.make_photo, menu);
        return true;
    }


    
}
