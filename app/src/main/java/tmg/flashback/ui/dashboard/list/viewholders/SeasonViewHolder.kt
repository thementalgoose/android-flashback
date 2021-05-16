package tmg.flashback.ui.dashboard.list.viewholders

import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import tmg.flashback.*
import tmg.flashback.databinding.ViewSeasonListSeasonBinding
import tmg.flashback.formula1.constants.Formula1
import tmg.flashback.formula1.constants.Formula1.currentSeasonYear
import tmg.flashback.statistics.R
import tmg.flashback.ui.dashboard.list.HeaderType
import tmg.flashback.ui.dashboard.list.ListItem
import tmg.utilities.extensions.getColor
import tmg.utilities.extensions.views.*

class SeasonViewHolder(
    private var favouriteToggled: (season: Int) -> Unit,
    private var seasonClicked: (season: Int) -> Unit,
    private var setDefault: (season: Int) -> Unit,
    private var clearDefault: () -> Unit,
    private val binding: ViewSeasonListSeasonBinding
): RecyclerView.ViewHolder(binding.root), View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    init {
        binding.container.setOnClickListener(this)
        binding.favourite.setOnClickListener(this)
        binding.more.setOnClickListener(this)
    }

    private var currentSeason: Int = -1
    private var isFavourited: Boolean = false
    private var popupMenu: PopupMenu = PopupMenu(context, binding.more).apply {
        inflate(R.menu.season_list_item)
        setOnMenuItemClickListener(this@SeasonViewHolder)
    }

    fun bind(previous: Int?, season: ListItem.Season, next: Int?) {
        currentSeason = season.season
        isFavourited = season.isFavourited

        popupMenu.menu.findItem(R.id.season_list_clear_default).isEnabled = season.showClearDefault

        val colour = Formula1.coloursDecade["${season.season.toString().substring(0, 3)}0"]?.toColorInt() ?: context.theme.getColor(R.attr.colorPrimary)

        binding.label.text = season.season.toString()
        binding.highlight.setCircleColour(context.theme.getColor(R.attr.backgroundPrimary))

        when (season.selected) {
            true -> {
                binding.label.setTextColor(context.theme.getColor(R.attr.contentPrimary))
                binding.label.alpha = 1.0f
                binding.highlight.invisible()
            }
            false -> {
                binding.label.setTextColor(context.theme.getColor(R.attr.contentTertiary))
                binding.label.alpha = 0.8f
                binding.highlight.visible()
            }
        }

        if (season.isFavourited) {
            binding.favourite.setImageResource(R.drawable.ic_star_filled_coloured)
            binding.favourite.contentDescription = getString(R.string.ab_season_list_unfavourite, season.season)
        }
        else {
            binding.favourite.setImageResource(R.drawable.ic_star_outline)
            binding.favourite.contentDescription = getString(R.string.ab_season_list_favourite, season.season)
        }

        if (season.default) {
            binding.defaultIndicator.show()
        }
        else {
            binding.defaultIndicator.gone()
        }

        binding.more.contentDescription = getString(R.string.ab_season_list_more, season.season)

        binding.cardview.setCircleColour(colour)
        binding.pipeTop.setBackgroundColor(colour)
        binding.pipeBottom.setBackgroundColor(colour)

        println("$previous - $currentSeason - $next")

        binding.pipeTop.show(!currentSeason.toString().endsWith('9') && next != null && next == currentSeason + 1 && season.fixed == HeaderType.ALL)
        binding.pipeBottom.show(!currentSeason.toString().endsWith('0') && previous == currentSeason - 1  && season.fixed == HeaderType.ALL)
    }

    //region View.OnClickListener

    override fun onClick(p0: View?) {
        when (p0) {
            binding.container -> {
                seasonClicked.invoke(currentSeason)
            }
            binding.favourite -> {
                favouriteToggled.invoke(currentSeason)
            }
            binding.more -> {
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