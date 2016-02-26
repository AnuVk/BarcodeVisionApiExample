package com.vijayarunkumar.anupama.barcodevisionapiexample;

import android.util.Log;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.barcode.Barcode;

/**
 * Created by Anu on 26/02/2016.
 */
public class CustomBarcodeTracker extends Tracker<Barcode> {

    @Override
    public void onNewItem(int id, Barcode item) {
        Log.d(">>>", "items are !!!!" + item);
    }

    @Override
    public void onUpdate(Detector.Detections<Barcode> detections, Barcode item) {
        Log.d(">>>","barcode is :" +item.rawValue);
    }
}
