package tmg.flashback.statistics.extensions

import android.widget.TextView
import com.bumptech.glide.Glide
import org.koin.androidx.compose.inject
import tmg.flashback.formula1.extensions.pointsDisplay
import tmg.flashback.formula1.model.RaceRaceResult
import tmg.flashback.formula1.utils.getFlagResourceAlpha3
import tmg.flashback.statistics.R
import tmg.flashback.statistics.databinding.LayoutPodiumBinding
import tmg.flashback.statistics.ui.util.positionStarted
import tmg.flashback.ui.animation.GlideProvider
import tmg.flashback.ui.extensions.getColor
import tmg.utilities.extensions.ordinalAbbreviation
import tmg.utilities.extensions.views.gone
import tmg.utilities.extensions.views.visible
import kotlin.math.abs

private val glideProvider: GlideProvider = GlideProvider()

fun LayoutPodiumBinding.bindRaceModel(model: RaceRaceResult, position: Int, pointsLayout: TextView) {
    val context = pointsLayout.context

    val points = model.points.pointsDisplay() ?: ""
    val started = model.grid?.positionStarted(context)

    pointsLayout.text = context.getString(R.string.round_podium_points, model.points.pointsDisplay())
    tvDriver.text = model.driver.driver.name
    tvNumber.text = model.driver.driver.number?.toString() ?: ""
    tvNumber.colorHighlight = model.driver.constructor.color
    tvConstructor.text = model.driver.constructor.name

    constructorColor.setBackgroundColor(model.driver.constructor.color)

    model.driver.driver.photoUrl?.let {
        glideProvider.load(imgDriver, it)
    }

    // Starting Position
    tvStartedAbsolute.text = model.grid?.positionStarted(context) ?: ""
    tvStartedRelative.text = abs((model.finish) - (model.grid ?: 0)).toString()
    val diff = (model.grid ?: 0) - (model.finish)
    when {
        diff == 0 || model.grid == null -> { // Equal
            imgStarted.setImageResource(R.drawable.ic_pos_neutral)
            imgStarted.setColorFilter(context.theme.getColor(R.attr.f1DeltaNeutral))
            tvStartedRelative.setTextColor(context.theme.getColor(R.attr.f1DeltaNeutral))
        }
        diff > 0 -> { // Gained
            imgStarted.setImageResource(R.drawable.ic_pos_up)
            imgStarted.setColorFilter(context.theme.getColor(R.attr.f1DeltaNegative))
            tvStartedRelative.setTextColor(context.theme.getColor(R.attr.f1DeltaNegative))
        }
        else -> { // Lost
            imgStarted.setImageResource(R.drawable.ic_pos_down)
            imgStarted.setColorFilter(context.theme.getColor(R.attr.f1DeltaPositive))
            tvStartedRelative.setTextColor(context.theme.getColor(R.attr.f1DeltaPositive))
        }
    }

    // Nationality
    imgNationality.setImageResource(context.getFlagResourceAlpha3(model.driver.driver.nationalityISO))

    // Time
    if (model.finish == 1) {
        tvTime.text = model.time.toString()
    }
    else {
        if (model.time?.noTime == false) {
            tvTime.text = context.getString(R.string.race_time_delta, model.time)
        }
        else {
            tvTime.text = model.status
        }
    }

    if (model.fastestLap?.rank == 1) {
        imgFastestLap.visible()
    } else {
        imgFastestLap.gone()
    }

    // Accessibility
    var contentDescription = when {
        model.grid != null -> context.getString(R.string.ab_race_podium_started,
            model.driver.driver.name,
            model.driver.constructor.name,
            position.ordinalAbbreviation,
            points,
            model.grid?.ordinalAbbreviation ?: "unknown"
        )
        else -> context.getString(R.string.ab_race_podium,
            model.driver.driver.name,
            model.driver.constructor.name,
            position.ordinalAbbreviation,
            points
        )
    }
    if (model.fastestLap?.rank == 1) {
        contentDescription += context.getString(R.string.ab_race_podium_fastest_lap)
    }
    container.contentDescription = contentDescription
}