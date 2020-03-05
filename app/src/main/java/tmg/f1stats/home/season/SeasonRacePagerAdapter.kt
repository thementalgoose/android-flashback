package tmg.f1stats.home.season

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter

class SeasonRacePagerAdapter(parent: Fragment): FragmentStateAdapter(parent) {

    override fun getItemCount(): Int = 20

    override fun createFragment(position: Int): Fragment {
        return SeasonRaceFragment()
    }
}