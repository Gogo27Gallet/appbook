package com.example.util

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType

object HapticManager {

    var isHapticsEnabled: Boolean = true

    fun tick(haptic: HapticFeedback? = null, context: Context? = null) {
        if (!isHapticsEnabled) return
        haptic?.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        vibrate(context, 12, 60)
    }

    fun click(haptic: HapticFeedback? = null, context: Context? = null) {
        if (!isHapticsEnabled) return
        haptic?.performHapticFeedback(HapticFeedbackType.LongPress)
        vibrate(context, 25, 120)
    }

    fun success(haptic: HapticFeedback? = null, context: Context? = null) {
        if (!isHapticsEnabled) return
        haptic?.performHapticFeedback(HapticFeedbackType.LongPress)
        vibratePattern(context, longArrayOf(0, 30, 60, 45), intArrayOf(0, 100, 0, 180))
    }

    fun heavy(haptic: HapticFeedback? = null, context: Context? = null) {
        if (!isHapticsEnabled) return
        haptic?.performHapticFeedback(HapticFeedbackType.LongPress)
        vibrate(context, 45, 200)
    }

    private fun getVibrator(context: Context?): Vibrator? {
        if (context == null) return null
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vm = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vm?.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            }
        } catch (_: Exception) {
            null
        }
    }

    private fun vibrate(context: Context?, durationMs: Long, amplitude: Int) {
        val vibrator = getVibrator(context) ?: return
        try {
            if (vibrator.hasVibrator()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val clampedAmp = amplitude.coerceIn(1, 255)
                    vibrator.vibrate(VibrationEffect.createOneShot(durationMs, clampedAmp))
                } else {
                    @Suppress("DEPRECATION")
                    vibrator.vibrate(durationMs)
                }
            }
        } catch (_: Exception) {
        }
    }

    private fun vibratePattern(context: Context?, timings: LongArray, amplitudes: IntArray) {
        val vibrator = getVibrator(context) ?: return
        try {
            if (vibrator.hasVibrator()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createWaveform(timings, amplitudes, -1))
                } else {
                    @Suppress("DEPRECATION")
                    vibrator.vibrate(timings, -1)
                }
            }
        } catch (_: Exception) {
        }
    }
}
