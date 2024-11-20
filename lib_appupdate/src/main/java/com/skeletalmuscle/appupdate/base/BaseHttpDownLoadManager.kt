package com.ninja.appupdate.base

import com.skeletalmuscle.appupdate.listener.OnDownloadListener

interface BaseHttpDownLoadManager {

    fun download(apkUrl:String,apkName:String,listener: OnDownloadListener)

    fun cancle()

    fun release()
}