package tmg.flashback.statistics.ui.dashboard.season.viewholders

import android.graphics.Color
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.recyclerview.widget.RecyclerView
import org.threeten.bp.LocalDate
import tmg.flashback.formula1.enums.TrackLayout
import tmg.flashback.formula1.utils.getFlagResourceAlpha3
import tmg.flashback.statistics.R
import tmg.flashback.statistics.databinding.LayoutDashboardSeasonCalendarWeekBinding
import tmg.flashback.statistics.databinding.ViewDashboardSeasonCalendarWeekBinding
import tmg.flashback.statistics.ui.dashboard.season.SeasonItem
import tmg.utilities.extensions.getColor
import tmg.utilities.extensions.views.context
import tmg.utilities.extensions.views.show

class CalendarWeekViewHolder(
    private val binding: ViewDashboardSeasonCalendarWeekBinding,
    private val calendarWeekRaceClicked: (track: SeasonItem.CalendarWeek) -> Unit,
) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

    private val cells: List<LayoutDashboardSeasonCalendarWeekBinding> by lazy {
        return@lazy listOf(
            binding.cell1,
            binding.cell2,
            binding.cell3,
            binding.cell4,
            binding.cell5,
            binding.cell6,
            binding.cell7
        )
    }
    private var hasExpandingContent: Boolean = false
    private lateinit var item: SeasonItem.CalendarWeek

    init {
        binding.raceData.setOnClickListener(this)
    }

    fun bind(item: SeasonItem.CalendarWeek) {
        this.item = item

        binding.highlight.show(item.race != null)

        cells.forEach {
            it.day.text = ""
            it.day.setBackgroundResource(0)
        }

        val startingIndex = item.startingDay.dayOfWeek.value - 1

        val dayOfMonth = item.startingDay.dayOfMonth
        val lastDayOfMonth: LocalDate =
            item.startingDay.withDayOfMonth(item.startingDay.lengthOfMonth())

        var lastSimulatedDay = 0

        for ((offset, x) in (startingIndex until cells.size).withIndex()) {

            val day = (dayOfMonth + offset)
            cells[x].day.text = day.toString()
            cells[x].day.show(day <= lastDayOfMonth.dayOfMonth)

            val date = item.startingDay.plusDays(offset.toLong())
            if (LocalDate.now() > date) {
                cells[x].day.alpha = textAlpha
            } else {
                cells[x].day.alpha = 1.0f
            }

            if (date == LocalDate.now()) {
                cells[x].day.setBackgroundResource(R.drawable.dashboard_calendar_current_day)
                cells[x].day.setTextColor(Color.WHITE)
            }
            else {
                cells[x].day.setBackgroundResource(0)
                cells[x].day.setTextColor(context.theme.getColor(R.attr.contentPrimary))
            }

            lastSimulatedDay = day
        }

        if (lastSimulatedDay > lastDayOfMonth.dayOfMonth || item.race == null) {
            hasExpandingContent = false
            binding.highlight.alpha = alphaHighlightEnabled
            binding.highlight.show(false)
            binding.flag.show(false)

            binding.circuit.setImageResource(0)
            binding.circuitName.text = ""
            binding.raceName.text = ""
            binding.round.text = ""

            binding.container.setInterpolatedProgress(0.0f)
            binding.container.setTransition(R.id.start, R.id.start)
            binding.container.isEnabled = false
            binding.container.isClickable = false

        } else {
            hasExpandingContent = true
            if (item.race.date < LocalDate.now()) {
                binding.highlight.alpha = alphaHighlightDisabled
            }
            else {
                binding.highlight.alpha = alphaHighlightEnabled
            }
            binding.highlight.show(true)
            binding.flag.show(true)
            binding.flag.setImageResource(context.getFlagResourceAlpha3(item.race.countryISO))

            val track = TrackLayout.getTrack(item.race.circuitId, item.race.season, item.race.raceName)
            binding.circuit.show(track != null)
            if (track != null) {
                binding.circuit.setImageResource(track.icon)
            }
            binding.circuitName.text = "${item.race.circuitName}, ${item.race.country}"
            binding.raceName.text = item.race.raceName
            binding.round.text = "#${item.race.round}"

            binding.container.setInterpolatedProgress(0.0f)
            binding.container.setTransition(R.id.start, R.id.end)
            binding.container.isEnabled = true
            binding.container.isClickable = true
        }

        binding.container.progress = 0.0f
    }

    companion object {
        private const val alphaHighlightEnabled = 0.6f
        private const val alphaHighlightDisabled = 0.2f

        private const val textAlpha = 0.35f
    }

    override fun onClick(p0: View?) {
        when (p0) {
            binding.raceData -> {
                calendarWeekRaceClicked(item)
            }
        }
    }
}