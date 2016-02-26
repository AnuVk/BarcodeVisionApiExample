package com.vijayarunkumar.anupama.barcodevisionapiexample;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BarcodeMainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 12;
    private static final String TAG = BarcodeMainActivity.class.getSimpleName();
    @Bind (R.id.scan_barcode) Button mScanBarcodeButton;
    private Context mContext;
    private View mLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mLayout = findViewById(R.id.activity_main);
        ButterKnife.bind(this);

        mContext = getApplicationContext();

        mScanBarcodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int rc = ActivityCompat.checkSelfPermission(BarcodeMainActivity.this, Manifest.permission.CAMERA);
                if (rc == PackageManager.PERMISSION_GRANTED){
                    launchCameraForScanning();
                }
                else {
                    requestCameraPermission();
                }
            }
        });
    }

    private void requestCameraPermission() {
        Log.w(TAG, "Camera permission is not granted. Requesting permission");

        final String[] permissions = new String[]{Manifest.permission.CAMERA};

        if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_ASK_PERMISSIONS);
            return;
        }

        final Activity thisActivity = this;

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(thisActivity, permissions,
                        REQUEST_CODE_ASK_PERMISSIONS);
            }
        };

        Snackbar.make(mLayout, "Access to camera is needed for detection",
                Snackbar.LENGTH_INDEFINITE)
                .setAction("OK", listener)
                .show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    Snackbar.make(mLayout, "Camera permission request was granted.",
                            Snackbar.LENGTH_SHORT)
                            .show();
                    launchCameraForScanning();
                } else {
                    // Permission Denied
                    Snackbar.make(mLayout, "Camera permission request was denied.",
                            Snackbar.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void launchCameraForScanning() {
        Intent intent = new Intent(mContext, BarcodeScannerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
