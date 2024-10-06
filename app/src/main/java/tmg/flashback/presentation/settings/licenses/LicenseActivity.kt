package tmg.flashback.presentation.settings.licenses

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Scaffold
import androidx.core.content.ContextCompat
import tmg.flashback.R
import tmg.flashback.style.AppTheme
import tmg.utilities.extensions.setStatusBarColor

class LicenseActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Scaffold(content = {
                    LicenseScreen()
                })
            }
        }

        setStatusBarColor(ContextCompat.getColor(this, R.color.splash_screen))
    }
}