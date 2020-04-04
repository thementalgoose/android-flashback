package tmg.f1stats.extensions

import tmg.f1stats.R
import tmg.f1stats.repo.enums.ViewTypePref

val ViewTypePref.label: Int
    get() = when (this) {
        ViewTypePref.SWIPING -> R.string.view_type_swiping
        ViewTypePref.STATIC -> R.string.view_type_static
    }