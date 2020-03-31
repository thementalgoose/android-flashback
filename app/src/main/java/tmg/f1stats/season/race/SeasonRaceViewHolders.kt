package tmg.f1stats.season.race

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_driver.view.*
import kotlinx.android.synthetic.main.view_qualifying_result.view.*
import tmg.f1stats.R

enum class SeasonRaceAdapterViewHolderType(
    @LayoutRes val viewHolderRes: Int
) {
    RACE_PODIUM(R.layout.view_race_podium),
    RACE_RESULT(R.layout.view_race_result),
    QUALIFYING_RESULT(R.layout.view_qualifying_result),
}

class SeasonRaceRacePodiumViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(driver1: SeasonRaceModel, driver2: SeasonRaceModel, driver3: SeasonRaceModel) {

    }
}

class SeasonRaceRaceResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(driver: SeasonRaceModel) {

    }
}

class SeasonRaceQualifyingResultViewHolder(
    private val callback: SeasonRaceAdapterCallback,
    itemView: View
) : RecyclerView.ViewHolder(itemView),
    View.OnClickListener {

    init {

    }

    fun bind(season: SeasonRaceModel) {
        itemView.apply {
            this.tvPosition.text = season.racePos.toString()
            this.layoutDriver.tvName.text = season.driver.name
            this.layoutDriver.tvNumber.text = season.driver.code
            this.layoutDriver.tvNumber.colorHighlight = season.driver.constructor.color
            this.layoutDriver.imgFlag.setBackgroundResource(R.drawable.gb)
        }
    }

    override fun onClick(p0: View?) {
        when (p0) {

        }
    }
}

//class SeasonRaceQualifyingGridViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
//    fun bind(driver1: SeasonRaceModel, driver2: SeasonRaceModel?) {
//        bindDriverFirst(driver1)
//        bindDriverSecond(driver2)
//    }
//
//    private fun bindDriverFirst(driver: SeasonRaceModel) {
//
//    }
//
//    private fun bindDriverSecond(driver: SeasonRaceModel?) {
//
//    }
//}

