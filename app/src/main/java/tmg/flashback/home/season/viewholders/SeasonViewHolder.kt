package tmg.flashback.home.season.viewholders

import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_season_list_season.view.*
import tmg.flashback.R
import tmg.flashback.bottomSheetFastScrollDuration
import tmg.flashback.coloursDecade
import tmg.flashback.extensions.dimensionPx
import tmg.flashback.home.season.SeasonListItem
import tmg.flashback.minimumSupportedYear
import tmg.utilities.extensions.ordinalAbbreviation

class SeasonViewHolder(
    private var favouriteToggled: ((season: Int) -> Unit)? = null,
    private var seasonClicked: ((season: Int) -> Unit)? = null,
    itemView: View
): RecyclerView.ViewHolder(itemView), View.OnClickListener {

    init {
        itemView.container.setOnClickListener(this)
        itemView.favourite.setOnClickListener(this)
    }

    private var currentSeason: Int = -1
    private var isFavourited: Boolean = false

    fun bind(season: SeasonListItem.Season, isCurrentlyOnScreen: Boolean, indentState: Boolean) {
        currentSeason = season.season
        isFavourited = season.isFavourited

        itemView.label.text = season.season.toString()
        itemView.favourite.setImageResource(if (season.isFavourited) R.drawable.ic_star_filled_coloured else R.drawable.ic_star_outline)
        itemView.season.setBackgroundColor(coloursDecade["${season.season.toString().substring(0, 3)}0"]?.toColorInt() ?: ContextCompat.getColor(itemView.context, R.color.colorTheme))
//        itemView.season.text = ((season.season - minimumSupportedYear) + 1).ordinalAbbreviation

        if (isCurrentlyOnScreen) {
            if (indentState) { // true = indent it!
                itemView.favourite
                    .animate()
                    .translationX(-itemView.context.dimensionPx(R.dimen.bottomSheetFastScrollWidth))
                    .setDuration(bottomSheetFastScrollDuration.toLong())
                    .start()
            }
            else {
                itemView.favourite
                    .animate()
                    .translationX(0.0f)
                    .setDuration(bottomSheetFastScrollDuration.toLong())
                    .start()
            }
        }
        else {
            itemView.favourite.translationX = if (indentState) {
                -itemView.context.dimensionPx(R.dimen.bottomSheetFastScrollWidth)
            } else {
                0.0f
            }
        }
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
    }
}