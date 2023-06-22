package tmg.flashback.newrelic.services

import android.content.Context

interface NewRelicService {
    fun start(applicationContext: Context)
}