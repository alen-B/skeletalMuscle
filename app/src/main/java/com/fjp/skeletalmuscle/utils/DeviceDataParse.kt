package com.fjp.skeletalmuscle.utils

import android.graphics.Color
import android.util.Log
import java.util.Locale

/**
 *Author:Mr'x
 *Time:2024/11/4
 *Description:
 */
object DeviceDataParse {
     fun handleNotifyData(data: ByteArray?) {
        Log.d("TEST", "Heart Rate: ccc, Blood Oxygen: ")
        if (data != null && data.size > 0) {
            // 通常第一个字节是错误码，如果数据正确，之后是实际的数据
            // 根据您提供的协议，我们期待至少29个字节（1个错误码 + 28个数据字节）
            if (data[0].toInt() == 0x00 && data.size >= 29) {
                // 心率在返回数据的第23个字节
                val heartRate = data[22].toInt() and 0xFF // 将byte转换为无符号int
                // 血氧在返回数据的第24个字节
                val bloodOxygen = data[23].toInt() and 0xFF // 将byte转换为无符号int
                Log.d("handleNotifyData", "Heart Rate: $heartRate, Blood Oxygen: $bloodOxygen")
            } else {
                Log.e("handleNotifyData", "Received data with unexpected format or error code.")
            }
        } else {
            Log.e("handleNotifyData", "Received empty or null data.")
        }
    }

    fun bytesToHex(bytes: ByteArray): String {
        val hexBuilder = StringBuilder()
        for (b in bytes) {
            hexBuilder.append(String.format("%02X ", b))
        }
        return hexBuilder.toString().trim { it <= ' ' } // 移除最后一个空格
    }

    fun parseData2HeartRate(data: ByteArray): Int {
        // 确保数据的长度至少为7个字节，因为关注的值在第7个位置
        if (data.size < 7) {
            return -1 // 数据不完整
        }

        // 确保前6个字节是你的特定标志位
        return if (data[0] == 0xDA.toByte() && data[1] == 0xC2.toByte() && data[2].toInt() == 0x03 && data[3].toInt() == 0x00 && data[4].toInt() == 0x00 && data[5].toInt() == 0x63) {
            // 提取第7个字节，这是我们关注的值
            // & 0xFF 是为了将signed byte转换为unsigned int representation
            data[6].toInt() and 0xFF
        } else -1
        // 数据不匹配
    }

    fun parseData2Pitch(data:ByteArray):Float {
        if (data.size >= 20) {
            val pitch: Float = Math.abs(twoBytesToFloat(data[14], data[15]) + 100)
            val roll: Float = Math.abs(twoBytesToFloat(data[16], data[17]))
            val yaw: Float = twoBytesToFloat(data[18], data[19])
            // 检查pitch是否大于100度
            return pitch
        }
        return 0f
    }
    fun parseData2Roll(data:ByteArray):Float {
        if (data.size >= 20) {
            val roll: Float = Math.abs(twoBytesToFloat(data[16], data[17]))
            // 检查pitch是否大于100度
            return roll
        }
        return 0f
    }
    fun parseData2Yaw(data:ByteArray):Float {
        if (data.size >= 20) {
            val yaw: Float = twoBytesToFloat(data[18], data[19])
            return yaw
        }
        return 0f
    }

    // 根据角度计算等级的方法
    private fun calculateLevelFromAngle(angle: Float): Int {
        // 假设角度从0到90度，分成7个等级
        // 你可以根据实际需求调整这个方法
        val maxAngle = 90
        val levels = 7
        // 计算每个等级的角度
        val anglePerLevel = (maxAngle / levels).toFloat()
        // 计算当前角度对应的等级
        val level = levels - Math.ceil((angle / anglePerLevel).toDouble()).toInt() // 反转等级
        // 确保返回的level不会超出triangleViews的大小
        return Math.min(level, 7)
    }

    private fun twoBytesToFloat(b1: Byte, b2: Byte): Float {
        // 根据文档中的公式转换两个字节的数据
        return (b2.toInt() shl 8 or (b1.toInt() and 0xFF)) / 32768.0f * 180.0f // 示例转换公式，根据实际情况调整
    }

}