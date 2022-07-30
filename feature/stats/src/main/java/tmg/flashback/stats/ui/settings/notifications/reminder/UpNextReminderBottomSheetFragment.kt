package tmg.flashback.stats.ui.settings.notifications.reminder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import tmg.flashback.stats.databinding.FragmentBottomSheetNotificationsReminderBinding
import tmg.flashback.stats.repository.models.NotificationReminder
import tmg.flashback.ui.base.BaseBottomSheetFragment
import tmg.flashback.ui.bottomsheet.BottomSheetAdapter
import tmg.utilities.extensions.observe
import tmg.utilities.extensions.observeEvent

@AndroidEntryPoint
class UpNextReminderBottomSheetFragment: BaseBottomSheetFragment<FragmentBottomSheetNotificationsReminderBinding>() {

    private val viewModel: UpNextReminderViewModel by viewModels()

    private lateinit var adapter: BottomSheetAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logScreenViewed("Up Next Reminders")
    }

    override fun inflateView(inflater: LayoutInflater) =
        FragmentBottomSheetNotificationsReminderBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = BottomSheetAdapter(
            itemClicked = {
                viewModel.inputs.selectNotificationReminder(NotificationReminder.values()[it.id])
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