package com.fjp.skeletalmuscle.viewmodel.state

import androidx.databinding.ObservableField
import com.fjp.skeletalmuscle.app.base.SMBaseViewModel

/**
 *Author:Mr'x
 *Time:2024/10/19
 *Description:
 */
class DiseaseViewModel : SMBaseViewModel() {
    val other = ObservableField("")
    val dataArr = arrayListOf("无", "心脏病", "肺病", "骨关节疾病", "其他")
    val heartDisease = arrayListOf("冠心病", "安放过\n支架", "搭桥", "心肌病", "心肌炎", "先天性\n心脏病", "其他心脏\n手术术后")
    val lungDisease = arrayListOf("肺间质\n纤维化", "慢性阻塞性\n肺疾病", "支气管\n哮喘", "支气管\n扩张", "肺结核", "其他原因曾经\n出现呼吸衰竭")
    val jointDisease = arrayListOf("股骨头置\n换术后", "严重膝关节\n骨关节炎")


}
