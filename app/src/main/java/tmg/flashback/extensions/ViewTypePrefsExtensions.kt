package tmg.flashback.extensions

import tmg.flashback.R
import tmg.flashback.repo.enums.ViewTypePref

val ViewTypePref.label: Int
    get() = when (this) {
        ViewTypePref.STATIC -> R.string.view_type_static
    }