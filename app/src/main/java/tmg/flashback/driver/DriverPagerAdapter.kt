package tmg.flashback.driver

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import tmg.flashback.driver.overview.DriverOverviewFragment
import tmg.flashback.driver.season.DriverSeasonFragment

class DriverPagerAdapter(
        val driverId: String,
        activity: AppCompatActivity
): FragmentStateAdapter(activity) {

    var seasons: List<Pair<String, Int>> = listOf()
        set(value) {
            if (field != value) {
                field = value
                notifyDataSetChanged()
            }
        }

    override fun getItemCount(): Int = seasons.size + 1

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) {
            DriverOverviewFragment.instance(driverId)
        }
        else {
            val (driverId, season) = seasons[position - 1]
            DriverSeasonFragment.instance(season, driverId)
        }
    }
}