package tmg.flashback.dashboard

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import tmg.flashback.dashboard.season.DashboardSeasonFragment

class DashboardFragmentAdapter: FragmentStateAdapter {

    constructor(fragmentManager: FragmentManager, lifecycle: Lifecycle) : super(
        fragmentManager,
        lifecycle
    )

    constructor(fragmentActivity: FragmentActivity) : super(fragmentActivity)
    constructor(fragment: Fragment) : super(fragment)

    override fun getItemCount(): Int = 19

    override fun createFragment(position: Int): Fragment {
        return DashboardSeasonFragment.instance(2019 - position)
    }
}