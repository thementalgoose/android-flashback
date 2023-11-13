package tmg.flashback.device.usecases

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import tmg.flashback.device.ActivityProvider
import javax.inject.Inject

class OpenSendEmailUseCase @Inject constructor(
    private val topActivityProvider: ActivityProvider,
    private val copyToClipboardUseCase: CopyToClipboardUseCase
) {

    fun sendEmail(email: String, subject: String = "") {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, email)
            putExtra(Intent.EXTRA_SUBJECT, subject)
        }
        try {
            topActivityProvider.activity?.startActivity(intent)
        } catch (ex: ActivityNotFoundException) {
            copyToClipboardUseCase.copyToClipboard(email)
        }
    }
}