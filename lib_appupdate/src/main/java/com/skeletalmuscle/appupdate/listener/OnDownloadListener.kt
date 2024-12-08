package com.skeletalmuscle.appupdate.listener

import java.io.File

interface OnDownloadListener {

    fun start()

    fun onDownLoading(max: Int, progress: Int)

    fun cancel()

    fun done(apk: File)

    fun error(e: Exception)
}