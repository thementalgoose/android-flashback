package tmg.flashback.driver.overview.viewholders

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.view_driver_overview_header.view.*
import kotlinx.android.synthetic.main.view_driver_overview_header.view.imgDriver
import kotlinx.android.synthetic.main.view_driver_overview_header.view.imgNationality
import kotlinx.android.synthetic.main.view_driver_overview_header.view.tvNumber
import org.threeten.bp.format.DateTimeFormatter
import tmg.flashback.R
import tmg.flashback.driver.overview.DriverOverviewItem
import tmg.flashback.driver.season.DriverSeasonItem
import tmg.flashback.utils.getColor
import tmg.flashback.utils.getFlagResourceAlpha3
import tmg.utilities.extensions.views.context

class HeaderViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    fun bind(item: DriverOverviewItem.Header) {

        Glide.with(itemView.context)
                .load(item.driverImg)
                .into(itemView.imgDriver)

        itemView.tvNumber.text = item.driverNumber.toString()
        itemView.tvNumber.colorHighlight = context.theme.getColor(R.attr.colorPrimary)
        itemView.driverBirthday.text = itemView.context.getString(R.string.driver_overview_stat_birthday, item.driverBirthday.format(DateTimeFormatter.ofPattern("dd MMMM yyyy")))

        itemView.imgNationality.setImageResource(context.getFlagResourceAlpha3(item.driverNationalityISO))
    }
}