package tmg.flashback.device.repository

import android.content.Context
import android.provider.Settings
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AccessibilityRepository @Inject constructor(
    @ApplicationContext
    private val applicationContext: Context
) {
    val isAnimationsEnabled: Boolean
        get() = animatorValue != 0f

    private val animatorValue: Float
        get() = Settings.Global.getFloat(
            applicationContext.contentResolver,
            Settings.Global.ANIMATOR_DURATION_SCALE,
            0f
        )
}