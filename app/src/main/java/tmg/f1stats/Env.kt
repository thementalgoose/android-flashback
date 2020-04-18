package tmg.f1stats

import tmg.utilities.extensions.toEnum

val env: Env = BuildConfig.ENVIRONMENT.toEnum<Env>() ?: Env.LIVE

enum class Env(
    val id: Int
) {
    SAND(1),
    LIVE(0);
}