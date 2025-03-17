package com.fjp.skeletalmuscle.ui.setting.fragment

import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import com.fjp.skeletalmuscle.app.base.BaseFragment
import com.fjp.skeletalmuscle.app.util.CacheUtil
import com.fjp.skeletalmuscle.data.model.bean.result.VersionData
import com.fjp.skeletalmuscle.databinding.FragmentUpdateVersionBinding
import com.fjp.skeletalmuscle.viewmodel.state.UpdateVersionViewModel
import com.luck.picture.lib.thread.PictureThreadUtils.runOnUiThread
import com.luck.picture.lib.utils.ToastUtils.showToast
import com.skeletalmuscle.appupdate.listener.OnDownloadListener
import com.skeletalmuscle.appupdate.manager.HttpDownLoadManager
import com.skeletalmuscle.appupdate.utils.ApkUtils
import java.io.File

class UpdateVersionFragment(val versionData: VersionData) : BaseFragment<UpdateVersionViewModel, FragmentUpdateVersionBinding>(), CompoundButton.OnCheckedChangeListener, OnDownloadListener {
    private var apkFile: File? = null

    companion object {
        fun newInstance(versionData: VersionData) = UpdateVersionFragment(versionData)
    }

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mDatabind.click = ProxyClick()
        mViewModel.versionTitle.set("骨骼肌V${versionData.version}")
        versionData.content.forEach {
            mViewModel.content.set(mViewModel.content.get() + it + '\n')
        }

        val isCheck = CacheUtil.getVoiceInteraction()

    }


    inner class ProxyClick {

        fun clickUpdate() {
            activity?.externalCacheDir?.let {
                mDatabind.progressBar.visibility = View.VISIBLE
                HttpDownLoadManager(it.path).download(versionData.download_url, "gugeji.apk", this@UpdateVersionFragment)
            }
        }

        fun clickDisUpdate() {
            parentFragmentManager.popBackStack()
        }
    }

    override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
        CacheUtil.setVoiceInteraction(p1)
    }

    override fun start() {
    }

    override fun onDownLoading(max: Int, progress: Int) {
        val curr = (progress / max.toDouble() * 100.0).toInt()
        runOnUiThread {
            mDatabind.progressBar.setProgress(curr)
        }
    }

    override fun cancel() {
    }

    override fun done(apk: File) {
        runOnUiThread {
            activity?.let {
                if (!it.packageManager.canRequestPackageInstalls()) {
                    this@UpdateVersionFragment.apkFile = apk
                    ApkUtils.startPackageInstallActivity(it)
                } else {
                    ApkUtils.installAPK(it, apk)
                }
            }


        }
    }

    override fun error(e: Exception) {
        runOnUiThread {
            e.printStackTrace()
            showToast(activity, "更新失败！")
        }
    }
}