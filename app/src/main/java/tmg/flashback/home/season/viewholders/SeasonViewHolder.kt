package tmg.flashback.home.season.viewholders

import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_season_list_season.view.*
import tmg.flashback.*
import tmg.flashback.extensions.dimensionPx
import tmg.flashback.home.season.HeaderType
import tmg.flashback.home.season.SeasonListItem
import tmg.utilities.extensions.views.show

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

        val colour = coloursDecade["${season.season.toString().substring(0, 3)}0"]?.toColorInt() ?: ContextCompat.getColor(itemView.context, R.color.colorTheme)

        itemView.label.text = season.season.toString()
        itemView.favourite.setImageResource(if (season.isFavourited) R.drawable.ic_star_filled_coloured else R.drawable.ic_star_outline)
        itemView.cardview.setCircleColour(colour)

        itemView.pipeTop.setBackgroundColor(colour)
        itemView.pipeBottom.setBackgroundColor(colour)
        itemView.pipeTop.show(!currentSeason.toString().endsWith('9') && currentSeason != currentYear && season.fixed == HeaderType.ALL)
        itemView.pipeBottom.show(!currentSeason.toString().endsWith('0') && currentSeason != currentYear && season.fixed == HeaderType.ALL)

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