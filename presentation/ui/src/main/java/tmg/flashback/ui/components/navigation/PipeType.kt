package tmg.flashback.ui.components.navigation

enum class PipeType {
    SINGLE,
    START,
    START_END,
    SINGLE_PIPE,
    END;

    val showTop: Boolean
        get() = this == START_END || this == END

    val showBottom: Boolean
        get() = this == START_END || this == START

    val showMiddle: Boolean
        get() = this == START || this == START_END || this == END || this == SINGLE
}