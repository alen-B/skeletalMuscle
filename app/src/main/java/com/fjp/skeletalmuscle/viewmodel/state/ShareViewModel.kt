package com.fjp.skeletalmuscle.viewmodel.state

import android.app.Activity
import android.view.View
import androidx.lifecycle.viewModelScope
import com.example.pdftest.ui.ShareUtils
import com.fjp.skeletalmuscle.app.base.SMBaseViewModel
import kotlinx.coroutines.launch

/**
 *Author:Mr'x
 *Time:2024/12/22
 *Description:
 */
class ShareViewModel:SMBaseViewModel() {
    fun share(activity: Activity,view1: View,view2: View,view3: View){
        viewModelScope.launch{
            val titleBitmap =  ShareUtils.createBitmapByView(activity,view1)
            val centerBitmap =  ShareUtils.createBitmapByView(activity,view2)
            val bottomBitmap =  ShareUtils.createBitmapByView(activity,view3)
            val shareBitmap = ShareUtils.mergeBitmaps(titleBitmap,centerBitmap,bottomBitmap)
            ShareUtils.shareBitmap(activity,shareBitmap)
        }
    }
}