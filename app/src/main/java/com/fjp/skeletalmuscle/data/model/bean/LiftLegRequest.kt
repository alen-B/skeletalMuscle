package com.fjp.skeletalmuscle.data.model.bean

/**
 *Author:Mr'x
 *Time:2024/12/1
 *Description:
 */
data class LiftLegRequest(
    val calorie: List<Calorie>,
    val cardiorespiratory_endurance: Int,
    val efficient_grease_burning: Int,
    val end_time: Int,
    val extreme_breakthrough: Int,
    val heart_lung_enhancement: Int,
    val heart_rate: List<HeartRate>,
    val record: List<Record>,
    val score: Int,
    val start_time: Int,
    val warm_up_activation: Int
)
