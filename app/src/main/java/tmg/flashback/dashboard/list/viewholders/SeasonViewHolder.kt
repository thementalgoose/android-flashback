package tmg.flashback.dashboard.list.viewholders

import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_season_list_season.view.*
import tmg.flashback.*
import tmg.flashback.dashboard.list.HeaderType
import tmg.flashback.dashboard.list.ListItem
import tmg.flashback.extensions.dimensionPx
import tmg.utilities.extensions.fromHtml
import tmg.utilities.extensions.getColor
import tmg.utilities.extensions.views.context
import tmg.utilities.extensions.views.invisible
import tmg.utilities.extensions.views.show
import tmg.utilities.extensions.views.visible

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

    fun bind(season: ListItem.Season) {
        currentSeason = season.season
        isFavourited = season.isFavourited

        val colour = coloursDecade["${season.season.toString().substring(0, 3)}0"]?.toColorInt() ?: ContextCompat.getColor(itemView.context, R.color.colorTheme)

        itemView.label.text = season.season.toString()
        itemView.highlight.setCircleColour(context.theme.getColor(R.attr.f1BackgroundPrimary))

        when (season.selected) {
            true -> {
                itemView.label.setTextColor(context.theme.getColor(R.attr.f1TextPrimary))
                itemView.highlight.invisible()
            }
            false -> {
                itemView.label.setTextColor(context.theme.getColor(R.attr.f1TextTertiary))
                itemView.highlight.visible()
            }
        }

        itemView.favourite.setImageResource(if (season.isFavourited) R.drawable.ic_star_filled_coloured else R.drawable.ic_star_outline)
        itemView.cardview.setCircleColour(colour)

        itemView.pipeTop.setBackgroundColor(colour)
        itemView.pipeBottom.setBackgroundColor(colour)
        itemView.pipeTop.show(!currentSeason.toString().endsWith('9') && currentSeason != currentYear && season.fixed == HeaderType.ALL)
        itemView.pipeBottom.show(!currentSeason.toString().endsWith('0') && (currentSeason != currentYear || !currentYear.toString().endsWith("0")) && season.fixed == HeaderType.ALL)

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