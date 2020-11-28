package tmg.flashback.firebase.utils

fun <T,E> Map<T, Map<T, E>>.flatten(): List<E> {
    val list: MutableList<E> = mutableListOf()
    for ((sKey, rounds) in this) {
        for ((rKey, round) in rounds) {
            list.add(round)
        }
    }
    return list
}