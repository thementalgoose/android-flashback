package tmg.flashback.statistics.ui.dashboard.racepreview

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.threeten.bp.LocalDate
import tmg.flashback.formula1.enums.TrackLayout
import tmg.flashback.formula1.utils.getFlagResourceAlpha3
import tmg.flashback.statistics.R
import tmg.flashback.statistics.databinding.FragmentBottomSheetRacePreviewBinding
import tmg.flashback.statistics.ui.shared.schedule.InlineScheduleAdapter
import tmg.flashback.ui.base.BaseBottomSheetFragment
import tmg.flashback.ui.extensions.getColor
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.views.*

class RacePreviewBottomSheetFragment : BaseBottomSheetFragment<FragmentBottomSheetRacePreviewBinding>() {

    private val viewModel: RacePreviewViewModel by viewModel()

    private lateinit var inlineScheduleAdapter: InlineScheduleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val season = arguments?.getInt(keySeason) ?: 0
        val round = arguments?.getInt(keyRound) ?: 0
        viewModel.inputs.input(season, round)
    }

    override fun inflateView(inflater: LayoutInflater) = FragmentBottomSheetRacePreviewBinding
        .inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        inlineScheduleAdapter = InlineScheduleAdapter()
        binding.enlargedSchedule.adapter = inlineScheduleAdapter
        binding.enlargedSchedule.layoutManager = LinearLayoutManager(context)

        setFragmentResult(fragmentResultKey, bundleOf(
            loadRaceKey to false
        ))

        binding.schedule.setOnClickListener {
            setFragmentResult(fragmentResultKey, bundleOf(
                loadRaceKey to true
            ))
            dismiss()
        }

        observe(viewModel.outputs.overview) {
            if (it == null) {
                dismiss()
                return@observe
            }

            binding.enlargedCountry.setImageResource(binding.enlargedCountry.context.getFlagResourceAlpha3(it.countryISO))
            setStatus(binding.enlargedName.context, it.hasResults, it.hasQualifying, it.date, binding.enlargedStatus)
            binding.enlargedName.text = it.raceName
            binding.enlargedCircuitName.text = it.circuitName
            binding.enlargedRaceCountry.text = it.country
            binding.enlargedRound.text = binding.enlargedRound.context.getString(R.string.race_round, it.round)

            // Track
            val trackLayout = TrackLayout.getTrack(it.circuitId, it.season, it.raceName)
            binding.enlargedTrackIcon.setImageResource(trackLayout?.icon ?: R.drawable.circuit_unknown)
            binding.enlargedTrackIconPlaceholder.setImageResource(trackLayout?.icon ?: R.drawable.circuit_unknown)

            // Schedule adapter
            when (inlineScheduleAdapter.setSchedule(it.schedule)) {
                true -> {
                    binding.enlargedSchedule.visible()
                    binding.enlargedTrackIconPlaceholder.gone()
                    binding.enlargedTrackIcon.visible()
                }
                false -> {
                    binding.enlargedSchedule.invisible()
                    binding.enlargedTrackIconPlaceholder.visible()
                    binding.enlargedTrackIcon.gone()
                }
            }
        }
    }

    private fun setStatus(
        context: Context,
        hasResults: Boolean,
        hasQualifying: Boolean,
        date: LocalDate,
        status: ImageView
    ) {
        when {
            hasResults -> {
                status.setColorFilter(context.theme.getColor(R.attr.f1ResultsFull))
                status.contentDescription = getString(R.string.ab_season_status_has_results)
                status.setImageResource(R.drawable.race_status_hasdata)
            }
            hasQualifying -> {
                status.setColorFilter(context.theme.getColor(R.attr.f1ResultsPartial))
                status.contentDescription = getString(R.string.ab_season_status_has_qualifying)
                status.setImageResource(R.drawable.race_status_hasqualifying)
            }
            date > LocalDate.now() -> {
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

    companion object {

        const val fragmentResultKey: String = "racePreview"

        private const val loadRaceKey: String = "loadRace"
        fun shouldLoadRace(bundle: Bundle) = bundle.getBoolean(loadRaceKey)

        private const val keySeason: String = "season"
        private const val keyRound: String = "round"

        fun instance(season: Int, round: Int) = RacePreviewBottomSheetFragment().apply {
            arguments = bundleOf(
                keySeason to season,
                keyRound to round
            )
        }
    }

}