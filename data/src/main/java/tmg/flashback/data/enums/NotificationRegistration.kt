package tmg.flashback.data.enums

enum class NotificationRegistration(
    val key: String
) {
    /**
     * User has opted in to push notifications
     *  or has been auto enrolled into notifications
     */
    OPT_IN("OPT_IN"),

    /**
     * User has opted out of push notifications
     */
    OPT_OUT("OPT_OUT");
}