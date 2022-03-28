package tmg.flashback.ui.components.number

//import androidx.compose.material.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.font.Font
//import androidx.compose.ui.text.font.FontFamily
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.tooling.preview.PreviewParameter
//import androidx.compose.ui.tooling.preview.PreviewParameterProvider
//import androidx.compose.ui.unit.sp
//import tmg.flashback.style.AppTheme
//import tmg.flashback.style.AppThemePreview
//
//@Composable
//fun DriverNumber(
//    number: String,
//    modifier: Modifier = Modifier,
//    highlightNumber: Color = AppTheme.colors.backgroundSecondary,
//) {
//    Text(
//        modifier = modifier,
//        text = number,
//        style = AppTheme.typography.block,
//        color = highlightNumber
//    )
//}
//
//@Preview
//@Composable
//private fun PreviewLight(
//    @PreviewParameter(TeamColourProvider::class) teamExample: Pair<String, Color>
//) {
//    val (driverNumber, color) = teamExample
//    AppThemePreview(isLight = true) {
//        DriverNumber(
//            number = driverNumber,
//            highlightNumber = color
//        )
//    }
//}
//
//@Preview
//@Composable
//private fun PreviewDark(
//    @PreviewParameter(TeamColourProvider::class) teamExample: Pair<String, Color>
//) {
//    val (driverNumber, color) = teamExample
//    AppThemePreview(isLight = false) {
//        DriverNumber(
//            number = driverNumber,
//            highlightNumber = color
//        )
//    }
//}
//
//internal class TeamColourProvider: PreviewParameterProvider<Pair<String, Color>> {
//    override val values: Sequence<Pair<String, Color>> = sequenceOf(
//        Pair("77", Color(0xFF900000)), // Alfa
////        Color(0xFF2B4562), // Alpha Tauri
////        Color(0xFF0090FF), // Alpine
////        Color(0xFF006F62), // Aston Martin
////        Color(0xFFDC0000), // Ferrari
////        Color(0xFFd9d9d9), // Haas
////        Color(0xFFFF8700), // McLaren
////        Color(0xFF00D2BE), // Mercedes
////        Color(0xFF0600EF), // Red Bull
////        Color(0xFF005AFF), // Williams
//    )
//}