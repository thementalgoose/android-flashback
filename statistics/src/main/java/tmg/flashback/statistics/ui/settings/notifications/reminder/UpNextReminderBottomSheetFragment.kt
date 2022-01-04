package tmg.flashback.statistics.ui.settings.notifications.reminder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.flashback.statistics.databinding.FragmentBottomSheetNotificationsReminderBinding
import tmg.flashback.ui.base.BaseBottomSheetFragment
import tmg.flashback.ui.bottomsheet.BottomSheetAdapter
import tmg.flashback.statistics.ui.settings.notifications.reminder.UpNextReminderViewModel
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent

class UpNextReminderBottomSheetFragment: BaseBottomSheetFragment<FragmentBottomSheetNotificationsReminderBinding>() {

    private val viewModel: UpNextReminderViewModel by viewModel()

    private lateinit var adapter: BottomSheetAdapter

    override fun inflateView(inflater: LayoutInflater) =
        FragmentBottomSheetNotificationsReminderBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = BottomSheetAdapter(
            itemClicked = {
                viewModel.inputs.selectNotificationReminder(tmg.flashback.statistics.repository.models.NotificationReminder.values()[it.id])
            }
        )
        binding.list.adapter = adapter
        binding.list.layoutManager = LinearLayoutManager(context)

        observe(viewModel.outputs.notificationPrefs) {
            adapter.list = it
        }

        observeEvent(viewModel.outputs.updated) {
            dismiss()
        }
    }
}