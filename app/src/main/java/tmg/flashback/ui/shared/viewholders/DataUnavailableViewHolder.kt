package tmg.flashback.ui.shared.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_shared_data_unavailable.view.*
import tmg.flashback.R
import tmg.utilities.extensions.views.getString

enum class DataUnavailable {
    IN_FUTURE_SEASON,
    EARLY_IN_SEASON,
    IN_FUTURE_RACE,
    COMING_SOON_RACE,
    MISSING_RACE,
    DRIVER_NOT_EXIST,
    CONSTRUCTOR_NOT_EXIST,
    CIRCUIT_NOT_EXIST
}

class DataUnavailableViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    fun bind(type: DataUnavailable) {
        itemView.title.text = when (type) {
            DataUnavailable.IN_FUTURE_SEASON -> getString(R.string.shared_unavailable_future_season)
            DataUnavailable.EARLY_IN_SEASON -> getString(R.string.shared_unavailable_early_season)
            DataUnavailable.IN_FUTURE_RACE -> getString(R.string.shared_unavailable_future_race)
            DataUnavailable.COMING_SOON_RACE -> getString(R.string.shared_unavailable_coming_soon_race)
            DataUnavailable.MISSING_RACE -> getString(R.string.shared_unavailable_missing_race)
            DataUnavailable.CIRCUIT_NOT_EXIST -> getString(R.string.shared_unavailable_circuit_not_found)
            DataUnavailable.DRIVER_NOT_EXIST -> getString(R.string.shared_unavailable_driver_not_found)
            DataUnavailable.CONSTRUCTOR_NOT_EXIST -> getString(R.string.shared_unavailable_constructor_not_found)
        }
    }
}