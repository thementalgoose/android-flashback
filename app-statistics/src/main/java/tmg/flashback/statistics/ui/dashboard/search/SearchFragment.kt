package tmg.flashback.statistics.ui.dashboard.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import tmg.flashback.core.ui.BaseFragment
import tmg.flashback.statistics.R
import tmg.flashback.statistics.databinding.FragmentDashboardSearchBinding

class SearchFragment: BaseFragment<FragmentDashboardSearchBinding>() {

    override fun inflateView(inflater: LayoutInflater) =
        FragmentDashboardSearchBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}