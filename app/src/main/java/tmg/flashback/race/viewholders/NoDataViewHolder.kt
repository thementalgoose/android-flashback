package tmg.flashback.race.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_race_no_data.view.*
import tmg.flashback.R
import tmg.utilities.extensions.views.show

class NoDataViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    fun bind(hasRacePassed: Boolean) {
        itemView.lottie.show(hasRacePassed)
        itemView.lights.show(!hasRacePassed)
        itemView.title.text = itemView.context.getString(if (hasRacePassed) R.string.race_not_found else R.string.race_not_started)
    }
}