package tmg.flashback.weekend.ui.shared

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import kotlin.math.absoluteValue

private const val alpha = 0.3f

@Composable
fun RelativePosition(
    delta: Int,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        when {
            delta > 0 -> {
                BackgroundDown()
                Position(
                    delta = delta,
                    color = AppTheme.colors.f1DeltaPositive,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            delta < 0 -> {
                BackgroundUp()
                Position(
                    delta = delta,
                    color = AppTheme.colors.f1DeltaNegative,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            else -> {
                BackgroundNeutral()
                Position(
                    delta = delta,
                    color = AppTheme.colors.f1DeltaNeutral,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

private fun getUpwardPath(x: Float, y: Float, width: Float, height: Float) = Path()
    .apply {
        moveTo(x + (width / 2f), y)
        lineTo(x, y + (height / 2f))
        lineTo(x, y + height)
        lineTo(x + (width / 2f), y + (height / 2f))
        lineTo(x + width, y + height)
        lineTo(x + width, y + (height / 2f))
        close()
    }
@Composable
private fun BackgroundUp() {
    val colour = AppTheme.colors.f1DeltaNegative.copy(alpha = alpha)
    Canvas(
        modifier = Modifier.fillMaxSize(),
        onDraw = {
            val chevronHeight = this.size.height / 2f
            val spacing = this.size.height / 3f
            val list = List(4) {
                getUpwardPath(0f, -spacing / 2f + (it * spacing), this.size.width, chevronHeight)
            }

            list.forEach {
                drawPath(it, colour)
            }
        }
    )
}

private fun getNeutralPath(x: Float, y: Float, width: Float, height: Float) = Path()
    .apply {
        moveTo(x, y)
        lineTo(x + width, y)
        lineTo(x + width, y + height)
        lineTo(x, y + height)
        close()
    }
@Composable
private fun BackgroundNeutral() {
    val colour = AppTheme.colors.f1DeltaNeutral.copy(alpha = alpha)
    Canvas(
        modifier = Modifier.fillMaxSize(),
        onDraw = {
            this.drawPath(getNeutralPath(0f, this.size.height / 10f, this.size.width, this.size.height / 5f), colour)
            this.drawPath(getNeutralPath(0f, (this.size.height / 10f + this.size.height / 3f), this.size.width, this.size.height / 5f), colour)
            this.drawPath(getNeutralPath(0f, (this.size.height / 10f + this.size.height / 1.5f), this.size.width, this.size.height / 5f), colour)
        }
    )
}

private fun getDownwardPath(x: Float, y: Float, width: Float, height: Float) = Path()
    .apply {
        moveTo(x, y)
        lineTo(x + (width / 2f), y + (height / 2f))
        lineTo(x + width, y)
        lineTo(x + width, y + (height / 2f))
        lineTo(x + (width / 2f), y + height)
        lineTo(x, y + (height / 2f))
        close()
    }
@Composable
private fun BackgroundDown() {
    val colour = AppTheme.colors.f1DeltaPositive.copy(alpha = alpha)
    Canvas(
        modifier = Modifier.fillMaxSize(),
        onDraw = {
            val chevronHeight = this.size.height / 2f
            val spacing = this.size.height / 3f
            val list = List(4) {
                getDownwardPath(0f, -spacing / 2f + (it * spacing), this.size.width, chevronHeight)
            }

            list.forEach {
                drawPath(it, colour)
            }
        }
    )
}

@Composable
private fun Position(
    delta: Int,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Text(
        color = color,
        text = delta.absoluteValue.toString(),
        modifier = modifier,
        maxLines = 1,
        textAlign = TextAlign.Center,
        style = AppTheme.typography.block.copy(
            fontSize = 24.sp
        )
    )
}

@PreviewTheme
@Composable
private fun PreviewPositive() {
    AppThemePreview {
        RelativePosition(
            delta = 3,
            modifier = Modifier.size(64.dp, 64.dp)
        )
    }
}

@PreviewTheme
@Composable
private fun PreviewNeutral() {
    AppThemePreview {
        RelativePosition(
            delta = 0,
            modifier = Modifier.size(64.dp, 64.dp)
        )
    }
}

@PreviewTheme
@Composable
private fun PreviewNegative() {
    AppThemePreview {
        RelativePosition(
            delta = -3,
            modifier = Modifier.size(64.dp, 64.dp)
        )
    }
}