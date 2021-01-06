package tmg.flashback.constants

object Migrations {

    /**
     * Number of builds that have required a remote config synchronisation
     * Increment this number by 1 when a new build comes out that requires
     *   a remote config fetch before
     */
    const val remoteConfigSyncCount: Int = 1
}