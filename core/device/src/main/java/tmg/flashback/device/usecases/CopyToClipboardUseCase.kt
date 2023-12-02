package tmg.flashback.device.usecases

import android.content.ClipData
import android.content.Context
import android.os.Build
import android.widget.Toast
import dagger.hilt.android.qualifiers.ApplicationContext
import tmg.flashback.strings.R.string
import tmg.utilities.extensions.managerClipboard
import javax.inject.Inject

class CopyToClipboardUseCase @Inject constructor(
    @ApplicationContext
    private val context: Context
) {
    fun copyToClipboard(text: String) {
        copyToClipboard(context, text)
    }

    private fun copyToClipboard(context: Context, url: String): Boolean {
        val clipboardManager = context.managerClipboard ?: return false
        clipboardManager.setPrimaryClip(ClipData.newPlainText("", url))
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
            Toast.makeText(context, string.copy_to_clipboard, Toast.LENGTH_LONG)
                .show()
        }
        return true
    }
}