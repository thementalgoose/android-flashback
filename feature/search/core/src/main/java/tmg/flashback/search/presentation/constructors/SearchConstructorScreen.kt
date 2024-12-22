package tmg.flashback.search.presentation.constructors

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import tmg.flashback.formula1.model.Constructor
import tmg.flashback.style.AppTheme
import tmg.flashback.style.text.TextBody2

fun LazyListScope.Constructors(
    constructorClicked: (Constructor) -> Unit,
    constructors: List<Constructor>,
) {
    items(constructors, key = { it.id }) {
        ConstructorRecord(
            constructorClicked = constructorClicked,
            constructor = it,
            modifier = Modifier.padding(
                horizontal = AppTheme.dimens.medium,
                vertical = AppTheme.dimens.small
            )
        )
    }
}

@Composable
private fun ConstructorRecord(
    constructorClicked: (Constructor) -> Unit,
    constructor: Constructor,
    modifier: Modifier = Modifier,
) {
    TextBody2("Constructor ${constructor.name}")
}