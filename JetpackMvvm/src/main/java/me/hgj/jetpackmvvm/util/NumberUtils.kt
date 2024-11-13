package me.hgj.jetpackmvvm.util

/**
 *Author:Mr'x
 *Time:2024/11/12
 *Description:
 */
object  NumberUtils {
    fun extractNumbers(input: String): List<Int> {
        val numberRegex = "\\d+".toRegex()
        return numberRegex.findAll(input).map { it.value.toInt() }.toList()
    }
}