package com.fjp.skeletalmuscle.app.ext

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import coil.transform.CircleCropTransformation
import com.fjp.skeletalmuscle.R

@BindingAdapter(value = ["url"], requireAll = false)
fun setImageUri(imageView: ImageView, url: String) {
    imageView.load(url,builder = {
        allowHardware(false)
        this.error(R.drawable.avatar_default)
        this.placeholder(R.drawable.avatar_default)
    })
    }
