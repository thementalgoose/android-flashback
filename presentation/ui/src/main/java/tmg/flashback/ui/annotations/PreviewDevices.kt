package tmg.flashback.ui.annotations

import androidx.compose.ui.tooling.preview.Preview

@Preview(
    showBackground = true,
    device = "spec:shape=Normal,width=480,height=720,unit=dp,dpi=480",
    name = "Phone"
)
@Preview(
    showBackground = true,
    device = "spec:shape=Normal,width=1200,height=800,unit=dp,dpi=480",
    name = "Tablet"
)
@Preview(
    showBackground = true,
    device = "spec:shape=Normal,width=673,height=841,unit=dp,dpi=480",
    name = "Foldable"
)
annotation class PreviewDevices(){}
