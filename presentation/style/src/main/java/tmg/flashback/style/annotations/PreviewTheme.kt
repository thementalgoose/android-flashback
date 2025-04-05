package tmg.flashback.style.annotations

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.content.res.Configuration.UI_MODE_TYPE_NORMAL
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    showBackground = true,
    name = "Light",
    backgroundColor = 4294506744
)
@Preview(
    showBackground = true,
    name = "Dark",
    backgroundColor = 4279769112,
    uiMode = UI_MODE_NIGHT_YES or UI_MODE_TYPE_NORMAL
)
annotation class PreviewTheme

@Preview(
    showBackground = true,
    name = "Light",
    backgroundColor = 4294506744,
    device = Devices.NEXUS_10
)
@Preview(
    showBackground = true,
    name = "Dark",
    backgroundColor = 4279769112,
    uiMode = UI_MODE_NIGHT_YES or UI_MODE_TYPE_NORMAL,
    device = Devices.NEXUS_10
)
annotation class PreviewThemeExpanded
