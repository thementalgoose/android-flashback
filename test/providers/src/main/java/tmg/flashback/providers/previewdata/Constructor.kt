package tmg.flashback.providers.previewdata

import android.graphics.Color
import tmg.flashback.formula1.model.Constructor

internal fun Constructor.Companion.model(
    id: String = "constructorId",
    name: String = "name",
    photoUrl: String? = "photoUrl",
    wikiUrl: String? = "wikiUrl",
    nationality: String = "nationality",
    nationalityISO: String = "nationalityISO",
    color: Int = Color.CYAN,
): Constructor = Constructor(
    id = id,
    name = name,
    photoUrl = photoUrl,
    wikiUrl = wikiUrl,
    nationality = nationality,
    nationalityISO = nationalityISO,
    color = color
)