package com.example.investigate

import android.os.Handler
import android.os.Looper
import androidx.annotation.UiThread
import com.google.android.gms.vision.Tracker
import com.google.android.gms.vision.barcode.Barcode

class BarcodeTracker(private val listener: BarcodeUpdateListener) : Tracker<Barcode>() {

    interface BarcodeUpdateListener {

        @UiThread
        fun onBarcodeDetected(barcode: Barcode)
    }

    override fun onNewItem(id: Int, barcode: Barcode) {
        super.onNewItem(id, barcode)
        Handler(Looper.getMainLooper()).post {
            listener.onBarcodeDetected(barcode)
        }
    }
}
