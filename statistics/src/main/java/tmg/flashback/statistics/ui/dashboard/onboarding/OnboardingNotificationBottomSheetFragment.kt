package tmg.flashback.statistics.ui.dashboard.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import tmg.flashback.statistics.databinding.FragmentBottomSheetNotificationsOnboardingBinding
import tmg.flashback.statistics.repository.models.NotificationChannel
import tmg.flashback.ui.base.BaseBottomSheetFragment
import tmg.flashback.ui.bottomsheet.BottomSheetAdapter
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

        setFragmentResult("FEATURE_BANNER_ONBOARDING", bundleOf())
    }
}