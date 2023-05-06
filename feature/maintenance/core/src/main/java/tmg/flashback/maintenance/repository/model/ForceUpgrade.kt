package tmg.flashback.maintenance.repository.model

internal data class ForceUpgrade(
        val title: String,
        val message: String,
        val link: Pair<String, String>? // LinkText, Link
)

