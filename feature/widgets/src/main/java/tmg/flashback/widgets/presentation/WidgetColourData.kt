package tmg.flashback.widgets.presentation

import android.content.Context
import android.os.Build
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat

fun getWidgetColourData(context: Context, showBackground: Boolean, isDarkMode: Boolean): WidgetConfigurationData {
    return WidgetConfigurationData(
        contentColour = when {
            showBackground && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && isDarkMode ->
                Color(ContextCompat.getColor(context, android.R.color.system_neutral1_50))
            showBackground && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ->
                Color(ContextCompat.getColor(context, android.R.color.system_neutral1_800))
            showBackground ->
                Color.White
            else ->
                Color.White
        },
        backgroundColor = when {
            showBackground && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && isDarkMode ->
                Color(ContextCompat.getColor(context, android.R.color.system_neutral1_900))
            showBackground && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ->
                Color(ContextCompat.getColor(context, android.R.color.system_neutral2_10))
            showBackground ->
                Color.DarkGray.copy(alpha = 0.5f)
            else ->
                Color.Transparent
        }
    )
}

data class WidgetConfigurationData(
    val contentColour: Color,
    val backgroundColor: Color
)