package com.example.pdftest.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

//androidmanifest.xml中添加
//<provider
//android:name="androidx.core.content.FileProvider"
//android:authorities="${applicationId}.fileprovider"
//android:exported="false"
//android:grantUriPermissions="true">
//<meta-data
//android:name="android.support.FILE_PROVIDER_PATHS"
//android:resource="@xml/file_paths" />
//</provider>
class ShareUtils {
    fun shareBitmap(context: Context, bitmap: Bitmap) {
        val shareIntent = Intent()
        shareIntent.setAction(Intent.ACTION_SEND)
        shareIntent.putExtra(Intent.EXTRA_STREAM, getUriForBitmap(context,bitmap))
        shareIntent.setType("image/jpeg")
        context.startActivity(Intent.createChooser(shareIntent, "Share bitmap using"))
    }

    private fun getUriForBitmap(context:Context,bitmap: Bitmap): Uri {
        // 将Bitmap转换为文件并保存到缓存中
        val file = File(context.cacheDir, "shared_bitmap.jpg")
        try {
            val fileOutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
            fileOutputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }


        // 返回文件的Uri
        return FileProvider.getUriForFile(
            context,
            context.packageName + ".fileprovider",
            file
        )
    }

    fun mergeBitmaps(bitmap1: Bitmap, bitmap2: Bitmap): Bitmap {
        // 创建一个新的位图，其大小为两个bitmap的总和
        val mergedBitmap = Bitmap.createBitmap(
            bitmap1.width,
            bitmap1.height + bitmap2.height,
            Bitmap.Config.ARGB_8888
        )
        // 创建一个画布来在新位图上绘制
        val canvas = Canvas(mergedBitmap)
        // 在画布上绘制第一个bitmap
        canvas.drawBitmap(bitmap1, 0f, 0f, null)
        // 在画布上绘制第二个bitmap，紧接着第一个bitmap
        canvas.drawBitmap(bitmap2, 0f, bitmap1.height.toFloat(), null)
        return mergedBitmap
    }

    //将隐藏view转换为bitmap
    private fun createBitmapByView(context: Activity, view: View): Bitmap {
        //计算设备分辨率
        val manager: WindowManager = context.getWindowManager()
        val metrics = DisplayMetrics()
        manager.defaultDisplay.getMetrics(metrics)
        val width = metrics.widthPixels
        val height = metrics.heightPixels

        //测量使得view指定大小
        val measureWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY)
        val measureHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.AT_MOST)

        view.measure(measureWidth, measureHeight)
        //调用layout方法布局后，可以得到view的尺寸
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)

        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE)
        view.draw(canvas)
        return bitmap
    }
}