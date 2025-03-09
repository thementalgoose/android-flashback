package tmg.flashback.style

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val montserrat = FontFamily(
    Font(R.font.montserrat, weight = FontWeight.Normal),
    Font(R.font.montserrat_semibold, weight = FontWeight.SemiBold)
)


private val closeness = FontFamily(
    Font(R.font.closeness, weight = FontWeight.Bold)
)

data class AppTypography(
    val h1: TextStyle = TextStyle(
        fontFamily = montserrat,
        fontWeight = FontWeight.SemiBold,
        fontSize = 36.sp
    ),
    val h2: TextStyle = TextStyle(
        fontFamily = montserrat,
        fontWeight = FontWeight.SemiBold,
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
    ),

    val block: TextStyle = TextStyle(
        fontFamily = closeness,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    )
)