package tmg.flashback.upnext.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.core.ui.base.BaseBottomSheetFragment
import tmg.core.ui.bottomsheet.BottomSheetAdapter
import tmg.flashback.upnext.databinding.FragmentBottomSheetNotificationsOnboardingBinding
import tmg.flashback.upnext.model.NotificationChannel
import tmg.utilities.extensions.observe

class OnboardingNotificationBottomSheetFragment: BaseBottomSheetFragment<FragmentBottomSheetNotificationsOnboardingBinding>() {

    private val viewModel: OnboardingNotificationViewModel by viewModel()

    private lateinit var adapter: BottomSheetAdapter

    override fun inflateView(inflater: LayoutInflater) =
        FragmentBottomSheetNotificationsOnboardingBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = BottomSheetAdapter(
            itemClicked = {
                viewModel.inputs.selectNotificationChannel(NotificationChannel.values()[it.id])
            }
        )
        binding.list.adapter = adapter
        binding.list.layoutManager = LinearLayoutManager(context)

        observe(viewModel.outputs.notificationPreferences) {
            adapter.list = it
        }
    }
}