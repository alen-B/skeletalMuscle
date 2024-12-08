package com.fjp.skeletalmuscle.app.util

/**
 *阈值可能会根据后续测试情况调整
 * 起坐次数:
 * 正常(良好):≥25 次
 * 代表核心肌肉力量和耐力良好，符合健康标准。
 * 可疑减少(需要进一步评估):15-24次表示核心肌肉力量下降，但未达到严重程度，建议进一步评估或进行适度锻炼。
 * 肌肉减少(诊断明确):<15 次 表示核心肌肉力量严重不足，可能影响日常生活，需加强锻炼或进行干预。
 * 高抬腿次数:正常(良好):≥100次
 * 代表下肢力量和耐力良好，符合健康标准可疑减少(需要进一步评估):
 * 60-99 次表示下肢力量或耐力下降，但未达到肌肉减少症的严重标准。
 * 肌肉减少(诊断明确):<60 次
 * 表示下肢肌肉力量或耐力严重不足，可能影响日常活动能力。
 * 握力最大值
 * 正常(无肌肉减少症): 男性握力 >25 kg女性握力 >18 kg
 * 肌肉可疑减少症(需要进一步评估，例如测量肌肉质量或体能) 男性握力 20-25 kg 女性握力 15-18 kg
 *
 * 肌肉减少症(诊断明确):
 * 男性握力<20 kg
 * 女性握力<15 kg
 */
class AssessmentUtils {

    companion object {
        const val LegCountLevel01 = 100
        const val LegCountLevel02 = 60

        const val SitUpCountLevel01 = 25
        const val SitUpCountLevel02 = 15

        val ManGripLevel01 = 25
        val ManGripLevel02 = 20

        val WOManGripLevel01 = 18
        val WoManGripLevel02 = 15
        fun testResult(legCount: Int, sitUpCount: Int, grip: Int, isMan: Boolean): String {
            var legLevel = 0
            if (legCount >= LegCountLevel01) {//抬腿良好
                legLevel = 1
            } else if (legCount >= LegCountLevel02) {//耐力下降
                legLevel = 2
            } else {
                legLevel = 3
            }
            var sitUpLevel = 0
            if (sitUpCount >= SitUpCountLevel01) {//良好
                var sitUpLevel = 1
            } else if (legCount >= SitUpCountLevel02) {//耐力下降
                var sitUpLevel = 2
            } else {
                var sitUpLevel = 3
            }

            var gripLevel = 0
            if (isMan) {
                if (grip >= ManGripLevel01) {//良好
                    gripLevel = 1
                } else if (legCount >= ManGripLevel02) {//耐力下降
                    gripLevel = 2
                } else {
                    gripLevel = 3
                }
            } else {
                if (grip >= WOManGripLevel01) {//良好
                    gripLevel = 1
                } else if (legCount >= WoManGripLevel02) {//耐力下降
                    gripLevel = 2
                } else {
                    gripLevel = 3
                }
            }
            if(legLevel==3 && sitUpLevel==3 && gripLevel==3){
                return "肌肉减少-肌肉可疑减少症(需要进一步评估,例如测量肌肉质量或体能)"
            }
            if(legLevel==3){
                return "肌肉减少-核心肌肉力量严重不足，可能影响日常生活，需加强锻炼或进行干预。"
            }
            if(sitUpLevel==3){
                return "肌肉减少-下肢肌肉力量或耐力严重不足，可能影响日常活动能力。"
            }
            if(gripLevel==3){
                return "肌肉减少-需要进一步评估，例如测量肌肉质量或体能"
            }

            if(legLevel==2){
                return "可疑减少-表示下肢力量或耐力下降，但未达到肌肉减少症的严重标准。"
            }
            if(sitUpLevel==2){
                return "可疑减少-核心肌肉力量下降，但未达到严重程度，建议进一步评估或进行适度锻炼。"
            }
            if(gripLevel==2){
                return "可疑减少-需要进一步评估，例如测量肌肉质量或体能"
            }

            return "正常-代表核心肌肉力量和耐力良好，符合健康标准。"
        }
    }
}