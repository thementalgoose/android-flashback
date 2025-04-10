package tmg.flashback.ui.annotations

import androidx.compose.ui.tooling.preview.Preview

@Preview(
    showBackground = true,
    device = "spec:width=480dp,height=720dp,dpi=480",
    name = "Phone"
)
@Preview(
    showBackground = true,
    device = "spec:width=1200dp,height=800dp,dpi=480",
    name = "Tablet"
)
annotation class PreviewDevices

@Preview(
    showBackground = true,
    device = "spec:width=1200dp,height=800dp,dpi=480",
    name = "Tablet"
)
annotation class PreviewTablet

@Preview(
    showBackground = true,
    device = "spec:width=480dp,height=720dp,dpi=480",
    name = "Phone"
)
annotation class PreviewPhone

@Preview(
    showBackground = true,
    device = "spec:width=480dp,height=720dp,dpi=480",
    name = "Foldable"
)
annotation class PreviewFoldable