package com.fjp.skeletalmuscle.data.model.bean.result

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 *Author:Mr'x
 *Time:2024/12/7
 *Description:
 */
data class AppVersion(
    val `data`: VersionData,
    val message: String,
    val status: String
)

@Parcelize
data class VersionData(
    val app_name: String,
    val content: Array<String>,
    val download_url: String,
    val id: Int,
    val version: String,
): Parcelable