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
    fun openUrl(url: String) {
        try {
            when {
                url.isPlayStore() || url.isYoutube() -> {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    topActivityProvider.activity?.startActivity(intent)
                }

                else -> {
                    val intent = webpageIntent(url)
                    topActivityProvider.activity?.startActivity(intent)
                }
            }
        } catch (e: ActivityNotFoundException) {
            copyToClipboard(context, url)
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

    private fun String.isPlayStore(): Boolean {
        return this.startsWith("https://play.google.com")
    }
    private fun String.isYoutube(): Boolean {
        return this.startsWith("https://youtube.com") || this.startsWith("https://www.youtube.com") ||
                this.startsWith("https://youtu.be") || this.startsWith(("https://www.youtu.be"))
    }
}