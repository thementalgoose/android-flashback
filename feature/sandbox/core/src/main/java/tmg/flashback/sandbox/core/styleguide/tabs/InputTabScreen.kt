package tmg.flashback.sandbox.core.styleguide.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import tmg.flashback.sandbox.core.R
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.input.InputPrimary
import tmg.flashback.style.input.InputSelection
import tmg.flashback.style.input.InputSwitch
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextBody2
import tmg.flashback.style.text.TextHeadline1

@Composable
internal fun InputTabScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = AppTheme.dimens.medium)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.nsmall)
    ) {
        TextHeadline1(text = "Inputs")

        TextBody1(text = "Input Primary", modifier = Modifier.fillMaxWidth())
        TextBody2(text = "Input primary is used for when text input is needed from the user, like in search", modifier = Modifier.fillMaxWidth())
        val primary = remember { mutableStateOf(TextFieldValue()) }
        InputPrimary(
            text = primary,
            placeholder = "placeholder"
        )

        TextBody1(text = "Input Selection", modifier = Modifier.fillMaxWidth())
        TextBody2(text = "Input selection is used when the user needs to select one of a list of possible items (ie. theme selection)", modifier = Modifier.fillMaxWidth())
        val selection = remember { mutableStateOf("Input 1") }
        InputSelection(
            label = "Input 1",
            icon = R.drawable.debug_list_adverts,
            isSelected = selection.value == "Input 1",
            itemClicked = {
                selection.value = "Input 1"
            }
        )
        InputSelection(
            label = "Input 2",
            icon = R.drawable.debug_list_styleguide,
            isSelected = selection.value == "Input 2",
            itemClicked = {
                selection.value = "Input 2"
            }
        )

        TextBody1(text = "Input Switch", modifier = Modifier.fillMaxWidth())
        TextBody2(text = "Input switch for when we need checkbox functionality", modifier = Modifier.fillMaxWidth())
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
            InputSwitch(isChecked = true)
            InputSwitch(isChecked = false)
        }
    }
}

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        InputTabScreen()
    }
}