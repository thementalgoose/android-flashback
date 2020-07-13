package tmg.flashback.driver.season.list.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.view_driver_header.view.*
import tmg.flashback.driver.season.list.DriverSeasonItem
import tmg.flashback.utils.getFlagResourceAlpha3
import tmg.utilities.extensions.views.context

class HeaderViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    fun bind(item: DriverSeasonItem.Header) {

        Glide.with(itemView)
                .clear(itemView.imgDriver)
        Glide.with(itemView)
                .load(item.driver.photoUrl)
                .into(itemView.imgDriver)

        itemView.tvNumber.colorHighlight = item.driver.constructorAtEndOfSeason.color
        itemView.tvNumber.text = item.driver.number.toString()

        itemView.imgNationality.setImageResource(context.getFlagResourceAlpha3(item.driver.nationalityISO))

        itemView.constructors.text = item.constructors.joinToString(separator = ", ") { it.name }
    }
}