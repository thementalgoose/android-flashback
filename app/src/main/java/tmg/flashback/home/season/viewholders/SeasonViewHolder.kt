package tmg.flashback.home.season.viewholders

import android.view.View
import android.widget.Toast
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_season_list_season.view.*
import tmg.flashback.R
import tmg.flashback.colours
import tmg.flashback.home.season.SeasonListItem

class SeasonViewHolder(
    var favouriteToggled: ((season: Int) -> Unit)? = null,
    var seasonClicked: ((season: Int) -> Unit)? = null,
    itemView: View
): RecyclerView.ViewHolder(itemView), View.OnClickListener {

    init {
        itemView.container.setOnClickListener(this)
        itemView.favourite.setOnClickListener(this)
    }

    var currentSeason: Int = -1
    var isFavourited: Boolean = false

    fun bind(season: SeasonListItem.Season) {
        currentSeason = season.season
        isFavourited = season.isFavourited

        itemView.season.setBackgroundColor(colours.random().first.toColorInt())
        itemView.label.text = season.season.toString()
        itemView.favourite.setImageResource(if (season.isFavourited) R.drawable.ic_star_filled else R.drawable.ic_star_outline)
    }

    override fun onClick(p0: View?) {
        when (p0) {
            itemView.container -> {
                seasonClicked?.invoke(currentSeason)
            }
            itemView.favourite -> {
                favouriteToggled?.invoke(currentSeason)
            }
        }
        Toast.makeText(p0?.context, "CLICKED CELL", Toast.LENGTH_SHORT).show()
    }
}