package com.westwin.khakaton

interface CameraContract {

    interface View {
        fun initView ()
        fun attachListeners ()
        fun startOther ()
        fun showEmotion (emo: String)
        fun showName (name: String)
        fun startAnim ()
    }

    interface Presenter {
        fun start ()
        fun JSON_Request (name: String?, base64: String)
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
        }
    }
}