package tmg.flashback.statistics.ui.shared.schedule.viewholders

import androidx.recyclerview.widget.RecyclerView
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import tmg.flashback.statistics.R
import tmg.flashback.statistics.databinding.ViewInlineScheduleDeviceTimeBinding
import tmg.utilities.extensions.views.context

class DeviceTimeViewHolder(
    private val binding: ViewInlineScheduleDeviceTimeBinding
): RecyclerView.ViewHolder(binding.root) {

    init {
        val zoneId = LocalDateTime.now().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("z"))
        binding.deviceTime.text = context.getString(R.string.dashboard_season_track_notification_time, zoneId)
    }
}