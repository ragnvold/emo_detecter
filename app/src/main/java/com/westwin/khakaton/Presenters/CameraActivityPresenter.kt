package com.westwin.khakaton.Presenters

import com.westwin.khakaton.CameraContract
import com.westwin.khakaton.Repos.CameraRepo
import com.westwin.khakaton.Views.CameraActivity

class CameraActivityPresenter (private var mView: CameraActivity): CameraContract.Presenter, CameraContract.Repository.CameraSync {

    private var mRepository: CameraContract.Repository = CameraRepo ()

    override fun start() {
        mView.initView()
        mView.attachListeners()
        mView.startOther()
    }

    override fun JSON_Request(name: String?, base64: String) {
        mRepository.postImg(
            "https://httpbin.org/post",
            name,
            base64,
            this
        )
        mView.startAnim()
    }

    override fun sendEmotion(emo: String) {
        mView.showEmotion(emo)
    }

    override fun sendName(name: String) {
        mView.showName(name)
    }
}