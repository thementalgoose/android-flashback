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
annotation class PreviewDevices

@Preview(
    showBackground = true,
    device = "spec:shape=Normal,width=1200,height=800,unit=dp,dpi=480",
    name = "Tablet"
)
annotation class PreviewTablet

@Preview(
    showBackground = true,
    device = "spec:shape=Normal,width=480,height=720,unit=dp,dpi=480",
    name = "Phone"
)
annotation class PreviewPhone

@Preview(
    showBackground = true,
    device = "spec:shape=Normal,width=673,height=841,unit=dp,dpi=480",
    name = "Foldable"
)
annotation class PreviewFoldable