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

class CameraActivity : AppCompatActivity(), View.OnClickListener, CameraContract.View {

    private val TAG = "APP"

    // for face scanning
    private lateinit var mCamera: Camera
    private lateinit var mCameraPreview: CameraPreview

    // layout
    private lateinit var preview: LinearLayout

    // views
    private lateinit var takePhotoBtn: ImageButton
    private lateinit var mEtName: EditText

    private lateinit var mPresenter: CameraActivityPresenter

    private lateinit var ranim: Animation

    // lifecycle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mPresenter = CameraActivityPresenter(this)
        mPresenter.start()
    }

    override fun onResume() {
        super.onResume()
        mPresenter.onResume()
    }

    override fun onPause() {
        super.onPause()
        mPresenter.onPause()
    }

    override fun onRestart() {
        super.onRestart()
        mPresenter.onRestart()
    }

    // init views

    override fun initView() {
        preview = findViewById(R.id.camera_preview)
        takePhotoBtn = findViewById(R.id.take_photo)
    }

    override fun attachListeners() {
        takePhotoBtn.setOnClickListener(this)
    }

    override fun startOther() {
        mCamera = Camera.open(1)
        mCamera.setDisplayOrientation(90)

        mCameraPreview = CameraPreview(this, mCamera)

        preview.addView(mCameraPreview)
    }

    // animation

    override fun loadAnimation() {
        ranim = AnimationUtils
            .loadAnimation(
                this,
                R.anim.rotate
            ) as Animation
    }

    override fun startAnimation() {
        takePhotoBtn.startAnimation(ranim)
    }

    override fun stopAnimation() {
        takePhotoBtn.animate().cancel()
    }

    // face detection

    override fun startDetectFace(): Int {
        mCamera.startPreview()
        val FDListener = FaceDetectionListener()
        mCamera.setFaceDetectionListener(FDListener)
        try {
            mCamera.startFaceDetection()
        } catch (e: Exception) {
            Log.i(TAG, "Face detection started")
        }
        return 0
    }

    override fun stopDetectFace(): Int {
        try {
            mCamera.stopFaceDetection()
        } catch (e: Exception) {
            Log.i(TAG, "Face detection stopped")
        }

        return 0
    }

    // inputs

    override fun onClick(p0: View?) {
        if (p0 != null)
            when (p0.id) {
                R.id.take_photo -> {
                    val inflater = LayoutInflater
                        .from(this)
                    val view = inflater
                        .inflate(
                            R.layout.save_face_alert,
                            null
                        )
                    mEtName = view
                        .findViewById(R.id.username)
                    AlertDialog.Builder(this)
                        .setTitle("Save face")
                        .setView(view)
                        .setPositiveButton(
                            "Yes",
                            DialogInterface.OnClickListener { dialog, which ->
                                mCamera
                                    .takePicture(
                                        null,
                                        null,
                                        Camera.PictureCallback { data, camera ->
                                            try {
                                                mPresenter
                                                    .sendImageOnAnalysis(
                                                        mEtName
                                                            .text
                                                            .toString(),
                                                        String(
                                                            Base64.encode(
                                                                data,
                                                                0
                                                            )
                                                        )
                                                    )
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

    // reactions

    override fun showEmotion(emo: String) {
        if (emo.isNotEmpty()) {
            when (emo) {
                getString(R.string.angry) -> {
                    takePhotoBtn.setImageResource(R.drawable.angry)
                }
                getString(R.string.disgust) -> {
                    takePhotoBtn.setImageResource(R.drawable.disgusted)
                }
                getString(R.string.fear) -> {
                    takePhotoBtn.setImageResource(R.drawable.fearful)
                }
                getString(R.string.happy) -> {
                    takePhotoBtn.setImageResource(R.drawable.happy)
                }
                getString(R.string.sad) -> {
                    takePhotoBtn.setImageResource(R.drawable.sad)
                }
                getString(R.string.surprise) -> {
                    takePhotoBtn.setImageResource(R.drawable.surprised)
                }
                getString(R.string.neutral) -> {
                    takePhotoBtn.setImageResource(R.drawable.neutral)
                }
            }
        }
    }

    override fun showName(name: String) {
        Toast
            .makeText(
                this,
                "Your name is $name",
                Toast.LENGTH_SHORT
            ).show()
    }
}