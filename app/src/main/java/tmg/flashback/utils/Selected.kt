package tmg.flashback.utils

class Selected<T>(
    val value: T,
    val isSelected: Boolean = false
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Selected<*>

        if (isSelected != other.isSelected) return false
        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        var result = isSelected.hashCode()
        result = 31 * result + (value?.hashCode() ?: 0)
        return result
    }
}