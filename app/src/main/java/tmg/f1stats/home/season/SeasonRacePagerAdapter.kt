package tmg.f1stats.home.season

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter

class SeasonRacePagerAdapter(parent: Fragment): FragmentStateAdapter(parent) {

    var list: List<SeasonRaceModel> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = list.size

    override fun createFragment(position: Int): Fragment {
        return SeasonRaceFragment(list[position])
    }
}