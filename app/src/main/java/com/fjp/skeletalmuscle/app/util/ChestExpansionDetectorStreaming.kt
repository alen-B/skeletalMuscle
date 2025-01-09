package com.fjp.skeletalmuscle.app.util

class ChestExpansionDetectorStreaming {
    private val yawDataWindow: MutableList<Double>
    private var count = 0

    init {
        yawDataWindow = ArrayList(WINDOW_SIZE)
    }

    fun processYawData(yawValue: Double) {
        yawDataWindow.add(yawValue)
        if (yawDataWindow.size > WINDOW_SIZE) {
            yawDataWindow.removeAt(0)
        }
        if (yawDataWindow.size == WINDOW_SIZE) {
            val windowArray = yawDataWindow.stream().mapToDouble { obj: Double -> obj }.toArray()
            val filteredYaw = movingAverageFilter(windowArray, WINDOW_SIZE)
            val yawDiff = DoubleArray(filteredYaw.size - 1)
            for (i in yawDiff.indices) {
                yawDiff[i] = Math.abs(filteredYaw[i + 1] - filteredYaw[i])
            }
            for (i in 1 until yawDiff.size - 1) {
                if (yawDiff[i] > yawDiff[i - 1] && yawDiff[i] > yawDiff[i + 1] && yawDiff[i] > AMPLITUDE_THRESHOLD) {
                    count++
                }
            }
            val frequency = count / filteredYaw.size.toDouble()
            if (frequency > PERIOD_THRESHOLD) {
                println("检测到扩胸运动")
            }
        }
    }

    companion object {
        private const val WINDOW_SIZE = 5
        private const val AMPLITUDE_THRESHOLD = 5.0
        private const val PERIOD_THRESHOLD = 0.5
        private fun movingAverageFilter(data: DoubleArray, windowSize: Int): DoubleArray {
            val dataLength = data.size
            if (dataLength < windowSize) {
                return data
            }
            val filteredData = DoubleArray(dataLength - windowSize + 1)
            for (i in filteredData.indices) {
                var sum = 0.0
                for (j in 0 until windowSize) {
                    sum += data[i + j]
                }
                filteredData[i] = sum / windowSize
            }
            return filteredData
        }

        @JvmStatic
        fun main(args: Array<String>) {
            val detector = ChestExpansionDetectorStreaming()

            // 模拟实时获取数据
            val mockData = doubleArrayOf(170.22217, 172.71057, 171.70532, 169.02466, 166.8164, 169.83215, 179.0332, 174.71558, 171.50208, 176.1383, 176.48438, 175.29785, 178.19824, -177.94556, -170.00793, -164.69055, -161.04858, -158.35144, -157.71973, -151.47949, -143.1189, -144.97559, -144.47021, -142.66296, -147.34314, -145.79407, -141.87744, -142.62451, -145.51941, -146.18958, -150.63904, -150.5072, -148.50769, -158.3075, -155.5774, -152.95715, -153.33069, -152.9242, -150.08972, -146.74988, -149.79309, -148.92517, -151.30371, -154.02832, -151.29272, -155.29724, -155.60486, -153.1604, -154.07776, -151.78162, -148.10669, -150.62256, -156.15967, -159.79065, -161.28479, -160.8728, -159.80164, -165.17944, -162.94373, -160.27954, -161.26282, -157.91199, -154.18762, -154.33044, -159.55444, -162.44934, -163.62488, -160.03235, -167.80518, -170.66162, -168.55774, -168.70056, -167.33276, -163.39966, -163.82263, -164.03137, -165.04211, -165.96497, -165.55847, -164.37195, -169.5575, -168.25562, -167.547, -165.7727, -159.22485, -161.48804, -162.82837, -164.50378, -165.9375, -162.67456, -157.45056, -164.6576, -164.34448, -163.39417, -163.46008, -157.63184, -157.19788, -159.76868, -163.2019, -164.82239, -165.17395, -163.48206, -168.54675, -169.94751, -166.69556, -164.88281, -161.05957, -159.22485, -157.1759, -161.12549, -163.98193, -164.45984, -163.927, -161.75171, -170.45288, -170.1178, -169.43665, -170.6781, -170.57922, -176.7041, -176.68762, 170.12878)
            for (data in mockData) {
                detector.processYawData(data)
            }
        }
    }
}