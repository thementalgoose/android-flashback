package tmg.f1stats.repo.utils

import tmg.f1stats.repo.Optional

fun <T> List<T>.firstOrOptional(callback: (item: T) -> Boolean): Optional<T> {
    return Optional(firstOrNull(callback))
}