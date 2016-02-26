package com.vijayarunkumar.anupama.barcodevisionapiexample;

import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.barcode.Barcode;

/**
 * Created by Anu on 26/02/2016.
 */
public class BarcodeTrackerFactory implements MultiProcessor.Factory<Barcode>{
    @Override
    public Tracker<Barcode> create(Barcode barcode) {
        return new CustomBarcodeTracker();
    }
}
