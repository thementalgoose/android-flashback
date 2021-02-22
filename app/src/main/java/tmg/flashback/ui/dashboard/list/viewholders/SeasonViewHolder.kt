package tmg.flashback.ui.dashboard.list.viewholders

import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_season_list_season.view.*
import tmg.flashback.*
import tmg.flashback.constants.App.coloursDecade
import tmg.flashback.constants.App.currentYear
import tmg.flashback.ui.dashboard.list.HeaderType
import tmg.flashback.ui.dashboard.list.ListItem
import tmg.utilities.extensions.getColor
import tmg.utilities.extensions.views.*

class SeasonViewHolder(
    private var favouriteToggled: (season: Int) -> Unit,
    private var seasonClicked: (season: Int) -> Unit,
    private var setDefault: (season: Int) -> Unit,
    private var clearDefault: () -> Unit,
    itemView: View
): RecyclerView.ViewHolder(itemView), View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    init {
        itemView.container.setOnClickListener(this)
        itemView.favourite.setOnClickListener(this)
        itemView.more.setOnClickListener(this)
    }

    private var currentSeason: Int = -1
    private var isFavourited: Boolean = false
    private var popupMenu: PopupMenu = PopupMenu(context, itemView.more).apply {
        inflate(R.menu.season_list_item)
        setOnMenuItemClickListener(this@SeasonViewHolder)
    }

    fun bind(season: ListItem.Season) {
        currentSeason = season.season
        isFavourited = season.isFavourited

        popupMenu.menu.findItem(R.id.season_list_clear_default).isEnabled = season.showClearDefault

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

        if (season.isFavourited) {
            itemView.favourite.setImageResource(R.drawable.ic_star_filled_coloured)
            itemView.favourite.contentDescription = getString(R.string.ab_season_list_unfavourite, season.season)
        }
        else {
            itemView.favourite.setImageResource(R.drawable.ic_star_outline)
            itemView.favourite.contentDescription = getString(R.string.ab_season_list_favourite, season.season)
        }

        if (season.default) {
            itemView.defaultIndicator.show()
        }
        else {
            itemView.defaultIndicator.gone()
        }

        itemView.more.contentDescription = getString(R.string.ab_season_list_more, season.season)

        itemView.cardview.setCircleColour(colour)
        itemView.pipeTop.setBackgroundColor(colour)
        itemView.pipeBottom.setBackgroundColor(colour)
        itemView.pipeTop.show(!currentSeason.toString().endsWith('9') && currentSeason != currentYear && season.fixed == HeaderType.ALL)
        itemView.pipeBottom.show(!currentSeason.toString().endsWith('0') && (currentSeason != currentYear || !currentYear.toString().endsWith("0")) && season.fixed == HeaderType.ALL)
    }

    //region View.OnClickListener

    override fun onClick(p0: View?) {
        when (p0) {
            itemView.container -> {
                seasonClicked.invoke(currentSeason)
            }
            itemView.favourite -> {
                favouriteToggled.invoke(currentSeason)
            }
            itemView.more -> {
                popupMenu.show()
            }
        }
    }

    //endregion

    //region PopupMenu.OnMenuItemClickListener

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.season_list_item_default -> {
                setDefault.invoke(currentSeason)
            }
            R.id.season_list_clear_default -> {
                clearDefault.invoke()
            }
        }
        return true
    }

    //endregion
}