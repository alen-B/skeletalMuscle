package com.fjp.skeletalmuscle.viewmodel.state

import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.base.SMBaseViewModel
import me.hgj.jetpackmvvm.base.appContext

/**
 *Author:Mr'x
 *Time:2024/11/30
 *Description:
 */
class SportsAssessmentTypeSelectedViewModel:SMBaseViewModel(){
    val dataArr = mutableListOf<String>(appContext.getString(R.string.sports_assessment_high_leg),
        appContext.getString(R.string.sports_assessment_type_weight_waistline),
        appContext.getString(R.string.sports_assessment_sit_up),
        appContext.getString(R.string.sports_assessment_grip))
}