package com.petex

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.petex.surface.diagnostics.captureDeviceSnapshot
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DeviceSnapshotTest {
    @Test fun readingsAreBoundedAndStorageIsMeasured() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val snapshot = captureDeviceSnapshot(context)
        assertTrue(snapshot.capturedAt > 0)
        snapshot.batteryPercent?.let { assertTrue(it in 0..100) }
        val available = requireNotNull(snapshot.availableBytes)
        val total = requireNotNull(snapshot.totalBytes)
        assertTrue(available >= 0)
        assertTrue(total > 0)
        assertTrue(available <= total)
    }
}
