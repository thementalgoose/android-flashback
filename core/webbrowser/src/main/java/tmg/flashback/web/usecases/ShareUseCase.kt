package tmg.flashback.web.usecases

import android.content.Intent
import tmg.flashback.device.ActivityProvider
import javax.inject.Inject

class ShareUseCase @Inject constructor(
    private val activityProvider: ActivityProvider
) {
    fun shareUrl(url: String, title: String = "") {
        val sendIntent = Intent.createChooser(Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, url)
            if (title.isNotEmpty()) {
                putExtra(Intent.EXTRA_TITLE, title)
            }
            type = "text/plain"
        }, null)
        val shareIntent = Intent.createChooser(sendIntent, null)
        activityProvider.activity?.startActivity(shareIntent)
    }

    fun shareText(text: String) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        activityProvider.activity?.startActivity(shareIntent)
    }
}