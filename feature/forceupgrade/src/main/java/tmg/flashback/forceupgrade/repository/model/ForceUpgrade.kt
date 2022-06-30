package tmg.flashback.forceupgrade.repository.model

internal data class ForceUpgrade(
        val title: String,
        val message: String,
        val link: Pair<String, String>? // LinkText, Link
)

