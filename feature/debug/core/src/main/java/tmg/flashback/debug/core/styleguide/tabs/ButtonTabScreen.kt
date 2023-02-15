package tmg.flashback.debug.core.styleguide.tabs

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import tmg.flashback.debug.core.R
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.buttons.ButtonPrimary
import tmg.flashback.style.buttons.ButtonSecondary
import tmg.flashback.style.buttons.ButtonTertiary
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextBody2
import tmg.flashback.style.text.TextHeadline1

@Composable
internal fun ButtonTabScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = AppTheme.dimens.medium)
            .scrollable(rememberScrollState(), orientation = Orientation.Vertical),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.nsmall)
    ) {
        TextHeadline1(text = "Buttons")
        
        TextBody1(text = "Primary Button", modifier = Modifier.fillMaxWidth())
        TextBody2(text = "Primary button is used for a primary action on the page", modifier = Modifier.fillMaxWidth())
        ButtonPrimary(text = "Primary", onClick = { }, modifier = Modifier.fillMaxWidth())

        TextBody1(text = "Secondary Button", modifier = Modifier.fillMaxWidth())
        TextBody2(text = "Secondary button is used for a secondary action on the page or an action that is considered minor", modifier = Modifier.fillMaxWidth())
        ButtonSecondary(text = "Secondary", onClick = { }, modifier = Modifier.fillMaxWidth())

        TextBody1(text = "Tertiary Button", modifier = Modifier.fillMaxWidth())
        TextBody2(text = "Tertiary button is used for little tooltips or in a sequence of sub actions related to an item", modifier = Modifier.fillMaxWidth())
        ButtonTertiary(text = "Tertiary", onClick = { }, modifier = Modifier.fillMaxWidth())
        ButtonTertiary(text = "Icon", enabled = false, onClick = { }, icon = R.drawable.debug_list_debug, modifier = Modifier.fillMaxWidth())
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ButtonTertiary(text = "No icon", onClick = { }, narrow = true)
            ButtonTertiary(text = "Debug", onClick = { }, narrow = true, icon = R.drawable.debug_list_debug)
            ButtonTertiary(text = "Style Guide", onClick = { }, narrow = true, icon = R.drawable.debug_list_styleguide)
            ButtonTertiary(text = "Adverts", onClick = { }, narrow = true, icon = R.drawable.debug_list_adverts)
        }
    }
}

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        ButtonTabScreen()
    }
}