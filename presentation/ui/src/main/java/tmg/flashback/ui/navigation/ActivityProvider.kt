package tmg.flashback.ui.navigation

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import tmg.flashback.crash_reporting.controllers.CrashController
import java.lang.ref.WeakReference
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ActivityProvider @Inject constructor(
    private val crashController: CrashController
): Application.ActivityLifecycleCallbacks {

    private var _activity: WeakReference<Activity>? = null
    private var startCount = 0

    val isForeground: Boolean
        get() = startCount != 0

    val activity: Activity?
        @Synchronized
        get() = _activity?.get()

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        update(activity, "CREATED")
    }

    override fun onActivityStarted(activity: Activity) {
        update(activity, "STARTED")
        startCount++
    }

    override fun onActivityResumed(activity: Activity) {
        /**
         * We should not use onActivityResumed from Android 10 because multi-resume
         *  could lead to the wrong activity being set. Other methods (onActivityStarted,
         *  onWindowFocusObtained) ensure that the top activity is still correct even
         *  when not using onActivityResumed but we still maintain old behavior in older
         *  versions of Android just to be safe.
         */
        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            update(activity, "RESUMED")
        }
    }

    fun onWindowFocusObtained(activity: Activity) {
        /**
         * onActivityResumed is NOT ENOUGH to determine the current activity in focus
         *
         * The javadoc of Activity#onResume() hints at relying on this method to
         *  determine the top activity
         */
        update(activity, "WINDOW_FOCUS_OBTAINED")
    }

    override fun onActivityStopped(activity: Activity) {
        startCount--
    }

    override fun onActivityPaused(activity: Activity) { }
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) { }
    override fun onActivityDestroyed(activity: Activity) { }

    fun intent(callback: (activity: Activity) -> Intent): Intent? {
        return activity?.let { callback(it) }
    }
    fun launch(callback: (activity: Activity) -> Unit) {
        activity?.let { callback(it) }
    }

    @Synchronized
    private fun update(activity: Activity, lifecycleState: String) {
        this._activity = WeakReference(activity)
        Log.i("Activity", "Top Activity updated to ${activity.javaClass.simpleName}")
        crashController.log("Activity ${activity.javaClass.simpleName}@${Integer.toHexString(activity.hashCode())} (event=${lifecycleState})")
    }
}