package tmg.f1stats.season.race

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tmg.utilities.extensions.toEnum
import kotlin.math.ceil

class SeasonRaceAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var viewType: SeasonRaceAdapterType = SeasonRaceAdapterType.RACE
    private var list: List<SeasonRaceModel> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewHolderType: SeasonRaceAdapterViewHolderType? = viewType.toEnum<SeasonRaceAdapterViewHolderType>()
        val view = if (viewHolderType != null) {
            LayoutInflater.from(parent.context).inflate(viewHolderType.viewHolderRes, parent, false)
        }
        else {
            View(parent.context)
        }
        return when (viewHolderType) {
            SeasonRaceAdapterViewHolderType.RACE_PODIUM -> SeasonRaceRacePodiumViewHolder(view)
            SeasonRaceAdapterViewHolderType.RACE_RESULT -> SeasonRaceRaceResultViewHolder(view)
            SeasonRaceAdapterViewHolderType.QUALIFYING_RESULT -> SeasonRaceQualifyingResultViewHolder(view)
            SeasonRaceAdapterViewHolderType.QUALIFYING_GRID -> SeasonRaceQualifyingGridViewHolder(view)
            null -> throw Error("View type not implemented")
        }
    }

    override fun getItemCount(): Int = when (viewType) {
        SeasonRaceAdapterType.RACE -> list.size - 2
        SeasonRaceAdapterType.QUALIFYING_POS -> list.size
        SeasonRaceAdapterType.QUALIFYING_GRID -> ceil(list.size / 2f).toInt()
    }

    override fun getItemViewType(position: Int): Int {
        return when (viewType) {
            SeasonRaceAdapterType.RACE -> if (position == 0) SeasonRaceAdapterViewHolderType.RACE_PODIUM.ordinal else SeasonRaceAdapterViewHolderType.RACE_RESULT.ordinal
            SeasonRaceAdapterType.QUALIFYING_POS -> SeasonRaceAdapterViewHolderType.QUALIFYING_RESULT.ordinal
            SeasonRaceAdapterType.QUALIFYING_GRID -> SeasonRaceAdapterViewHolderType.QUALIFYING_GRID.ordinal
        }
    }

    fun update(type: SeasonRaceAdapterType, list: List<SeasonRaceModel>) {
        this.viewType = type
        this.list = list
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position).toEnum<SeasonRaceAdapterViewHolderType>()) {
            SeasonRaceAdapterViewHolderType.RACE_PODIUM -> {
                (holder as? SeasonRaceRacePodiumViewHolder)?.bind(list[0], list[1], list[2])
            }
            SeasonRaceAdapterViewHolderType.RACE_RESULT -> {
                (holder as? SeasonRaceRaceResultViewHolder)?.bind(list[position + 2])
            }
            SeasonRaceAdapterViewHolderType.QUALIFYING_RESULT -> {
                (holder as? SeasonRaceQualifyingResultViewHolder)?.bind(list[position])
            }
            SeasonRaceAdapterViewHolderType.QUALIFYING_GRID -> {
                val driver1: SeasonRaceModel = list[position * 2]
                val driver2: SeasonRaceModel? = list.getOrNull((position * 2) + 1)
                (holder as? SeasonRaceQualifyingGridViewHolder)?.bind(driver1, driver2)
            }
        }
    }
}

enum class SeasonRaceAdapterType {
    RACE,
    QUALIFYING_POS,
    QUALIFYING_GRID
}