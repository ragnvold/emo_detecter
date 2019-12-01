package com.westwin.khakaton

interface CameraContract {

    interface View {
        fun initView ()
        fun attachListeners ()
        fun startOther ()
        fun showEmotion (emo: String)
        fun showName (name: String)
        fun startDetectFace (): Int
        fun stopDetectFace (): Int
        fun loadAnimation ()
        fun startAnimation ()
        fun stopAnimation ()
    }

    interface Presenter {
        fun start ()
        fun sendImageOnAnalysis (name: String?, base64: String)

        fun onResume ()
        fun onPause ()
        fun onRestart ()
    }

    interface Repository {
        fun postImg (
            url: String,
            name: String?,
            img: String?,
            sync: CameraSync
        )

        interface CameraSync {
            fun sendEmotion (emo: String)
            fun sendName (name: String)

            fun onError (msg: String)
        }
    }
}