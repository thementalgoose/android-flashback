package tmg.flashback.ui.utils

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
internal val fakeExpandedWindowSizeClass: WindowSizeClass = WindowSizeClass.calculateFromSize(DpSize(900.dp, 600.dp))

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
internal val fakeMediumWindowSizeClass: WindowSizeClass = WindowSizeClass.calculateFromSize(DpSize(700.dp, 600.dp))

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
internal val fakeCompactWindowSizeClass: WindowSizeClass = WindowSizeClass.calculateFromSize(DpSize(400.dp, 600.dp))