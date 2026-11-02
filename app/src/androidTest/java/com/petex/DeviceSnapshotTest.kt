package com.petex

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.petex.surface.diagnostics.captureDeviceSnapshot
import org.junit.Assert.assertNotNull
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
        assertNotNull(snapshot.availableBytes)
        assertNotNull(snapshot.totalBytes)
        assertTrue(snapshot.availableBytes!! >= 0)
        assertTrue(snapshot.totalBytes!! > 0)
        assertTrue(snapshot.availableBytes <= snapshot.totalBytes)
    }
}
