package com.example.cameraapp;

import android.graphics.Rect;
import android.hardware.Camera;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by sudheendra.sn on 6/18/13.
 */
public class MyFaceDetectionListener implements Camera.FaceDetectionListener
{
    private void setmetering(Camera camera, Rect rect)
    {
        Camera.Parameters params = camera.getParameters();
        if (params.getMaxNumMeteringAreas() > 0)
        {
            List<Camera.Area> meterRectList = new ArrayList<Camera.Area>();
            meterRectList.add(new Camera.Area(rect, 600));
            params.setMeteringAreas(meterRectList);
            Log.i("CameraApp", "Metering");
            camera.setParameters(params);
        }
    }
    public void onFaceDetection(Camera.Face[] faces, Camera camera)
    {
        Log.i("CameraApp", "Face Detection: " + faces.length);
        for (int i = 0 ; i < faces.length ; i++)
        {
            Log.i("Face Detection", "Face : " + i + " Location X: " + faces[i].rect.centerX() + " Location Y: " + faces[i].rect.centerY());
            Rect facerect = new Rect(faces[i].rect.left, faces[i].rect.top, faces[i].rect.right, faces[i].rect.bottom);
            setmetering(camera, facerect);
        }
    }
}
