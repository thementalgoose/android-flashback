package tmg.f1stats.season

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import tmg.f1stats.season.race.SeasonRaceFragment

class SeasonPagerAdapter(parent: Fragment): FragmentStateAdapter(parent) {

    var list: List<SeasonAdapterModel> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = list.size

    override fun createFragment(position: Int): Fragment {
        return SeasonRaceFragment.instance(list[position].season, list[position].round)
    }
}