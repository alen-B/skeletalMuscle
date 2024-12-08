package com.skeletalmuscle.appupdate.utils

import java.io.File


object FileUtils {
    fun fileExists(downloadPath: String, fileName: String): Boolean {
        return File(downloadPath, fileName).exists()
    }

    fun createFile(downloadPath: String, fileName: String): File {
        return File(downloadPath, fileName)
    }

    fun delete(downloadPath: String, fileName: String): Boolean {
        return File(downloadPath, fileName).delete()
    }

    fun createDirectory(downloadPath: String) {
        val file = File(downloadPath)
        if (!file.exists()) {
            file.mkdirs()
        }
    }
}