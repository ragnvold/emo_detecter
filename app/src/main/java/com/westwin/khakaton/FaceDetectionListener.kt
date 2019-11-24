package com.westwin.khakaton

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.hardware.Camera
import android.util.Log
import android.graphics.Rect

class FaceDetectionListener: Camera.FaceDetectionListener {

    override fun onFaceDetection(faces: Array<Camera.Face>, camera: Camera) {

        if (faces.isEmpty()) {
            Log.i("CAMERA", "No faces detected")
        } else if (faces.isNotEmpty()) {
            Log.i("CAMERA", "Faces Detected = " + faces.size.toString())

            val faceRects: MutableList<Rect>
            faceRects = ArrayList()

            val paint = Paint()
            paint.color = Color.GREEN
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 5f

            val canvas = Canvas()

            for (i in faces.indices) {
                val left = faces[i].rect.left
                val right = faces[i].rect.right
                val top = faces[i].rect.top
                val bottom = faces[i].rect.bottom
                val uRect = Rect(left, top, right, bottom)
                //faceRects.add(uRect)

                canvas.drawRect(uRect, paint)
            }
        }
    }
}