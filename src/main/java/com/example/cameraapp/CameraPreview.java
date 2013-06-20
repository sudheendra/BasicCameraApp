package com.example.cameraapp;

import android.content.Context;
import android.graphics.Rect;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sudheendra.sn on 6/17/13.
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback{
    private SurfaceHolder mHolder;
    private Camera mCamera;
    private Camera.Parameters mParams;

    public CameraPreview(Context context, Camera camera)
    {
        super(context);
        mCamera = camera;
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mParams = mCamera.getParameters();
    }
    /* public void setCamera(Camera camera) {
        if (mCamera == camera) { return; }

        stopPreviewAndFreeCamera();

        mCamera = camera;

        if (mCamera != null) {
            List<Size> localSizes = mCamera.getParameters().getSupportedPreviewSizes();
            mSupportedPreviewSizes = localSizes;
            requestLayout();

            try {
                mCamera.setPreviewDisplay(mHolder);
            } catch (IOException e) {
                e.printStackTrace();
            }


          Important: Call startPreview() to start updating the preview surface. Preview must
          be started before you can take a picture.

            mCamera.startPreview();
        }
    } */

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {
        Log.i("CameraApp", "SurfaceChanged");
        if (mHolder.getSurface() == null)
        {
            return;
        }
        try
        {
            mCamera.stopPreview();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        try
        {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void surfaceCreated(SurfaceHolder holder)
    {
// The Surface has been created, now tell the camera where to draw the preview.
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
            startFaceDetection();
        } catch (IOException e) {
            Log.e("CameraApp","Error setting camera preview: " + e.getMessage());
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder)
    {

    }

    public void startFaceDetection()
    {
        if (mParams.getMaxNumDetectedFaces() > 0)
        {
            mCamera.startFaceDetection();
        }
    }
}
