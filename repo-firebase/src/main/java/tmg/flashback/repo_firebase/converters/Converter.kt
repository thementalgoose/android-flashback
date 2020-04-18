package tmg.flashback.repo_firebase.converters

fun <T, E> Map<String, T>.toModels(convert: (model: T) -> E): List<E> {
    return this.map {
        convert(it.value)
    }
}