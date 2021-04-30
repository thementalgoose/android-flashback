package tmg.flashback.core.model

data class ForceUpgrade(
        val title: String,
        val message: String,
        val link: Pair<String, String>?
)