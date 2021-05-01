package tmg.configuration.repository.models

data class ForceUpgrade(
        val title: String,
        val message: String,
        val link: Pair<String, String>?
)