package tmg.flashback.repo.models

data class Response<T>(
    val result: T?,
    val code: Int = if (result != null) 200 else 500
) {
    val isNoNetwork: Boolean = code == -1
}