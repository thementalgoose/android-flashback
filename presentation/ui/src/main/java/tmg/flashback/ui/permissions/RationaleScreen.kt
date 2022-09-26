package tmg.flashback.ui.permissions

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import tmg.flashback.style.AppTheme
import tmg.flashback.style.buttons.ButtonPrimary
import tmg.flashback.style.buttons.ButtonTertiary
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextHeadline2
import tmg.flashback.ui.R

@Composable
internal fun RationaleScreen(
    type: RationaleType,
    confirmClicked: () -> Unit,
    cancelClicked: () -> Unit,
) {
    Column(modifier = Modifier.padding(
        horizontal = AppTheme.dimens.medium,
        vertical = AppTheme.dimens.medium
    )) {

        type.raw?.let { raw ->
            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(raw))
            val progress by animateLottieCompositionAsState(composition, iterations = Int.MAX_VALUE)
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(AppTheme.dimens.medium)
            ) {
                LottieAnimation(
                    modifier = Modifier
                        .size(150.dp)
                        .align(Alignment.Center),
                    composition = composition,
                    progress = { progress },
                )
            }
        }

        TextHeadline2(text = stringResource(id = type.title))
        TextBody1(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = AppTheme.dimens.medium),
            text = stringResource(id = type.description)
        )
        Row {
            ButtonTertiary(
                modifier = Modifier.weight(1f),
                narrow = false,
                text = stringResource(id = R.string.permissions_rationale_cancel),
                onClick = cancelClicked
            )
            Spacer(Modifier.width(AppTheme.dimens.medium))
            ButtonPrimary(
                modifier = Modifier.weight(1f),
                text = stringResource(id = R.string.permissions_rationale_confirm),
                onClick = confirmClicked
            )
        }
    }
}