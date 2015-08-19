package com.vijayarunkumar.anupama.barcodevisionapiexample;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.FocusingProcessor;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Anu on 19/08/15.
 */
public class BarcodeScannerActivity extends Activity implements SurfaceHolder.Callback{

    @Bind(R.id.surface_view)SurfaceView mSurfaceView;

    private CameraSource mCameraSource;
    private Context mContext;
    private SurfaceHolder mSurfaceHolder;
    private BarcodeDetector mBarcodeDetector;
    private Camera mCamera = null;
    private Barcode mBarcode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.camera_main);

        ButterKnife.bind(this);

        mBarcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ITF | Barcode.PDF417| Barcode.QR_CODE)
                .build();
//        mBarcodeDetector.setProcessor(new FocusingProcessor<Barcode>(mBarcodeDetector, new Tracker<Barcode>()) {
//
//            @Override
//            public int selectFocus(Detector.Detections<Barcode> detections) {
//                SparseArray<Barcode> detectedItems = detections.getDetectedItems();
//                for (int i = 0; i < detectedItems.size(); i++) {
//                    Log.e(">>>", detectedItems.valueAt(i).rawValue);
//                    int key = detections.getDetectedItems().keyAt(i);
//                    return key;
//                }
//                return 0;
//            }
//        });


        mCameraSource = new CameraSource.Builder(this,mBarcodeDetector)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedPreviewSize(1600, 1024)
//                .setRequestedFps(15.0f)
                .build();

        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            mCameraSource.start(holder);
            Boolean isWorking = mBarcodeDetector.isOperational();
            Log.d(">>>", "is barode detector working : " +isWorking);

//            mBarcodeDetector.setProcessor(new BarcodeFocusingprocessor(mBarcodeDetector,new BarcodeTracker()));

//            mSurfaceView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (mCamera == null) {
//                        mCamera = Camera.open();
//                    }
//                    mCamera.autoFocus(new Camera.AutoFocusCallback() {
//                        @Override
//                        public void onAutoFocus(boolean success, Camera camera) {
//                            Log.d(">>>", "autofocus");
//                        }
//                    });
//                }
//            });
//            mCamera = Camera.open();
//            Camera.Parameters parameters = mCamera.getParameters();
//            parameters.setAntibanding(Camera.Parameters.ANTIBANDING_AUTO);
//            parameters.setVideoStabilization(true);
//            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_MACRO);
//            mCamera.setParameters(parameters);
//
//            mCamera.autoFocus(new Camera.AutoFocusCallback() {
//                @Override
//                public void onAutoFocus(boolean success, Camera camera) {
//                    Log.d(">>>","autofocus is : " +success);
//                }
//            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void releaseCameraAndPreview() {
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mBarcodeDetector.setProcessor(new BarcodeFocusingprocessor(mBarcodeDetector,new BarcodeTracker()));

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mCamera.setPreviewCallback(null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCamera.release();
        mCameraSource.release();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCamera.release();
        mCameraSource.release();
    }

private class BarcodeFocusingprocessor implements Detector.Processor<Barcode> {
    public BarcodeFocusingprocessor(BarcodeDetector barcodeDetector, BarcodeTracker barcodeTracker) {
        Log.d(">>>","BarcodeDetector is :" +barcodeDetector);

    }

    @Override
    public void release() {

    }

    @Override
    public void receiveDetections(Detector.Detections<Barcode> detections) {
        SparseArray<Barcode> detectedItems = detections.getDetectedItems();
        Log.d(">>>","items are " +detectedItems);
    }
}

private class BarcodeTracker extends Tracker<Barcode> {

    @Override
    public void onUpdate(Detector.Detections<Barcode> detections, Barcode item) {
        Log.d(">>>","barcode is :" +item);

    }
}
}
