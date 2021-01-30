package tmg.flashback.firebase

import java.lang.RuntimeException

class FlashbackParseException(msg: String, cause: Throwable): RuntimeException(msg, cause)