package tmg.common.repository.model

data class ForceUpgrade(
        val title: String,
        val message: String,
        val link: Pair<String, String>? // LinkText, Link
)

