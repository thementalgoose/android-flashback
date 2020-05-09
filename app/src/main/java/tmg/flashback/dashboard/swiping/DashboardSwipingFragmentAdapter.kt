package tmg.flashback.dashboard.swiping

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import tmg.flashback.currentYear
import tmg.flashback.dashboard.swiping.season.DashboardSeasonFragment
import tmg.flashback.minimumSupportedYear

class DashboardSwipingFragmentAdapter: FragmentStateAdapter {

    constructor(fragmentManager: FragmentManager, lifecycle: Lifecycle) : super(
        fragmentManager,
        lifecycle
    )

    constructor(fragmentActivity: FragmentActivity) : super(fragmentActivity)
    constructor(fragment: Fragment) : super(fragment)

    override fun getItemCount(): Int = (currentYear + 1) - minimumSupportedYear

    override fun createFragment(position: Int): Fragment {
        return DashboardSeasonFragment.instance(currentYear - position)
    }
}