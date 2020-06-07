package tmg.flashback.home.season.viewholders

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_season_list_season.view.*
import tmg.flashback.R
import tmg.flashback.bottomSheetFastScrollDuration
import tmg.flashback.extensions.dimensionPx
import tmg.flashback.home.season.SeasonListItem
import tmg.utilities.extensions.views.show
import tmg.utilities.utils.ConstraintSetAnimator

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

//        itemView.season.setBackgroundColor(colours.random().first.toColorInt())
        itemView.label.text = season.season.toString()
        itemView.favourite.setImageResource(if (season.isFavourited) R.drawable.ic_star_filled_coloured else R.drawable.ic_star_outline)

        Log.i("Flashback", "Indent is visible $isCurrentlyOnScreen")

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