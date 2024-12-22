package com.fjp.skeletalmuscle.viewmodel.state

import android.app.Activity
import android.graphics.Bitmap
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.example.pdftest.ui.ShareUtils
import com.fjp.skeletalmuscle.app.base.SMBaseViewModel
import kotlinx.coroutines.launch

/**
 *Author:Mr'x
 *Time:2024/11/1
 *Description:
 */
class SportsHighKneeCompletedViewModel : SMBaseViewModel() {
    val curScore = ObservableField("0")
    val sportsTime = ObservableField("0")
    val heartRate = ObservableField("0")
    val endurance = ObservableField("0")
    val heat = ObservableField("0")
    val totalCount = ObservableField("0")
    val leftCount = ObservableField("0")
    val rightCount = ObservableField("0")
    val avgHeart = ObservableField("0")
    val maxHeart = ObservableField("0")

   fun share(activity: Activity,view1:View,view2:View,view3: View){
        viewModelScope.launch{
            val titleBitmap =  ShareUtils.createBitmapByView(activity,view1)
            val centerBitmap =  ShareUtils.createBitmapByView(activity,view2)
            val bottomBitmap =  ShareUtils.createBitmapByView(activity,view3)
            val shareBitmap = ShareUtils.mergeBitmaps(titleBitmap,centerBitmap,bottomBitmap)
            ShareUtils.shareBitmap(activity,shareBitmap)
        }
    }
}