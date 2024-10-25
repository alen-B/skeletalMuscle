package com.fjp.skeletalmuscle.viewmodel.state

import androidx.databinding.ObservableField
import com.fjp.skeletalmuscle.app.base.SMBaseViewModel
import java.util.ArrayList


class SportsRecordViewModel : SMBaseViewModel() {
    val legSportsDate = arrayListOf<String>("1","2","3","4","5","6")
    val calendarTitle = ObservableField("")
    val legSportsTime = ObservableField("10月23日 18：30")
    val dumbbellSportsTime = ObservableField("10月23日 18：30")
    val plankSportsTime = ObservableField("10月23日 18：30")


}