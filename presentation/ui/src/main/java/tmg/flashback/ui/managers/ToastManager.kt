package tmg.flashback.ui.managers

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import dagger.hilt.android.qualifiers.ApplicationContext
import tmg.flashback.navigation.ActivityProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ToastManager @Inject constructor(
    @ApplicationContext
    private val applicationContext: Context,
    private val activityProvider: tmg.flashback.navigation.ActivityProvider
) {
    fun displayToast(@StringRes msg: Int) {
        Toast.makeText(activityProvider.activity ?: applicationContext, msg, Toast.LENGTH_LONG).show()
    }
}