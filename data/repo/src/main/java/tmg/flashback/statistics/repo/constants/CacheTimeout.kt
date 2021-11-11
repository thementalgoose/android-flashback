package tmg.flashback.statistics.repo.constants

internal object CacheTimeout {

    /**
     * How long we should wait between automatically making a network request
     *  to overview and races for the current season
     *
     * If the request has not been made for the following amount of minutes then
     *  the request will next be made when the home screen is updated
     */
    const val timeoutMinutes: Int = 60
}