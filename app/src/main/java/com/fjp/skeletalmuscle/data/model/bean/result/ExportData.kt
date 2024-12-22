package com.fjp.skeletalmuscle.data.model.bean.result

/**
 *Author:Mr'x
 *Time:2024/12/21
 *Description:
 */
data class ExportData(
    val avg_rate_value: Int,
    val calorie_total: Int,
    val score: Int,
    val sport_dumbbell: ExportSportDumbbell,
    val sport_flat_support: ExportSportFlatSupport,
    val sport_lift_leg: ExportSportLiftLeg,
    val sport_time: Int
)

data class ExportSportDumbbell(
    val expand_chest_times: Int,
    val score: Int,
    val sport_time: Int,
    val up_times: Int,
    val weight: Int
)

data class ExportSportFlatSupport(
    val score: Int,
    val sport_time: Int,
    val sum_calorie: Int
)

data class ExportSportLiftLeg(
    val avg_left_degree: Int,
    val avg_rate_value: Int,
    val avg_right_degree: Int,
    val cardiorespiratory_endurance: Double,
    val efficient_grease_burning: Int,
    val extreme_breakthrough: Int,
    val heart_lung_enhancement: Int,
    val left_sport_amount: Int,
    val left_times: Int,
    val max_rate_value: Int,
    val min_rate_value: Int,
    val right_sport_amount: Int,
    val right_times: Int,
    val score: Int,
    val sport_time: Int,
    val sum_calorie: Int,
    val warm_up_activation: Int
)