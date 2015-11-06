package com.vijayarunkumar.anupama.barcodevisionapiexample;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.lang.reflect.Field;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Anu on 19/08/15.
 */
//public class BarcodeScannerActivity extends Activity implements SurfaceHolder.Callback{
public class BarcodeScannerActivity extends Activity{

        //    @Bind(R.id.surface_view)SurfaceView mSurfaceView;
    @Bind(R.id.camera_preview) CustomCameraPreview mCustomeCameraPreview;

    private CameraSource mCameraSource;
//    private SurfaceHolder mSurfaceHolder;
    private BarcodeDetector mBarcodeDetector;
//    private CustomCameraPreview mCustomCameraPreview;
    private Camera mCamera;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_main);

        ButterKnife.bind(this);

        mBarcodeDetector = new BarcodeDetector.Builder(this)
                .build();
        BarcodeFocusingprocessor barcodeFactory = new BarcodeFocusingprocessor();
        mBarcodeDetector.setProcessor(
                new MultiProcessor.Builder<>(barcodeFactory).build());


        mCameraSource = new CameraSource.Builder(this, mBarcodeDetector)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedPreviewSize(1600, 1024)
                .build();

        mCamera = getCameraInstance();
        if (cameraAvailable(mCamera)){
            mCustomeCameraPreview.init(mCameraSource,mBarcodeDetector);
        }
        else {
            finish();
        }

//        mSurfaceHolder = mSurfaceView.getHolder();
//        mSurfaceHolder.addCallback(this);
    }


    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e) {
            // Camera is not available or doesn't exist
            Log.d("getCamera failed", e.getMessage());
        }
        return c;
    }

    public static boolean cameraAvailable(Camera camera) {
        return camera != null;
    }

//    @Override
//    public void surfaceCreated(SurfaceHolder holder) {
//        try {
//            mCameraSource.start(holder);
//            Boolean isWorking = mBarcodeDetector.isOperational();
//            Log.d(">>>", "is barode detector working : " + isWorking);
//            cameraFocus(mCameraSource,Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//    }
//
//    @Override
//    public void surfaceDestroyed(SurfaceHolder holder) {
//    }



private class BarcodeFocusingprocessor implements MultiProcessor.Factory<Barcode> {

    @Override
    public Tracker<Barcode> create(Barcode barcode) {
        return new BarcodeTracker();
    }
}

private class BarcodeTracker extends Tracker<Barcode>{

    @Override
    public void onNewItem(int id, Barcode item) {
        Log.d(">>>", "items are !!!!" + item);
    }

    @Override
    public void onUpdate(Detector.Detections<Barcode> detections, Barcode item) {
        Log.d(">>>","barcode is :" +item.rawValue);

    }
}


//    @Override
//    protected void onResume() {
//        super.onResume();
//    }

    @Override
    protected void onPause() {
        super.onPause();
       releaseCamera();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseCamera();
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        releaseCamera();
//    }

    private void releaseCamera() {
        if(mCamera != null){
            mCamera.release();
            mCamera = null;
        }
    }

//    public static boolean cameraFocus(@NonNull CameraSource cameraSource, @NonNull String focusMode) {
//        Field[] declaredFields = CameraSource.class.getDeclaredFields();
//
//        for (Field field : declaredFields) {
//            if (field.getType() == Camera.class) {
//                field.setAccessible(true);
//                try {
//                    Camera camera = (Camera) field.get(cameraSource);
//                    if (camera != null) {
//                        Camera.Parameters params = camera.getParameters();
//
//                        if (!params.getSupportedFocusModes().contains(focusMode)) {
//                            return false;
//                        }
//
//                        params.setFocusMode(focusMode);
//                        camera.setParameters(params);
//                        return true;
//                    }
//
//                    return false;
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                }
//
//                break;
//            }
//        }
//
//        return false;
//    }
}
