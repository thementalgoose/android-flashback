package tmg.flashback.statistics.ui.dashboard.season.viewholders

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.graphics.Color
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.AnimationSet
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import androidx.core.animation.addListener
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.delay
import org.threeten.bp.LocalDate
import tmg.core.ui.model.AnimationSpeed
import tmg.flashback.formula1.enums.TrackLayout
import tmg.flashback.formula1.utils.getFlagResourceAlpha3
import tmg.flashback.statistics.R
import tmg.flashback.statistics.databinding.LayoutDashboardSeasonCalendarWeekBinding
import tmg.flashback.statistics.databinding.ViewDashboardSeasonCalendarWeekBinding
import tmg.flashback.statistics.ui.dashboard.season.SeasonItem
import tmg.utilities.extensions.getColor
import tmg.utilities.extensions.views.context
import tmg.utilities.extensions.views.gone
import tmg.utilities.extensions.views.show
import tmg.utilities.extensions.views.visible

class CalendarWeekViewHolder(
    private val binding: ViewDashboardSeasonCalendarWeekBinding,
    private val calendarWeekRaceClicked: (track: SeasonItem.CalendarWeek) -> Unit,
    private val animationSpeed: AnimationSpeed
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

    // Flag to stop enterance animation clicks
    private var isExpanded: Boolean = false

    // Animators
    private var animation: AnimatorSet? = null

    init {
        binding.raceData.setOnClickListener(this)
        binding.calendar.setOnClickListener(this)
    }

    fun bind(item: SeasonItem.CalendarWeek) {
        this.item = item

        // Reset content
        isExpanded = false
        cancelAnimations()
        binding.highlight.show(item.race != null)
        cells.forEach {
            it.day.text = ""
            it.day.setBackgroundResource(0)
        }

        // Drawing calendar content
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

        // Race data binding
        if (lastSimulatedDay > lastDayOfMonth.dayOfMonth || item.race == null) {
            hasExpandingContent = false
            binding.highlight.alpha = alphaHighlightEnabled
            binding.highlight.show(false)
            binding.flag.show(false)

            binding.circuit.setImageResource(0)
            binding.circuitName.text = ""

        } else {
            hasExpandingContent = true
            if (item.race.date < LocalDate.now()) {
                binding.highlight.alpha = alphaHighlightDisabled
                binding.background.alpha = alphaHighlightDisabled
            }
            else {
                binding.highlight.alpha = alphaHighlightEnabled
                binding.background.alpha = alphaHighlightEnabled
            }
            binding.highlight.show(true)
            binding.flag.show(true)
            binding.flag.setImageResource(context.getFlagResourceAlpha3(item.race.countryISO))

            val track = TrackLayout.getTrack(item.race.circuitId, item.race.season, item.race.raceName)
            binding.circuit.show(track != null)
            if (track != null) {
                binding.circuit.setImageResource(track.icon)
            }
            binding.circuitName.text = "${item.race.circuitName} (#${item.race.round})"
        }

        binding.calendar.translationX = 0.0f

        binding.raceData.visibility = View.INVISIBLE
        binding.raceData.translationX = binding.root.width.toFloat()
    }

    /**
     * Animate the views from [calendarXInitial] to [calendarXFinal]
     * @param calendarXInitial Pixel value of the x initial position, null = current position
     * @param calendarXFinal Pixel value of the x final position
     */
    private fun animateFrom(
        calendarXInitial: Float? = null,
        calendarXFinal: Float,
        interpolator: Interpolator = LinearInterpolator(),
        duration: Long = animationSpeed.millis.toLong()
    ): AnimatorSet {

        val initial = calendarXInitial ?: binding.raceData.translationX
        val final = calendarXFinal
        val screenWidth = binding.root.width.toFloat()

        // Calendar view
        val calendarTranslator = ValueAnimator.ofFloat(initial, final)
        calendarTranslator.addUpdateListener {
            val value = it.animatedValue as Float
            binding.calendar.translationX = value
        }
        calendarTranslator.addListener(
            onEnd = { binding.calendar.translationX = final }
        )

        // Race Data view
        val raceDataTranslator = ValueAnimator.ofFloat(initial + screenWidth, final + screenWidth)
        raceDataTranslator.addUpdateListener {
            val value = it.animatedValue as Float
            binding.raceData.translationX = value
        }
        raceDataTranslator.addListener(
            onEnd = { binding.raceData.translationX = final + screenWidth }
        )

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(calendarTranslator, raceDataTranslator)
        animatorSet.interpolator = interpolator
        animatorSet.duration = duration
        return animatorSet
    }

    /**
     * Cancel any current animations
     */
    private fun cancelAnimations() {
        animation?.cancel()
        animation = null
    }

    override fun onClick(p0: View?) {
        when (p0) {
            binding.calendar -> {
                if (hasExpandingContent && !isExpanded) {

                    binding.raceData.visible()

                    isExpanded = true
                    val initialAnimation = animateFrom(
                        calendarXInitial = 0f,
                        calendarXFinal = -(binding.raceData.width.toFloat()),
                        interpolator = DecelerateInterpolator()
                    )
                    val delayedReverse = animateFrom(
                        calendarXInitial = -(binding.raceData.width.toFloat()),
                        calendarXFinal = 0f,
                        interpolator = AccelerateInterpolator()
                    )
                    delayedReverse.startDelay = 3000L
                    delayedReverse.addListener(
                        onEnd = { isExpanded = false }
                    )

                    animation = AnimatorSet()
                    animation?.playTogether(initialAnimation, delayedReverse)
                    animation?.start()
                }
            }
            binding.raceData -> {
                if (hasExpandingContent) {
                    calendarWeekRaceClicked(item)
                }
            }
        }
    }

    companion object {
        private const val alphaHighlightEnabled = 0.4f
        private const val alphaHighlightDisabled = 0.2f

        private const val textAlpha = 0.35f
    }
}