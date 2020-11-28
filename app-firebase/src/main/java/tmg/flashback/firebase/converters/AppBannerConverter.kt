package tmg.flashback.firebase.converters

import tmg.flashback.repo.models.AppBanner
import tmg.flashback.firebase.models.FAppBanner

fun FAppBanner.convert(): AppBanner {
    return AppBanner(
        this.show ?: false,
        this.message
    )
}