package com.vijayarunkumar.anupama.barcodevisionapiexample;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Anu on 19/08/15.
 */
public class BarcodeScannerActivity extends AppCompatActivity{

    private static final String TAG = BarcodeScannerActivity.class.getSimpleName();
    @Bind(R.id.camera_preview) CustomCameraPreview mCustomCameraPreview;

    private CameraSource mCameraSource;
    private BarcodeDetector mBarcodeDetector;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_main);

        ButterKnife.bind(this);

        mBarcodeDetector = new BarcodeDetector.Builder(this).build();
        BarcodeTrackerFactory barcodeTrackerFactory = new BarcodeTrackerFactory();

        mBarcodeDetector.setProcessor(
                new MultiProcessor.Builder<>(barcodeTrackerFactory).build());

        if (mBarcodeDetector.isOperational()){
            Log.w(TAG, "Detector dependencies are not yet available.");

            IntentFilter lowstorageFilter = new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
            boolean hasLowStorage = registerReceiver(null, lowstorageFilter) != null;

            if (hasLowStorage) {
                Toast.makeText(this, "Low memory", Toast.LENGTH_LONG).show();
                Log.w(TAG, "Dependencies cannot be downloaded due to low storage");
            }
        }

        mCameraSource = new CameraSource.Builder(this, mBarcodeDetector)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedPreviewSize(1600, 1024)
                .setRequestedFps(15.0f).build();

        try {
            mCustomCameraPreview.start(mCameraSource, mBarcodeDetector);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        startCameraSourcePreview();
    }

    private void startCameraSourcePreview() {
        try {
            mCustomCameraPreview.start(mCameraSource, mBarcodeDetector);
        } catch (IOException e){
            mCameraSource.release();
            mCameraSource = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCustomCameraPreview.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCameraSource.release();
    }
}
