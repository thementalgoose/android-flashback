package tmg.flashback.dashboard

interface DashboardNavigationCallback {
    fun openSeasonList()
    fun seasonSelected(season: Int)
    fun closeSeasonList()
    fun openSearch()
    fun closeSearch()
}