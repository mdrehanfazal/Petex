package com.petex.surface.orb

/** Visual states only: selecting a state does not start audio or perform an action. */
sealed interface OrbState {
    val label: String

    data object Dormant : OrbState { override val label = "Dormant" }
    data object Attending : OrbState { override val label = "Attending" }
    data object Listening : OrbState { override val label = "Listening" }
    data object Thinking : OrbState { override val label = "Thinking" }
    data object Speaking : OrbState { override val label = "Speaking" }
    data object Acting : OrbState { override val label = "Acting" }
    data object Alarmed : OrbState { override val label = "Alarmed" }

    companion object {
        val all: List<OrbState> = listOf(Dormant, Attending, Listening, Thinking, Speaking, Acting, Alarmed)
    }
}
