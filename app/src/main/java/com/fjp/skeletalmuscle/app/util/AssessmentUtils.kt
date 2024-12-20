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
        private const val LegCountLevel01 = 100
        private const val LegCountLevel02 = 60

        private const val SitUpCountLevel01 = 25
        private const val SitUpCountLevel02 = 15

        private const val ManGripLevel01 = 25
        private const val ManGripLevel02 = 20

        private const val WOManGripLevel01 = 18
        private const val WoManGripLevel02 = 15
        fun testResult(legCount: Int, sitUpCount: Int, grip: Int, isMan: Boolean): String {
            val levels = arrayListOf(0, 0, 0)
            if (legCount >= LegCountLevel01) {//抬腿良好
                levels[0] = 1
            } else if (legCount >= LegCountLevel02) {//耐力下降
                levels[0] = 2
            } else {
                levels[0] = 3
            }
            if (sitUpCount >= SitUpCountLevel01) {//良好
                levels[1] = 1
            } else if (legCount >= SitUpCountLevel02) {//耐力下降
                levels[1] = 2
            } else {
                levels[1] = 3
            }

            if (isMan) {
                if (grip >= ManGripLevel01) {//良好
                    levels[2] = 1
                } else if (legCount >= ManGripLevel02) {//耐力下降
                    levels[2] = 2
                } else {
                    levels[2] = 3
                }
            } else {
                if (grip >= WOManGripLevel01) {//良好
                    levels[2] = 1
                } else if (legCount >= WoManGripLevel02) {//耐力下降
                    levels[2] = 2
                } else {
                    levels[2] = 3
                }
            }
            return if (levels.contains(3)) {
                "肌肉减少-根据测评结果，您处于肌肉减少状态，请寻求专业帮助。"
            } else if (levels.contains(2)) {
                "肌肉可疑减少-根据测评结果，您的肌肉可疑减少，请适当增加锻炼量。"
            } else {
                "肌肉正常：根据测评结果，您的锻炼效果显著，请继续保持哦！"
            }

        }
    }
}