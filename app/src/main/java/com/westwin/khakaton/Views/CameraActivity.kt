package com.westwin.khakaton.Views

import android.content.DialogInterface
import android.hardware.Camera
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.westwin.khakaton.CameraContract
import com.westwin.khakaton.FaceDetectionListener
import com.westwin.khakaton.Presenters.CameraActivityPresenter
import com.westwin.khakaton.R

class CameraActivity: AppCompatActivity (), View.OnClickListener, CameraContract.View {

    private lateinit var mCamera: Camera
    private lateinit var mCameraPreview: CameraPreview

    private lateinit var preview: LinearLayout
    private lateinit var take_photo_btn: ImageButton

    private lateinit var mPresenter: CameraActivityPresenter

    private lateinit var mEtName: EditText

    private lateinit var ranim: Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mPresenter = CameraActivityPresenter(this)
        mPresenter.start()

        startDetectFace()

        ranim = AnimationUtils.loadAnimation(this, R.anim.rotate) as Animation
    }

    override fun onResume() {
        super.onResume()

        startDetectFace()
    }

    override fun onPause() {
        super.onPause()

        stopDetectFace()
    }

    override fun onRestart() {
        super.onRestart()

        mCamera.startPreview()

        startDetectFace()
    }

    private fun startDetectFace(): Int {
        mCamera.startPreview()
        val FDListener = FaceDetectionListener()
        mCamera.setFaceDetectionListener(FDListener)
        try {
            mCamera.startFaceDetection()
        } catch (e: Exception) {
            Log.i("CAMERA", "START")
        }
        return 0
    }

    private fun stopDetectFace(): Int {
        try {
            mCamera.stopFaceDetection()
        } catch (e: Exception) {
            Log.i("CAMERA", "STOP")
        }

        return 0
    }

    override fun onClick(p0: View?) {
        if (p0 != null)
            when (p0.id) {
                R.id.take_photo -> {
                    val inflater = LayoutInflater.from(this)
                    val view = inflater.inflate(R.layout.save_face_alert, null)
                    mEtName = view.findViewById(R.id.username)
                    AlertDialog.Builder(this)
                        .setTitle("Save face")
                        .setView(view)
                        .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
                            val name = mEtName.text.toString()
                            mCamera.takePicture(null, null, Camera.PictureCallback { data, camera ->
                        try {
                            mPresenter.JSON_Request(name, String(Base64.encode(data, 0)))
                            mCameraPreview.refreshCamera(camera)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    })
                        })
                        .show()
                }
            }
    }

    override fun startAnim() {
        take_photo_btn.startAnimation(ranim)
    }

    override fun initView() {
        preview = findViewById(R.id.camera_preview)
        take_photo_btn = findViewById(R.id.take_photo)
    }

    override fun attachListeners() {
        take_photo_btn.setOnClickListener(this)
    }

    override fun startOther() {
        mCamera = Camera.open(1)
        mCamera.setDisplayOrientation(90)

        mCameraPreview = CameraPreview(this, mCamera)
        //mFaceView = FaceView(this, null, 0)

        //mFaceView.setContent()

        preview.addView(mCameraPreview)
    }

    override fun showEmotion(emo: String) {
        take_photo_btn.animate().cancel()
        if (emo.isNotEmpty())
            when (emo) {
                "angry" -> { take_photo_btn.setImageResource(R.drawable.angry) }
                "disgust" -> { take_photo_btn.setImageResource(R.drawable.disgusted) }
                "fear" -> { take_photo_btn.setImageResource(R.drawable.fearful) }
                "happy" -> { take_photo_btn.setImageResource(R.drawable.happy) }
                "sad" -> { take_photo_btn.setImageResource(R.drawable.sad) }
                "surprise" -> { take_photo_btn.setImageResource(R.drawable.surprised) }
                "neutral" -> { take_photo_btn.setImageResource(R.drawable.neutral) }
            }
    }

    override fun showName(name: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}