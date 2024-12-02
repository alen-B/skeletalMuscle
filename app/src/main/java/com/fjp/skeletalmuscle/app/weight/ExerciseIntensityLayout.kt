package com.fjp.skeletalmuscle.app.weight

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.LinearLayoutCompat
import com.fjp.skeletalmuscle.R


/**
 *Author:Mr'x
 *Time:2024/11/1
 *Description:
 */
class ExerciseIntensityLayout(context: Context, attrs: AttributeSet) : LinearLayoutCompat(context, attrs) {
    var warmupTimeTv: View
    var fatBurningTimeTv: View
    var cardioTimeTv: View
    var breakTimeTv: View

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_exercise_intensity, this);

        warmupTimeTv = findViewById<View>(R.id.warmupTime)
        fatBurningTimeTv = findViewById<View>(R.id.fatBurningTime)
        cardioTimeTv = findViewById<View>(R.id.cardioTime)
        breakTimeTv = findViewById<View>(R.id.breakTime)


    }

    fun setValue(warmupTime: Int, fatBurningTime: Int, cardioTime: Int, breakTime: Int) {
        val warmupTimeLP = LinearLayoutCompat.LayoutParams(0,  // 宽度设置为包含布局的权重比例
            LinearLayoutCompat.LayoutParams.WRAP_CONTENT, warmupTime.toFloat())
        warmupTimeTv.layoutParams = warmupTimeLP

        val fatBurningTimeLP = LinearLayoutCompat.LayoutParams(0, LinearLayoutCompat.LayoutParams.WRAP_CONTENT, fatBurningTime.toFloat())
        fatBurningTimeTv.layoutParams = fatBurningTimeLP
        val cardioTimeLP = LinearLayoutCompat.LayoutParams(0, LinearLayoutCompat.LayoutParams.WRAP_CONTENT, cardioTime.toFloat())
        cardioTimeTv.layoutParams = cardioTimeLP
        val breakTimeLP = LinearLayoutCompat.LayoutParams(0, LinearLayoutCompat.LayoutParams.WRAP_CONTENT, breakTime.toFloat())
        breakTimeTv.layoutParams = breakTimeLP
    }
}