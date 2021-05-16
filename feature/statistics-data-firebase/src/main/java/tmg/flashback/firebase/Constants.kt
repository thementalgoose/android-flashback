package tmg.flashback.firebase

import org.threeten.bp.Year

internal val currentYear: Int
    get() = Year.now().value