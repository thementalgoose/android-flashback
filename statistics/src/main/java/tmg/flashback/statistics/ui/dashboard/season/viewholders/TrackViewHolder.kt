package tmg.flashback.statistics.ui.dashboard.season.viewholders

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import tmg.flashback.formula1.enums.TrackLayout
import tmg.flashback.formula1.model.Timestamp
import tmg.flashback.statistics.R
import tmg.flashback.statistics.ui.dashboard.season.SeasonItem
import tmg.flashback.ui.extensions.getColor
import tmg.flashback.statistics.databinding.ViewDashboardSeasonTrackBinding
import tmg.flashback.formula1.utils.getFlagResourceAlpha3
import tmg.flashback.statistics.ui.shared.schedule.InlineScheduleAdapter
import tmg.utilities.extensions.ordinalAbbreviation
import tmg.utilities.extensions.views.context
import tmg.utilities.extensions.views.getString
import tmg.utilities.extensions.views.show

@Suppress("EXPERIMENTAL_API_USAGE")
class TrackViewHolder(
    val trackClicked: (SeasonItem.Track) -> Unit,
    private val binding: ViewDashboardSeasonTrackBinding
): RecyclerView.ViewHolder(binding.root), View.OnClickListener {

    private lateinit var data: SeasonItem.Track

    private val inlineScheduleAdapter: InlineScheduleAdapter

    private var clickShowExpanded: Boolean = false
    private var showExpanded: Boolean = false
        set(value) {
            binding.container.show(!value)
            binding.schedule.show(value)
            field = value
        }

    init {
        binding.container.setOnClickListener(this)
        binding.schedule.setOnClickListener(this)

        inlineScheduleAdapter = InlineScheduleAdapter()
        binding.enlargedSchedule.adapter = inlineScheduleAdapter
        binding.enlargedSchedule.layoutManager = LinearLayoutManager(context)
    }

    fun bind(item: SeasonItem.Track) {
        data = item
        binding.apply {
            // Country icon
            country.setImageResource(context.getFlagResourceAlpha3(item.raceCountryISO))
            enlargedCountry.setImageResource(context.getFlagResourceAlpha3(item.raceCountryISO))

            // Status
            setStatus(item, this.status)
            setStatus(item, this.enlargedStatus)

            // Race name
            raceName.text = item.raceName
            enlargedName.text = item.raceName

            // Circuit Name
            circuitName.text = item.circuitName
            enlargedCircuitName.text = item.circuitName

            // Country
            raceCountry.text = item.raceCountry
            enlargedRaceCountry.text = item.raceCountry

            // Round
            round.text = context.getString(R.string.race_round, item.round)
            enlargedRound.text = context.getString(R.string.race_round, item.round)

            // Track
            val trackLayout = TrackLayout.getTrack(data.circuitId, data.season, data.raceName)
            enlargedTrackIcon.setImageResource(trackLayout?.icon ?: R.drawable.circuit_unknown)

            // Schedule adapter
            inlineScheduleAdapter.setSchedule(item.schedule)
            val zoneId = LocalDateTime.now().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("z"))
            enlargedDeviceTime.text = getString(R.string.dashboard_season_track_notification_time, zoneId)

            @SuppressLint("SetTextI18n")
            date.text = "${item.date.dayOfMonth.ordinalAbbreviation} ${item.date.format(DateTimeFormatter.ofPattern("MMM yy"))}"
        }

        fade(!data.hasQualifying && !data.hasResults)

        showExpanded = (!data.hasQualifying || !data.hasResults) && item.defaultExpanded
        clickShowExpanded = data.date >= LocalDate.now()
    }

    override fun onClick(p0: View?) {
        if (clickShowExpanded && !showExpanded) {
            showExpanded = true
        }
        else {
            trackClicked(data)
        }
    }

    private fun setStatus(data: SeasonItem.Track, status: ImageView) {
        when {
            data.hasResults -> {
                status.setColorFilter(context.theme.getColor(R.attr.f1ResultsFull))
                status.contentDescription = getString(R.string.ab_season_status_has_results)
                status.setImageResource(R.drawable.race_status_hasdata)
            }
            data.hasQualifying -> {
                status.setColorFilter(context.theme.getColor(R.attr.f1ResultsPartial))
                status.contentDescription = getString(R.string.ab_season_status_has_qualifying)
                status.setImageResource(R.drawable.race_status_hasqualifying)
            }
            data.date > LocalDate.now() -> {
                status.setColorFilter(context.theme.getColor(R.attr.f1ResultsNeutral))
                status.contentDescription = getString(R.string.ab_season_status_in_future)
                status.setImageResource(R.drawable.race_status_nothappened)
            }
            else -> {
                status.setColorFilter(context.theme.getColor(R.attr.f1ResultsNeutral))
                status.contentDescription = getString(R.string.ab_season_status_waiting_for_results)
                status.setImageResource(R.drawable.race_status_waitingfor)
            }
        }
    }

    private fun fade(shouldFade: Boolean) {
        binding.raceName.alpha = if (shouldFade) fadeAlpha else 1.0f
        binding.circuitName.alpha = if (shouldFade) fadeAlpha else 1.0f
        binding.raceCountry.alpha = if (shouldFade) fadeAlpha else 1.0f
        binding.round.alpha = if (shouldFade) fadeAlpha else 1.0f
        binding.date.alpha = if (shouldFade) fadeAlpha else 1.0f
        binding.country.alpha = if (shouldFade) fadeAlpha else 1.0f
        binding.status.alpha = if (shouldFade) fadeAlpha else 1.0f
    }

    companion object {
        private const val fadeAlpha = 0.5f
    }
}