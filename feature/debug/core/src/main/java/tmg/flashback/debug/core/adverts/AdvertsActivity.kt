package tmg.flashback.debug.core.adverts

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import tmg.flashback.ads.components.NativeBanner
import tmg.flashback.style.AppTheme
import tmg.flashback.style.text.TextTitle

@AndroidEntryPoint
class AdvertsActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme {
                Column {
                    TextTitle(
                        text = "Native Banner",
                        modifier = Modifier.padding(vertical = AppTheme.dimens.medium)
                    )
                    NativeBanner()
                    TextTitle(
                        text = "Native Banner (offset)",
                        modifier = Modifier.padding(vertical = AppTheme.dimens.medium)
                    )
                    NativeBanner(badgeOffset = true)
                    TextTitle(
                        text = "Config",
                        modifier = Modifier.padding(vertical = AppTheme.dimens.medium)
                    )
                }
            }
        }
    }
}