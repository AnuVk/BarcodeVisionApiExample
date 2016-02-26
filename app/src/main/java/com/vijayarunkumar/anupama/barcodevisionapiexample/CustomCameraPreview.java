package com.vijayarunkumar.anupama.barcodevisionapiexample;

import android.Manifest;
import android.content.Context;
import android.hardware.Camera;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 * Created by Anu on 06/11/2015.
 */
public class CustomCameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder mSurfaceHolder;

    public CameraSource getCameraSource() {
        return mCameraSource;
    }

    private CameraSource mCameraSource;
    private BarcodeDetector mBarcodeDetector;

    public CustomCameraPreview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CustomCameraPreview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomCameraPreview(Context context) {
        super(context);
    }

    @SuppressWarnings("deprecation") // needed for < 3.0
    private void initSurfaceHolder() {
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void start(CameraSource cameraSource, BarcodeDetector barcodeDetector) throws IOException, SecurityException{
        mBarcodeDetector = barcodeDetector;
        if (cameraSource == null){
            stop();
        }
        mCameraSource = cameraSource;

        if (mCameraSource != null){
            initSurfaceHolder();
        }
    }

    public void stop() {
        if (mCameraSource != null){
            mCameraSource.stop();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            mCameraSource.start(holder);
            Boolean isWorking = mBarcodeDetector.isOperational();
            Log.d(">>>", "is barode detector working : " + isWorking);
            cameraFocus(mCameraSource,Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        } catch (IOException e) {
            e.printStackTrace();
        }    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    public static boolean cameraFocus(@NonNull CameraSource cameraSource, @NonNull String focusMode) {
        Field[] declaredFields = CameraSource.class.getDeclaredFields();

        for (Field field : declaredFields) {
            if (field.getType() == Camera.class) {
                field.setAccessible(true);
                try {
                    Camera camera = (Camera) field.get(cameraSource);
                    if (camera != null) {
                        Camera.Parameters params = camera.getParameters();

                        if (!params.getSupportedFocusModes().contains(focusMode)) {
                            return false;
                        }

                        params.setFocusMode(focusMode);
                        camera.setParameters(params);
                        return true;
                    }

                    return false;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                break;
            }
        }
        return false;
    }
}
