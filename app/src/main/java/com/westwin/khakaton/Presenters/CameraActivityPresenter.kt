package com.westwin.khakaton.Presenters

import com.westwin.khakaton.CameraContract
import com.westwin.khakaton.Repos.CameraRepo
import com.westwin.khakaton.Views.CameraActivity

class CameraActivityPresenter(private var mView: CameraActivity) : CameraContract.Presenter,
    CameraContract.Repository.CameraSync {

    private var mRepository: CameraContract.Repository = CameraRepo()

    // init views

    override fun start() {
        mView.initView()
        mView.attachListeners()
        mView.startOther()
        mView.startDetectFace()
    }

    // network request

    override fun sendImageOnAnalysis(name: String?, base64: String) {
        mRepository.postImg(
            "https://httpbin.org/post",
            name,
            base64,
            this
        )
        mView.startAnimation()
    }

    // handling network response

    override fun onError(msg: String) {}

    override fun sendEmotion(emo: String) {
        mView.stopAnimation()
        mView.showEmotion(emo)
    }

    override fun sendName(name: String) {
        mView.showName(name)
    }

    // lifecycle

    override fun onResume() {
        mView.startDetectFace()
    }

    override fun onPause() {
        mView.stopDetectFace()
    }

    override fun onRestart() {
        mView.startDetectFace()
    }
}