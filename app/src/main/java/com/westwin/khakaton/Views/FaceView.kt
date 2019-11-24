package com.westwin.khakaton.Views

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.SparseArray
import android.view.View
import com.google.android.gms.vision.face.Face

class FaceView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(
    context,
    attrs,
    defStyle
) {

    private var mBitmap: Bitmap? = null
    private var mFaces: SparseArray<Face>? = null

    fun setContent(bitmap: Bitmap, faces: SparseArray<Face>) {
        mBitmap = bitmap
        mFaces = faces
        invalidate()
    }

    fun drawFaceRectangle(canvas: Canvas, scale: Double) {
        val paint = Paint()
        paint.color = Color.GREEN
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 5f

        for (i in 0 until mFaces!!.size()) {
            val face = mFaces!!.valueAt(i)
            canvas.drawRect(
                (face.position.x * scale).toFloat(),
                (face.position.y * scale).toFloat(),
                ((face.position.x + face.width) * scale).toFloat(),
                ((face.position.y + face.height) * scale).toFloat(),
                paint
            )
        }
    }
}