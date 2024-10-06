package tmg.flashback.presentation.settings.licenses

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.util.withJson
import tmg.flashback.R

@Composable
fun LicenseScreen() {
    val context = LocalContext.current

    val result = Libs.Builder().withJson(context, tmg.flashback.R.raw.aboutlibraries).build()
    result.licenses.forEach {

    }
}