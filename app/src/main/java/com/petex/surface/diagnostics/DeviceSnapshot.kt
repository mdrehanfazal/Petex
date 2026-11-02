package com.petex.surface.diagnostics

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Environment
import android.os.StatFs

/** Instantaneous readings only. No forecast, trend, health score or background monitoring. */
data class DeviceSnapshot(
    val capturedAt: Long,
    val batteryPercent: Int?,
    val batteryStatus: String?,
    val batteryTemperatureCelsius: Float?,
    val availableBytes: Long?,
    val totalBytes: Long?,
)

fun captureDeviceSnapshot(context: Context): DeviceSnapshot {
    val battery = runCatching {
        context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
    }.getOrNull()
    val level = battery?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
    val scale = battery?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
    val batteryPercent = if (level >= 0 && scale > 0 && level <= scale) {
        (level.toLong() * 100 / scale).toInt()
    } else null
    val batteryStatus = when (battery?.getIntExtra(BatteryManager.EXTRA_STATUS, -1)) {
        BatteryManager.BATTERY_STATUS_CHARGING -> "Charging"
        BatteryManager.BATTERY_STATUS_DISCHARGING -> "Discharging"
        BatteryManager.BATTERY_STATUS_NOT_CHARGING -> "Not charging"
        BatteryManager.BATTERY_STATUS_FULL -> "Full"
        else -> null
    }
    // Android reports battery temperature in tenths of a degree Celsius.
    val temperature = battery?.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, Int.MIN_VALUE)
        ?.takeIf { it != Int.MIN_VALUE }?.div(10f)
    val storage = runCatching { StatFs(Environment.getDataDirectory().path) }.getOrNull()
    return DeviceSnapshot(System.currentTimeMillis(), batteryPercent, batteryStatus, temperature,
        storage?.availableBytes, storage?.totalBytes)
}
