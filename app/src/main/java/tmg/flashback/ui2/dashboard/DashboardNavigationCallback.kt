package tmg.flashback.ui2.dashboard

interface DashboardNavigationCallback {
    fun openSeasonList()
    fun seasonSelected(season: Int)
    fun closeSeasonList()
}