package com.skeletalmuscle.appupdate.manager

import com.ninja.appupdate.base.BaseHttpDownLoadManager
import com.skeletalmuscle.appupdate.listener.OnDownloadListener
import com.skeletalmuscle.appupdate.utils.FileUtils
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL
import java.util.concurrent.Executors

class HttpDownLoadManager(var downloadPath: String) : BaseHttpDownLoadManager {
    private val HTTP_TIME_OUT = 10000
    private var shutdown = false
    private var apkUrl: String = ""
    private var apkName: String = ""
    private var listener: OnDownloadListener? = null
    private val executor = Executors.newSingleThreadExecutor()
    private val runnable = Runnable {
        if (!FileUtils.fileExists(downloadPath, apkName)) {
            FileUtils.delete(downloadPath, apkName)
        }
        fullDownload()
    }

    private fun fullDownload() {
        listener?.start()
        try {
            val url = URL(apkUrl)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.readTimeout = HTTP_TIME_OUT
            connection.connectTimeout = HTTP_TIME_OUT
            connection.setRequestProperty("Accept-Encoding", "identity")
            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val inputStream = connection.inputStream
                val contentLength = connection.contentLength
                var progress = 0
                val buffer = ByteArray(1024 * 2)
                val file = FileUtils.createFile(downloadPath, apkName)
                val stream = FileOutputStream(file)
                var len: Int
                while (inputStream.read(buffer).also { len = it } != -1 && !shutdown) {
                    stream.write(buffer, 0, len)
                    progress += len
                    listener?.onDownLoading(contentLength, progress)
                }

                if (shutdown) {
                    shutdown = false
                    listener?.cancel()
                } else {
                    listener?.done(file)
                }
                stream.flush()
                stream.close()
                inputStream.close()
            } else if (connection.responseCode == HttpURLConnection.HTTP_MOVED_PERM || connection.responseCode == HttpURLConnection.HTTP_MOVED_TEMP) {
                apkUrl = connection.getHeaderField("Location")
                connection.disconnect()
                fullDownload()
            } else {
                listener?.error(SocketTimeoutException("下载失败：Http ResponseCode = " + connection.responseCode))
            }
            connection.disconnect()
        } catch (e: Exception) {
            listener?.error(e)
        }
    }

    override fun download(apkUrl: String, apkName: String, listener: OnDownloadListener) {
        this.apkUrl = apkUrl
        this.apkName = apkName
        this.listener = listener
        executor.execute(runnable)
    }


    override fun cancle() {
        shutdown = true
    }

    override fun release() {
        listener = null
        executor.shutdown()
    }
}