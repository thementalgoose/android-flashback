package tmg.flashback.driver

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import tmg.flashback.driver.season.DriverSeasonFragment

class DriverPagerAdapter(activity: AppCompatActivity): FragmentStateAdapter(activity) {

    var seasons: List<Pair<String, Int>> = listOf(Pair("perez", 2019), Pair("perez", 2020))
        set(value) {
            if (field != value) {
                field = value
                notifyDataSetChanged()
            }
        }

    override fun getItemCount(): Int = seasons.size

    override fun createFragment(position: Int): Fragment {
        val (driverId, season) = seasons[position]
        return DriverSeasonFragment.instance(season, driverId)
    }
}