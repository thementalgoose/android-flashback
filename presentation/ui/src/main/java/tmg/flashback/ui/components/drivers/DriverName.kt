package tmg.flashback.ui.components.drivers

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.text.TextBody1

@Composable
fun DriverName(
    firstName: String,
    lastName: String,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier
        .semantics(mergeDescendants = true) {
            this.stateDescription = "$firstName $lastName"
            this.contentDescription = "$firstName $lastName"
        }
    ) {
        TextBody1(
            text = firstName,
            maxLines = 1
        )
        Spacer(Modifier.width(4.dp))
        TextBody1(
            text = lastName,
            maxLines = 1,
            bold = true
        )
    }
}

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        DriverName(firstName = "Alex", lastName = "Albon")
    }
}