package tmg.flashback.formula1.enums

import androidx.annotation.DrawableRes
import tmg.flashback.formula1.R

/**
 * Supported track layouts
 */
enum class TrackLayout(
        val circuitId: String,
        @DrawableRes
        val icon: Int,
        val override: List<String> = emptyList() // "2020 Sakhir Grand Prix" - Should be "${year} ${raceName}". Update unit tests!
) {
    ADELAIDE("adelaide", R.drawable.circuit_adelaide),
    ALBERT_PARK("albert_park", R.drawable.circuit_albert_park),
    ALGARVE("algarve", R.drawable.circuit_algarve),
    PORTIMAO("portimao", R.drawable.circuit_algarve),
    AMERICAS("americas", R.drawable.circuit_americas),
    BUDDH("buddh", R.drawable.circuit_buddh),
    BAHRAIN("bahrain", R.drawable.circuit_bahrain),
    BAK("BAK", R.drawable.circuit_bak),
    CATALUNYA("catalunya", R.drawable.circuit_catalunya),
    DONINGTON("donington", R.drawable.circuit_donington),
    ESTORIL("estoril", R.drawable.circuit_estoril),
    FAIR_PARK("fair_park", R.drawable.circuit_fair_park),
    FUJI("fuji", R.drawable.circuit_fuji),
    GEORGE("george", R.drawable.circuit_george),
    HANOI("hanoi", R.drawable.circuit_hanoi),
    HOCKENHEIMRING("hockenheimring", R.drawable.circuit_hockenheimring),
    HUNGARORING("hungaroring", R.drawable.circuit_hungaroring),
    INDIANAPOLIS("indianapolis", R.drawable.circuit_indianapolis),
    INTERLAGOS("interlagos", R.drawable.circuit_interlagos),
    IMOLA("imola", R.drawable.circuit_imola),
    ISTANBUL("istanbul", R.drawable.circuit_istanbul),
    JACAREPAGUA("jacarepagua", R.drawable.circuit_jacarepagua),
    JEDDAH("jeddah", R.drawable.circuit_jeddah),
    JEREZ("jerez", R.drawable.circuit_jerez),
    LOSAIL("losail", R.drawable.circuit_losail),
    MAGNY_COURS("magny_cours", R.drawable.circuit_magny_cours),
    MARINA_BAY("marina_bay", R.drawable.circuit_marina_bay),
    MONACO("monaco", R.drawable.circuit_monaco),
    MONZA("monza", R.drawable.circuit_monza),
    MUGELLO("mugello", R.drawable.circuit_mugello),
    NURBURGRING("nurburgring", R.drawable.circuit_nurburgring),
    RED_BULL_RING("red_bull_ring", R.drawable.circuit_red_bull_ring),
    RICARD("ricard", R.drawable.circuit_ricard),
    RODRIGUEZ("rodriguez", R.drawable.circuit_rodriguez),
    SAKHIR("sakhir", R.drawable.circuit_sakhir, listOf(
            "2020 Sakhir Grand Prix"
    )),
    SEPANG("sepang", R.drawable.circuit_sepang),
    SHANGHAI("shanghai", R.drawable.circuit_shanghai),
    SILVERSTONE("silverstone", R.drawable.circuit_silverstone),
    SOCHI("sochi", R.drawable.circuit_sochi),
    SPA("spa", R.drawable.circuit_spa),
    SUZUKA("suzuka", R.drawable.circuit_suzuka),
    VALENCIA("valencia", R.drawable.circuit_valencia),
    VILLENEUVE("villeneuve", R.drawable.circuit_villeneuve),
    YAS_MARINA("yas_marina", R.drawable.circuit_yas_marina),
    YEONGAM("yeongam", R.drawable.circuit_yeongam),
    ZANDVOORT("zandvoort", R.drawable.circuit_zandvoort);

    companion object {

        fun getTrack(circuitId: String?, year: Int? = null, raceName: String? = null): TrackLayout? {
            if (year != null && raceName != null) {
                return getOverride(year, raceName) ?: getTrack(circuitId)
            }
            return getTrack(circuitId)
        }

        private fun getTrack(circuitId: String?): TrackLayout? {
            return TrackLayout
                .values()
                .firstOrNull { it.circuitId == circuitId }
        }

        private fun getOverride(year: Int, raceName: String): TrackLayout? {
            return TrackLayout
                    .values()
                    .firstOrNull { it.override.contains("$year $raceName") }
        }
    }
}