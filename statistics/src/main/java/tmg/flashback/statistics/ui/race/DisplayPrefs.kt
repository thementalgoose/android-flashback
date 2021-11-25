package tmg.flashback.statistics.ui.race

data class DisplayPrefs(
    val q1: Boolean,
    val q2: Boolean,
    val q3: Boolean,
    val deltas: Boolean,
    val penalties: Boolean,
    val fadeDNF: Boolean
) {
    val none: Boolean
        get() = !q1 && !q2 && !q3
}