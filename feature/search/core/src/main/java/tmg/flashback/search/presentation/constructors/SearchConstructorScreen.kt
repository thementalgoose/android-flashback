package tmg.flashback.search.presentation.constructors

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import tmg.flashback.formula1.model.Constructor
import tmg.flashback.style.AppTheme
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextBody2
import tmg.flashback.ui.components.flag.Flag

private val constructorIconSize: Dp = 48.dp

fun LazyListScope.Constructors(
    constructorClicked: (Constructor) -> Unit,
    constructors: List<Constructor>,
) {
    items(constructors, key = { it.id }) {
        ConstructorRecord(
            constructorClicked = constructorClicked,
            constructor = it,
            modifier = Modifier
        )
    }
}

fun LazyGridScope.Constructors(
    constructorClicked: (Constructor) -> Unit,
    constructors: List<Constructor>,
) {
    items(constructors, key = { it.id }) {
        ConstructorRecord(
            constructorClicked = constructorClicked,
            constructor = it,
            modifier = Modifier
        )
    }
}

@Composable
private fun ConstructorRecord(
    constructorClicked: (Constructor) -> Unit,
    constructor: Constructor,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier
        .clickable(
            onClick = { constructorClicked(constructor) }
        )
        .padding(
            horizontal = AppTheme.dimens.medium,
            vertical = AppTheme.dimens.small
        )
    ) {
        Box(modifier = Modifier
            .size(constructorIconSize)
            .clip(RoundedCornerShape(4.dp))
            .background(constructor.colour)
        ) {
            AsyncImage(
                model = constructor.photoUrl,
                contentDescription = null
            )
        }
        Column(
            modifier = Modifier.padding(
                start = AppTheme.dimens.medium,
                top = AppTheme.dimens.xsmall
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextBody1(
                text = constructor.name,
                modifier = Modifier.fillMaxWidth(),
                bold = true,
            )
            Row(modifier = Modifier.fillMaxWidth()) {
                Flag(
                    iso = constructor.nationalityISO,
                    nationality = constructor.nationality,
                    modifier = Modifier.size(16.dp),
                )
                Spacer(Modifier.width(8.dp))
                TextBody2(
                    text = constructor.nationality,
                    modifier = Modifier
                )
            }
        }
    }
}