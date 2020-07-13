package tmg.flashback.home.season.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_season_list_header.view.*
import tmg.flashback.R
import tmg.flashback.bottomSheetFastScrollDuration
import tmg.flashback.extensions.dimensionPx
import tmg.flashback.home.season.HeaderType
import tmg.flashback.home.season.SeasonListItem
import tmg.utilities.extensions.views.show

class HeaderViewHolder(
    private var featureToggled: ((type: HeaderType) -> Unit)? = null,
    itemView: View
): RecyclerView.ViewHolder(itemView), View.OnClickListener {

    init {
        itemView.container.setOnClickListener(this)
    }

    private lateinit var type: HeaderType
    private var expanded: Boolean? = null

    fun bind(header: SeasonListItem.Header, isCurrentlyOnScreen: Boolean, indentState: Boolean) {

        type = header.type
        expanded = header.expanded

        itemView.header.setText(header.type.label)
        itemView.arrow.show(header.expanded != null)
        when (header.expanded) {
            true -> {
                itemView.arrow.setImageResource(R.drawable.arrow_down)
            }
            false -> {
                itemView.arrow.setImageResource(R.drawable.arrow_up)
            }
        }

        if (isCurrentlyOnScreen) {
            if (indentState) { // true = indent it!
                itemView.arrow
                    .animate()
                    .translationX(-itemView.context.dimensionPx(R.dimen.bottomSheetFastScrollWidth))
                    .setDuration(bottomSheetFastScrollDuration.toLong())
                    .start()
            }
            else {
                itemView.arrow
                    .animate()
                    .translationX(0.0f)
                    .setDuration(bottomSheetFastScrollDuration.toLong())
                    .start()
            }
        }
        else {
            itemView.arrow.translationX = if (indentState) {
                -itemView.context.dimensionPx(R.dimen.bottomSheetFastScrollWidth)
            } else {
                0.0f
            }
        }
    }

    override fun onClick(p0: View?) {
        if (p0 == itemView.container) {
            featureToggled?.invoke(type)
        }
    }
}