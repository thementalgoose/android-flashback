package tmg.flashback.firebase.models

internal fun FAppLockout.Companion.model(
    show: Boolean? = false,
    title: String? = "title",
    message: String? = "message",
    linkText: String? = "linkText",
    link: String? = "link",
    version: Int? = 1
): FAppLockout = FAppLockout(
    show = show,
    title = title,
    message = message,
    linkText = linkText,
    link = link,
    version = version
)