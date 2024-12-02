package com.fjp.skeletalmuscle.app.ext

import android.text.TextUtils
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation

@BindingAdapter(value = ["url", "placeholderRes","errRes","circle"], requireAll = false)
fun setImageUri(imageView: ImageView, url: String, placeholderRes: Int?,errRes: Int?,circle: Boolean?) {
    if (!TextUtils.isEmpty(url)) { //使用coil框架加载图片
        imageView.load(url){
            errRes?.let { error(it) }
            placeholderRes?.let { placeholder(it) }
            if(circle == true){
                transformations(CircleCropTransformation())
            }
        }
    }

}