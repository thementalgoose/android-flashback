package tmg.flashback.testutils

import tmg.flashback.prefs.manager.PreferenceManager

class FakePreferenceManager: PreferenceManager {

    private val values: MutableMap<String, Any> = mutableMapOf()

    override fun save(key: String, value: Int) { values[key] = value }
    override fun save(key: String, value: String) { values[key] = value }
    override fun save(key: String, value: Long) { values[key] = value }
    override fun save(key: String, value: Float) { values[key] = value }
    override fun save(key: String, value: Boolean) { values[key] = value }
    override fun save(key: String, value: Set<String>) { values[key] = value }

    override fun getInt(key: String, value: Int): Int = (values[key] as? Int) ?: value
    override fun getString(key: String, value: String?): String? = (values[key] as? String) ?: value
    override fun getLong(key: String, value: Long): Long = (values[key] as? Long) ?: value
    override fun getFloat(key: String, value: Float): Float = (values[key] as? Float) ?: value
    override fun getBoolean(key: String, value: Boolean): Boolean = (values[key] as? Boolean) ?: value
    @Suppress("UNCHECKED_CAST")
    override fun getSet(key: String, value: Set<String>): MutableSet<String> = ((values[key] as? Set<String>) ?: value) as MutableSet<String>
}