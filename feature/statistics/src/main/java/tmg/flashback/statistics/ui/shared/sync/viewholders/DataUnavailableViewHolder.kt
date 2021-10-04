package tmg.flashback.statistics.ui.shared.sync.viewholders

import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.statistics.R
import tmg.flashback.statistics.databinding.ViewSharedDataUnavailableBinding
import tmg.utilities.extensions.views.getString

enum class DataUnavailable {
    IN_FUTURE_SEASON,
    EARLY_IN_SEASON,
    IN_FUTURE_RACE,
    COMING_SOON_RACE,
    MISSING_RACE,
    DRIVER_NOT_EXIST,
    CONSTRUCTOR_NOT_EXIST,
    CIRCUIT_NOT_EXIST,
    NO_SEARCH_RESULTS
}

class DataUnavailableViewHolder(
    private val binding: ViewSharedDataUnavailableBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(type: DataUnavailable) {
        binding.title.text = when (type) {
            DataUnavailable.IN_FUTURE_SEASON -> getString(R.string.shared_unavailable_future_season)
            DataUnavailable.EARLY_IN_SEASON -> getString(R.string.shared_unavailable_early_season)
            DataUnavailable.IN_FUTURE_RACE -> getString(R.string.shared_unavailable_future_race)
            DataUnavailable.COMING_SOON_RACE -> getString(R.string.shared_unavailable_coming_soon_race)
            DataUnavailable.MISSING_RACE -> getString(R.string.shared_unavailable_missing_race)
            DataUnavailable.CIRCUIT_NOT_EXIST -> getString(R.string.shared_unavailable_circuit_not_found)
            DataUnavailable.DRIVER_NOT_EXIST -> getString(R.string.shared_unavailable_driver_not_found)
            DataUnavailable.CONSTRUCTOR_NOT_EXIST -> getString(R.string.shared_unavailable_constructor_not_found)
            DataUnavailable.NO_SEARCH_RESULTS -> getString(R.string.shared_unavailable_no_search_results)
        }
    }
}