package tmg.flashback.firebase

import java.lang.RuntimeException

/**
 * Exception that's used to wrap issues thrown when data is being parsed from firestore
 */
class FlashbackParseException(msg: String, cause: Throwable): RuntimeException(msg, cause)