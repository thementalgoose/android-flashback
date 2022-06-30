package tmg.flashback.style.annotations

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    showBackground = true,
    name = "Light",
    backgroundColor = 4294506744,
    uiMode = UI_MODE_NIGHT_NO,
    device = Devices.PIXEL_4
)
@Preview(
    showBackground = true,
    name = "Dark",
    backgroundColor = 4279769112,
    uiMode = UI_MODE_NIGHT_YES,
    device = Devices.PIXEL_4
)
annotation class PreviewTheme