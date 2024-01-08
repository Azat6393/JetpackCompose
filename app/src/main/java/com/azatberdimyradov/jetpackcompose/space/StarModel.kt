package com.azatberdimyradov.canvarworkplace.space

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

class StarModel {

    companion object {
        const val MOTION_SPEED_SLOW = 0.00000000025f
        const val MOTION_SPEED_FAST = 0.00000000999f
        const val INITIAL_Z_COORD = -3f
    }

    val stars = mutableListOf<Star>()

    fun update(elapsedTime: Long, isTurbo: Boolean, speed: Float, starCount: Int) {
        for (i in 1..starCount) {
            stars.add(
                Star(
                    x = random(-1f, 1f),
                    y = random(-1f, 1f),
                    z = INITIAL_Z_COORD,
                    color = Color.White.toArgb()
                )
            )
        }
        stars.forEach { item ->
            item.z += elapsedTime * speed
        }
        val iterator = stars.iterator()
        while (iterator.hasNext()) {
            val star = iterator.next()
            if (star.z >= 0) {
                iterator.remove()
            }
        }
    }

    private fun random(from: Float, to: Float): Float {
        return (from + (to - from) * Math.random()).toFloat()
    }
}