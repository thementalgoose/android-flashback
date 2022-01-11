package tmg.flashback.style

import androidx.compose.material.Typography
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val montserrat = FontFamily(
    Font(R.font.montserrat, weight = FontWeight.Normal),
    Font(R.font.montserrat_semibold, weight = FontWeight.SemiBold)
)

data class AppTypography(
    val h1: TextStyle = TextStyle(
        fontFamily = montserrat,
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp
    ),
    val h2: TextStyle = TextStyle(
        fontFamily = montserrat,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp
    ),
    val title: TextStyle = TextStyle(
        fontSize = 16.sp
    ),
    val section: TextStyle = TextStyle(
        fontFamily = montserrat,
        fontWeight = FontWeight.SemiBold,
        fontSize = 13.sp
    ),
    val body1: TextStyle = TextStyle(
        fontSize = 14.sp
    ),
    val body2: TextStyle = TextStyle(
        fontSize = 12.sp
    ),
    val caption: TextStyle = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 11.sp
    )
)