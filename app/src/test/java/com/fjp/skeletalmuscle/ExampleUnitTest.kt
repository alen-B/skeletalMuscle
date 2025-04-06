package com.fjp.skeletalmuscle

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ChestExpansionDetector {
    private var lastAngle: Double? = null
    private var currentPeak: Double? = null
    private var currentValley: Double? = null
    private var expansionCount = 0
    private val angleDifferences = mutableListOf<Double>()
    private var isAscending = true
    private var consecutiveChangeCount = 0
    private val MIN_CONSECUTIVE_CHANGES = 3

    fun processAngle(angle: Double) {
        if (lastAngle == null) {
            lastAngle = angle
            currentPeak = angle
            currentValley = angle
            return
        }

        val isNowAscending = angle > lastAngle!!

        if (isNowAscending != isAscending) {
            consecutiveChangeCount++
        } else {
            consecutiveChangeCount = 0
        }

        if (consecutiveChangeCount >= MIN_CONSECUTIVE_CHANGES) {
            if (isAscending) {
                // 从上升变为下降，确认波峰
                if (currentPeak!! - currentValley!! >= 40) {
                    expansionCount++
                    angleDifferences.add(currentPeak!! - currentValley!!)
                }
                currentValley = angle
            } else {
                // 从下降变为上升，确认波谷
                currentPeak = angle
            }
            isAscending = isNowAscending
            consecutiveChangeCount = 0
        }

        if (isAscending) {
            if (angle > currentPeak!!) {
                currentPeak = angle
            }
        } else {
            if (angle < currentValley!!) {
                currentValley = angle
            }
        }

        lastAngle = angle
    }

    fun getExpansionCount(): Int {
        return expansionCount
    }

    fun getAngleDifferences(): List<Double> {
        return angleDifferences
    }
}


class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val testDatasets = listOf(
            // 数据集 1
            listOf(
                20.0, 30.0, 50.0, 80.0, 120.0, 150.0, 120.0, 80.0, 50.0, 30.0,
                40.0, 60.0, 90.0, 130.0, 1580.0, 130.0, 90.0, 60.0, 40.0, 20.0,20.0, 30.0, 50.0, 80.0, 120.0, 150.0, 120.0, 80.0, 50.0, 30.0,
                40.0, 60.0, 90.0, 130.0, 150.0, 130.0, 90.0, 60.0, 40.0, 20.0,20.0, 30.0, 50.0, 80.0, 120.0, 150.0, 120.0, 80.0, 50.0, 30.0,
                40.0, 60.0, 90.0, 130.0, 148.0, 130.0, 90.0, 60.0, 40.0, 20.0,20.0, 30.0, 50.0, 80.0, 120.0, 150.0, 120.0, 80.0, 50.0, 30.0,
                40.0, 60.0, 90.0, 130.0, 130.0, 90.0, 60.0, 40.0, 20.0
            )

        )

        for ((index, dataset) in testDatasets.withIndex()) {
            val detector = ChestExpansionDetector()
            for (angle in dataset) {
                detector.processAngle(angle)
            }
            println("数据集 $index 扩胸运动次数: ${detector.getExpansionCount()}")
            println("数据集 $index 每次扩胸运动的角度差: ${detector.getAngleDifferences()}")
        }
    }
}