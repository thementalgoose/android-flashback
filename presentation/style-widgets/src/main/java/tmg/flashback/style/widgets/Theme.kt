package tmg.flashback.style.widgets

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.LocalContext
import androidx.glance.background
import androidx.glance.layout.Column
import androidx.glance.layout.padding
import com.jakewharton.threetenabp.AndroidThreeTen

@Composable
fun WidgetTheme(
    content: @Composable () -> Unit
) {
    GlanceTheme(colors = GlanceTheme.colors) {
        content()
    }
}

@Composable
fun WidgetThemePreview(
    isLight: Boolean = true,
    content: @Composable () -> Unit,
) {
    AndroidThreeTen.init(LocalContext.current)
    val backgroundColor = when (isLight) {
        true -> Color.Black
        false -> Color.White
    }
    Column(modifier = GlanceModifier.background(backgroundColor)) {
        WidgetTheme(content)
    }
}