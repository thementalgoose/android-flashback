package tmg.flashback.extensions

import tmg.flashback.R
import tmg.flashback.repo.enums.ViewTypePref

val ViewTypePref.label: Int
    get() = when (this) {
        ViewTypePref.SWIPING -> R.string.view_type_swiping
        ViewTypePref.STATIC -> R.string.view_type_static
    }