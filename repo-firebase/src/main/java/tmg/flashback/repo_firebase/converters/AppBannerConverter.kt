package tmg.flashback.repo_firebase.converters

import tmg.flashback.repo.models.AppBanner
import tmg.flashback.repo_firebase.models.FAppBanner

fun FAppBanner.convert(): AppBanner {
    return AppBanner(
        this.show ?: false,
        this.message
    )
}