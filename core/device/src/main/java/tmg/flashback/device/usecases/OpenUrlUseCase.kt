package tmg.flashback.device.usecases

import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.widget.Toast
import dagger.hilt.android.qualifiers.ApplicationContext
import tmg.flashback.strings.R.string
import tmg.flashback.device.ActivityProvider
import tmg.utilities.extensions.managerClipboard
import java.net.MalformedURLException
import javax.inject.Inject

class OpenUrlUseCase @Inject constructor(
    @ApplicationContext
    private val context: Context,
    private val topActivityProvider: ActivityProvider,
) {
    fun openUrl(url: String): Boolean {
        try {
            val intent = webpageIntent(url)
            topActivityProvider.activity?.startActivity(intent)
            return true
        } catch (e: ActivityNotFoundException) {
            copyToClipboard(context, url)
            return false
        }
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

    private fun webpageIntent(url: String): Intent? {
        val uri = try {
            Uri.parse(url)
        } catch (e: MalformedURLException) {
            return null
        }

        val browserSelectorIntent = Intent()
            .setAction(Intent.ACTION_VIEW)
            .addCategory(Intent.CATEGORY_BROWSABLE)
            .setData(Uri.parse("https:"))
        val targetIntent = Intent()
            .setAction(Intent.ACTION_VIEW)
            .addCategory(Intent.CATEGORY_BROWSABLE)
            .setData(uri)

        targetIntent.selector = browserSelectorIntent

        return targetIntent
    }
}