package tmg.flashback.device.usecases

import android.content.Context
import android.content.Intent
import dagger.hilt.android.qualifiers.ApplicationContext
import tmg.flashback.strings.R
import tmg.flashback.device.ActivityProvider
import javax.inject.Inject

class OpenShareUseCase @Inject constructor(
    @ApplicationContext
    private val context: Context,
    private val topActivityProvider: ActivityProvider,
) {
    fun shareUrl(url: String, title: String = "") {
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, url)
            if (title.isNotEmpty()) {
                putExtra(Intent.EXTRA_TITLE, title)
            }
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(intent, context.getString(R.string.intent_chooser_share))
        topActivityProvider.activity?.startActivity(shareIntent)
    }

    fun shareText(text: String) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, context.getString(R.string.intent_chooser_share))
        topActivityProvider.activity?.startActivity(shareIntent)
    }
}