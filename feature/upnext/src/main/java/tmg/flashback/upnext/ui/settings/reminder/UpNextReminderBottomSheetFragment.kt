package tmg.flashback.upnext.ui.settings.reminder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.core.ui.base.BaseBottomSheetFragment
import tmg.core.ui.bottomsheet.BottomSheetAdapter
import tmg.flashback.upnext.databinding.FragmentBottomSheetNotificationsReminderBinding
import tmg.flashback.upnext.model.NotificationReminder
import tmg.utilities.extensions.observe

class UpNextReminderBottomSheetFragment: BaseBottomSheetFragment<FragmentBottomSheetNotificationsReminderBinding>() {

    private val viewModel: UpNextReminderViewModel by viewModel()

    private lateinit var adapter: BottomSheetAdapter

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
            dismiss()
        }
    }
}