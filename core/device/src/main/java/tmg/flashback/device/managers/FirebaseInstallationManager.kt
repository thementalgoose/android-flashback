package tmg.flashback.device.managers

import com.google.firebase.installations.FirebaseInstallations
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseInstallationManager @Inject constructor() {
    private val installation = FirebaseInstallations.getInstance()

    suspend fun getInstallationId(): String? {
        return try {
            installation.id.await()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}