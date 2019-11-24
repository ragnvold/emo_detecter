package com.westwin.khakaton.Views

import android.content.Context
import android.hardware.Camera
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.westwin.khakaton.CameraPreview
import java.io.IOException

class CameraPreview (context: Context, camera: Camera) : SurfaceView (context), SurfaceHolder.Callback, CameraPreview
{
    private var mCamera: Camera = camera

    private var mHolder: SurfaceHolder = holder

    init {
        mHolder.addCallback(this)
    }

    override fun refreshCamera(camera: Camera) {
        if (mHolder.surface == null) {
            // preview surface does not exist
            return
        }
        // stop preview before making changes
        try {
            mCamera.stopPreview()
        } catch (e: Exception) {
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here
        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(mHolder)
            mCamera.startPreview()
        } catch (e: Exception) {
            //Log.d(View.VIEW_LOG_TAG, "Error starting camera preview: " + e.message)
        }
    }

    override fun surfaceCreated(p0: SurfaceHolder?) {
        try {
            mCamera.setPreviewDisplay(p0)
            mCamera.startPreview()
        } catch (e: IOException) {
            //Log.d(View.VIEW_LOG_TAG, "Error setting camera preview: " + e.message)
        }
    }

    override fun surfaceChanged(p0: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {
        refreshCamera(mCamera)
    }

    override fun surfaceDestroyed(p0: SurfaceHolder?) {
        mCamera.stopPreview()
        mCamera.release()
    }
}