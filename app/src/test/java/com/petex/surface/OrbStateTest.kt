package com.petex.surface

import com.petex.surface.orb.OrbState
import com.petex.surface.theme.PetexColors
import androidx.compose.ui.graphics.toArgb
import org.junit.Assert.assertEquals
import org.junit.Test

class OrbStateTest {
    @Test fun allSevenStatesAreAvailableExactlyOnce() {
        assertEquals(listOf("Dormant", "Attending", "Listening", "Thinking", "Speaking", "Acting", "Alarmed"),
            OrbState.all.map { it.label })
        assertEquals(OrbState.all.size, OrbState.all.toSet().size)
    }

    @Test fun paletteMatchesTheExperienceSpec() {
        val colors = listOf(PetexColors.Void, PetexColors.Cyan, PetexColors.Violet, PetexColors.Amber,
            PetexColors.Mint, PetexColors.Ember, PetexColors.TextPrimary, PetexColors.TextDim)
        val expected = listOf(-16381940, -11546113, -5796870, -19372,
            -9508937, -42401, -1510920, -8615013)
        assertEquals(expected, colors.map { it.toArgb() })
    }
}
