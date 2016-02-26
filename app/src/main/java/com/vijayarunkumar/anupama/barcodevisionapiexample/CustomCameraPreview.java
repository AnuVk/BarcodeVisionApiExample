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
            Log.d(">>>", "is bacrode detector working : " + isWorking);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(">>>","io exception in surfaceCreated");
        } catch (SecurityException s){
            s.printStackTrace();
            Log.e(">>>","Security exception in surfaceCreated");
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }
}
