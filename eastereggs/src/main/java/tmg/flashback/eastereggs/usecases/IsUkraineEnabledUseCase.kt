package tmg.flashback.eastereggs.usecases

import tmg.flashback.eastereggs.repository.EasterEggsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IsUkraineEnabledUseCase @Inject constructor(
    private val easterEggsRepository: EasterEggsRepository
) {
    operator fun invoke(): Boolean {
        return easterEggsRepository.isUkraineEnabled
    }
}