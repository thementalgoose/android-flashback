package tmg.flashback.ui.model

enum class AnimationSpeed(
    val key: String,
    val millis: Int
) {
    NONE("0", 0),
    QUICK("500", 500),
    MEDIUM("1000", 1000),
    SLOW("2000", 2000);
}