package com.petex.surface.motion

import androidx.compose.animation.core.spring

/** Shared motion definitions. E1 intentionally has no animated clock. */
object MotionTokens {
    const val MICRO_MS = 120
    const val STANDARD_MS = 240
    const val AMBIENT_MS = 420
    const val HERO_MS = 760

    val ui get() = spring<Float>(dampingRatio = 0.82f, stiffness = 420f)
    val organic get() = spring<Float>(dampingRatio = 0.62f, stiffness = 130f)
    val settle get() = spring<Float>(dampingRatio = 1.0f, stiffness = 300f)
}
