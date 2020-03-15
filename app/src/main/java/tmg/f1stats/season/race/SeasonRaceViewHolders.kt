package tmg.f1stats.season.race

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_season_qualifying_result.view.*
import tmg.f1stats.R

enum class SeasonRaceAdapterViewHolderType(
    @LayoutRes val viewHolderRes: Int
) {
    RACE_PODIUM(R.layout.view_season_race_podium),
    RACE_RESULT(R.layout.view_season_race_result),
    QUALIFYING_RESULT(R.layout.view_season_qualifying_result),
    QUALIFYING_GRID(R.layout.view_season_qualifying_grid);
}

class SeasonRaceRacePodiumViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    fun bind(driver1: SeasonRaceModel, driver2: SeasonRaceModel, driver3: SeasonRaceModel) {

    }
}

class SeasonRaceRaceResultViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    fun bind(driver: SeasonRaceModel) {

    }
}

class SeasonRaceQualifyingResultViewHolder(
    private val callback: SeasonRaceAdapterCallback,
    itemView: View
): RecyclerView.ViewHolder(itemView),
    View.OnClickListener {

    init {
        itemView.container.setOnClickListener(this)
        itemView.q1Time.setOnClickListener(this)
        itemView.q2Time.setOnClickListener(this)
        itemView.q3Time.setOnClickListener(this)
    }
    fun bind(driver: SeasonRaceModel) {
        itemView.apply {
            tvDriver.text = "${driver.driver.name} ${driver.driver.surname}"
            tvConstructor.text = driver.driver.constructor.name + " " + driver.qualiGridPos
            q1Time.text = driver.q1.time
            q2Time.text = driver.q2?.time ?: ""
            q3Time.text = driver.q3?.time ?: ""
        }
    }

    override fun onClick(p0: View?) {
        when (p0) {
            itemView.q1Time -> callback.orderBy(SeasonRaceAdapterType.QUALIFYING_POS_1)
            itemView.q2Time -> callback.orderBy(SeasonRaceAdapterType.QUALIFYING_POS_2)
            itemView.q3Time -> callback.orderBy(SeasonRaceAdapterType.QUALIFYING_POS_3)
            itemView.container -> callback.orderBy(SeasonRaceAdapterType.QUALIFYING_POS)
        }
    }
}

class SeasonRaceQualifyingGridViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    fun bind(driver1: SeasonRaceModel, driver2: SeasonRaceModel?) {

    }
}

