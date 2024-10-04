package tmg.flashback.ui.managers

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import dagger.hilt.android.qualifiers.ApplicationContext
import tmg.flashback.device.ActivityProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ToastManager @Inject constructor(
    @ApplicationContext
    private val applicationContext: Context,
    private val activityProvider: ActivityProvider
) {
    fun displayToast(@StringRes msg: Int, vararg formatArgs: Any) {
        val context = activityProvider.activity ?: applicationContext
        Toast.makeText(context, context.getString(msg, formatArgs), Toast.LENGTH_LONG).show()
    }
}