package com.fjp.skeletalmuscle.app.util

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.OvershootInterpolator

object AnimUtil {
    fun flipAnimatorYViewShow(oldView: View, newView: View, time: Long) {
        val animator1 = ObjectAnimator.ofFloat(oldView, "rotationY", 0f, 90f)
        val animator2 = ObjectAnimator.ofFloat(newView, "rotationY", -90f, 0f)
        animator2.interpolator = OvershootInterpolator(2.0f)
        animator1.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationEnd(animation: Animator) {
                oldView.visibility = View.GONE
                animator2.setDuration(time).start()
                newView.visibility = View.VISIBLE
            }

            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })
        animator1.setDuration(time).start()
    }
}