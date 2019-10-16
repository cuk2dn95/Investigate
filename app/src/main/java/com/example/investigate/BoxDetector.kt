package com.example.investigate

import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Rect
import android.graphics.YuvImage
import android.util.SparseArray
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.barcode.Barcode
import java.io.ByteArrayOutputStream

class BoxDetector(private val delegate: Detector<Barcode>, private var boxWidth: Int = 0, private var boxHeight: Int = 0) : Detector<Barcode>() {

    override fun detect(frame: Frame): SparseArray<Barcode> {
        if (boxWidth == 0 || boxHeight == 0) return SparseArray()
        val width = frame.metadata.width
        val height = frame.metadata.height
        val right = width / 2 + boxHeight / 2
        val left = width / 2 - boxHeight / 2
        val bottom = height / 2 + boxWidth / 2
        val top = height / 2 - boxWidth / 2

        val yuvImage = YuvImage(frame.grayscaleImageData.array(), ImageFormat.NV21, width, height, null)
        val byteArrayOutputStream = ByteArrayOutputStream()
        yuvImage.compressToJpeg(Rect(left, top, right, bottom), 70, byteArrayOutputStream)
        val jpegArray = byteArrayOutputStream.toByteArray()
        val bitmap = BitmapFactory.decodeByteArray(jpegArray, 0, jpegArray.size)

        val croppedFrame = Frame.Builder()
                .setBitmap(bitmap)
                .setRotation(frame.metadata.rotation)
                .build()

        return delegate.detect(croppedFrame)
    }

    fun updateDimension(width: Int, height: Int) {
        boxWidth = width
        boxHeight = height
    }
}
