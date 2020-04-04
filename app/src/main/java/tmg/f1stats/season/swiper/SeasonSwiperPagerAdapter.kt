package tmg.f1stats.season.swiper

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import tmg.f1stats.season.race.RaceFragment

class SeasonSwiperPagerAdapter(parent: Fragment): FragmentStateAdapter(parent) {

    var list: List<SeasonSwiperAdapterModel> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = list.size

    override fun createFragment(position: Int): Fragment {
        return RaceFragment.instance(list[position].season, list[position].round)
    }
}