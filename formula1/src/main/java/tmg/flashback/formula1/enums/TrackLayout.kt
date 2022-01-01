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
    AINTREE("aintree", R.drawable.circuit_aintree),
    AIN_DIAB("ain-diab", R.drawable.circuit_ain_diab),
    ADELAIDE("adelaide", R.drawable.circuit_adelaide),
    ALBERT_PARK("albert_park", R.drawable.circuit_albert_park),
    ALGARVE("algarve", R.drawable.circuit_algarve),
    AVUS("avus", R.drawable.circuit_avus),
    PORTIMAO("portimao", R.drawable.circuit_algarve),
    AMERICAS("americas", R.drawable.circuit_americas),
    BUDDH("buddh", R.drawable.circuit_buddh),
    BAHRAIN("bahrain", R.drawable.circuit_bahrain),
    BAK("BAK", R.drawable.circuit_bak),
    BREMGARTEN("bremgarten", R.drawable.circuit_bremgarten),
    BRANDS_HATCH("brands_hatch", R.drawable.circuit_brands_hatch),
    BOAVISTA("boavista", R.drawable.circuit_boavista),
    CATALUNYA("catalunya", R.drawable.circuit_catalunya),
    CHARADE("charade", R.drawable.circuit_charade),
    DALLAS("dallas", R.drawable.circuit_dallas),
    DETROIT("detroit", R.drawable.circuit_detroit),
    DIJON("dijon", R.drawable.circuit_dijon),
    DONINGTON("donington", R.drawable.circuit_donington),
    ESTORIL("estoril", R.drawable.circuit_estoril),
    FAIR_PARK("fair_park", R.drawable.circuit_fair_park),
    FUJI("fuji", R.drawable.circuit_fuji),
    GALVEZ("galvez", R.drawable.circuit_galvez),
    GALVEZ_1974_1981("galvez", R.drawable.circuit_galvez_1974_1981, listOf(
        "1974 Argentine Grand Prix",
        "1975 Argentine Grand Prix",
        "1976 Argentine Grand Prix",
        "1977 Argentine Grand Prix",
        "1978 Argentine Grand Prix",
        "1979 Argentine Grand Prix",
        "1980 Argentine Grand Prix",
        "1981 Argentine Grand Prix"
    )),
    GEORGE("george", R.drawable.circuit_george),
    HANOI("hanoi", R.drawable.circuit_hanoi),
    HOCKENHEIMRING("hockenheimring", R.drawable.circuit_hockenheimring),
    HUNGARORING("hungaroring", R.drawable.circuit_hungaroring),
    INDIANAPOLIS("indianapolis", R.drawable.circuit_indianapolis),
    INTERLAGOS("interlagos", R.drawable.circuit_interlagos),
    IMOLA("imola", R.drawable.circuit_imola),
    ISTANBUL("istanbul", R.drawable.circuit_istanbul),
    JACAREPAGUA("jacarepagua", R.drawable.circuit_jacarepagua),
    JARAMA("jarama", R.drawable.circuit_jarama),
    JEDDAH("jeddah", R.drawable.circuit_jeddah),
    JEREZ("jerez", R.drawable.circuit_jerez),
    KYALAMI("kyalami", R.drawable.circuit_kyalami),
    LAS_VEGAS("las_vegas", R.drawable.circuit_las_vegas),
    LEMANS("lemans", R.drawable.circuit_lemans),
    LONG_BEACH("long_beach", R.drawable.circuit_long_beach),
    LOSAIL("losail", R.drawable.circuit_losail),
    MAGNY_COURS("magny_cours", R.drawable.circuit_magny_cours),
    MARINA_BAY("marina_bay", R.drawable.circuit_marina_bay),
    MIAMI("miami", R.drawable.circuit_miami),
    MONACO("monaco", R.drawable.circuit_monaco),
    MONZA("monza", R.drawable.circuit_monza),
    MONSANTO("monsanto", R.drawable.circuit_monsanto),
    MOSPORT("mosport", R.drawable.circuit_mosport),
    MUGELLO("mugello", R.drawable.circuit_mugello),
    NURBURGRING("nurburgring", R.drawable.circuit_nurburgring),
    OSTERREICHRING("osterreichring", R.drawable.circuit_osterreichring),
    PESCARA("pescara", R.drawable.circuit_pescara),
    PEDRALBES("pedralbes", R.drawable.circuit_pedralbes),
    PHOENIX("phoenix", R.drawable.circuit_phoenix),
    PORT_IMPERIAL("port_imperial", R.drawable.circuit_port_imperial),
    RED_BULL_RING("red_bull_ring", R.drawable.circuit_red_bull_ring),
    RICARD("ricard", R.drawable.circuit_ricard),
    RIVERSIDE("riverside", R.drawable.circuit_riverside),
    RODRIGUEZ("rodriguez", R.drawable.circuit_rodriguez),
    SAKHIR("sakhir", R.drawable.circuit_sakhir, listOf(
            "2020 Sakhir Grand Prix"
    )),
    SEBRING("sebring", R.drawable.circuit_sebring),
    SEPANG("sepang", R.drawable.circuit_sepang),
    SHANGHAI("shanghai", R.drawable.circuit_shanghai),
    SILVERSTONE("silverstone", R.drawable.circuit_silverstone),
    SOCHI("sochi", R.drawable.circuit_sochi),
    SPA("spa", R.drawable.circuit_spa),
    SUZUKA("suzuka", R.drawable.circuit_suzuka),
    TREMBLANT("tremblant", R.drawable.circuit_tremblant),
    VALENCIA("valencia", R.drawable.circuit_valencia),
    VILLENEUVE("villeneuve", R.drawable.circuit_villeneuve),
    WATKINS_GLEN("watkins_glen", R.drawable.circuit_watkins_glen),
    YAS_MARINA("yas_marina", R.drawable.circuit_yas_marina),
    YAS_MARINA_2009_2020("yas_marina", R.drawable.circuit_yas_marina_2009_2020, listOf(
        "2009 Abu Dhabi Grand Prix",
        "2010 Abu Dhabi Grand Prix",
        "2011 Abu Dhabi Grand Prix",
        "2012 Abu Dhabi Grand Prix",
        "2013 Abu Dhabi Grand Prix",
        "2014 Abu Dhabi Grand Prix",
        "2015 Abu Dhabi Grand Prix",
        "2016 Abu Dhabi Grand Prix",
        "2017 Abu Dhabi Grand Prix",
        "2018 Abu Dhabi Grand Prix",
        "2019 Abu Dhabi Grand Prix",
        "2020 Abu Dhabi Grand Prix"
    )),
    YEONGAM("yeongam", R.drawable.circuit_yeongam),
    ZANDVOORT("zandvoort", R.drawable.circuit_zandvoort),
    ZOLDER("zolder", R.drawable.circuit_zolder);

    companion object {

        fun getTrack(circuitId: String?, year: Int? = null, raceName: String? = null): TrackLayout? {
            if (year != null && raceName != null) {
                return getOverride(year, raceName) ?: getTrack(circuitId)
            }
            return getTrack(circuitId)
        }

        private fun getTrack(circuitId: String?): TrackLayout? {
            return values()
                .firstOrNull { it.circuitId == circuitId }
        }

        private fun getOverride(year: Int, raceName: String): TrackLayout? {
            return values()
                    .firstOrNull { it.override.contains("$year $raceName") }
        }
    }
}