package tmg.flashback.formula1.enums

import androidx.annotation.DrawableRes
import tmg.flashback.formula1.R

/**
 * Supported track layouts
 */
enum class TrackLayout(
    val circuitId: String,
    @DrawableRes
    private val icon: Int,
    private vararg val overrides: Configuration,
) {
    ADELAIDE("adelaide", R.drawable.circuit_adelaide),
    AINTREE("aintree", R.drawable.circuit_aintree),
    AIN_DIAB("ain-diab", R.drawable.circuit_ain_diab),
    ALBERT_PARK("albert_park", R.drawable.circuit_albert_park),
    ALGARVE("algarve", R.drawable.circuit_algarve),
    ANDERSTORP("anderstorp", R.drawable.circuit_anderstorp),
    AVUS("avus", R.drawable.circuit_avus),
    PORTIMAO("portimao", R.drawable.circuit_algarve),
    AMERICAS("americas", R.drawable.circuit_americas),
    BUDDH("buddh", R.drawable.circuit_buddh),
    BAHRAIN("bahrain", R.drawable.circuit_bahrain,
        Configuration.OneOff(2010, _icon = R.drawable.circuit_bahrain_2010),
        Configuration.OneOff(2020, name = "Sakhir Grand Prix", _icon = R.drawable.circuit_sakhir)
    ),
    BAK("BAK", R.drawable.circuit_bak),
    BREMGARTEN("bremgarten", R.drawable.circuit_bremgarten),
    BRANDS_HATCH("brands_hatch", R.drawable.circuit_brands_hatch),
    BOAVISTA("boavista", R.drawable.circuit_boavista),
    CATALUNYA("catalunya", R.drawable.circuit_catalunya),
    CATALUNYA_1991_2022("catalunya", R.drawable.circuit_catalunya_1991_2022),
    CHARADE("charade", R.drawable.circuit_charade),
    DALLAS("dallas", R.drawable.circuit_dallas),
    DETROIT("detroit", R.drawable.circuit_detroit),
    DIJON("dijon", R.drawable.circuit_dijon),
    DONINGTON("donington", R.drawable.circuit_donington),
    ESTORIL("estoril", R.drawable.circuit_estoril),
    ESSARTS("essarts", R.drawable.circuit_essarts),
    FAIR_PARK("fair_park", R.drawable.circuit_fair_park),
    FUJI("fuji", R.drawable.circuit_fuji),
    GALVEZ("galvez", R.drawable.circuit_galvez,
        Configuration.Range(min = 1974, max = 1981, _icon = R.drawable.circuit_galvez_1974_1981)
    ),
    GEORGE("george", R.drawable.circuit_george),
    HANOI("hanoi", R.drawable.circuit_hanoi),
    HOCKENHEIMRING("hockenheimring", R.drawable.circuit_hockenheimring),
    HUNGARORING("hungaroring", R.drawable.circuit_hungaroring),
    INDIANAPOLIS("indianapolis", R.drawable.circuit_indianapolis),
    INTERLAGOS("interlagos", R.drawable.circuit_interlagos),
    IMOLA("imola", R.drawable.circuit_imola,
        Configuration.Range(min = 1995, max = 2006, _icon = R.drawable.circuit_imola_1995_2006),
        Configuration.Range(min = 1980, max = 1994, _icon = R.drawable.circuit_imola_1980_1994),
    ),
    ISTANBUL("istanbul", R.drawable.circuit_istanbul),
    JACAREPAGUA("jacarepagua", R.drawable.circuit_jacarepagua),
    JARAMA("jarama", R.drawable.circuit_jarama),
    JEDDAH("jeddah", R.drawable.circuit_jeddah),
    JEREZ("jerez", R.drawable.circuit_jerez),
    KYALAMI("kyalami", R.drawable.circuit_kyalami),
    LAS_VEGAS("las_vegas", R.drawable.circuit_las_vegas_1981_1982),
    LEMANS("lemans", R.drawable.circuit_lemans),
    LONG_BEACH("long_beach", R.drawable.circuit_long_beach),
    LOSAIL("losail", R.drawable.circuit_losail),
    MAGNY_COURS("magny_cours", R.drawable.circuit_magny_cours),
    MARINA_BAY("marina_bay", R.drawable.circuit_marina_bay,
        Configuration.Range(min = 2008, max = 2022, _icon = R.drawable.circuit_marina_bay_2008_2022)
    ),
    MIAMI("miami", R.drawable.circuit_miami),
    MONACO("monaco", R.drawable.circuit_monaco),
    MONZA("monza", R.drawable.circuit_monza),
    MONSANTO("monsanto", R.drawable.circuit_monsanto),
    MONTJUIC("montjuic", R.drawable.circuit_montjuic),
    MOSPORT("mosport", R.drawable.circuit_mosport),
    MUGELLO("mugello", R.drawable.circuit_mugello),
    NIVELLES("nivelles", R.drawable.circuit_nivelles),
    NURBURGRING("nurburgring", R.drawable.circuit_nurburgring),
    OKAYAMA("okayama", R.drawable.circuit_okayama),
    OSTERREICHRING("osterreichring", R.drawable.circuit_osterreichring),
    PESCARA("pescara", R.drawable.circuit_pescara),
    PEDRALBES("pedralbes", R.drawable.circuit_pedralbes),
    PHOENIX("phoenix", R.drawable.circuit_phoenix),
    PORT_IMPERIAL("port_imperial", R.drawable.circuit_port_imperial),
    RED_BULL_RING("red_bull_ring", R.drawable.circuit_red_bull_ring),
    REIMS("reims", R.drawable.circuit_reims),
    RICARD("ricard", R.drawable.circuit_ricard),
    RIVERSIDE("riverside", R.drawable.circuit_riverside),
    RODRIGUEZ("rodriguez", R.drawable.circuit_rodriguez),
    SEBRING("sebring", R.drawable.circuit_sebring),
    SEPANG("sepang", R.drawable.circuit_sepang),
    SHANGHAI("shanghai", R.drawable.circuit_shanghai),
    SILVERSTONE("silverstone", R.drawable.circuit_silverstone,
        Configuration.Range(min = 1997, max = 2009, _icon = R.drawable.circuit_silverstone_1997_2009),
        Configuration.OneOff(year = 1996, _icon = R.drawable.circuit_silverstone_1996),
        Configuration.Range(min = 1994, max = 1995, _icon = R.drawable.circuit_silverstone_1994_1995),
        Configuration.Range(min = 1991, max = 1993, _icon = R.drawable.circuit_silverstone_1991_1993),
        Configuration.Range(min = 1987, max = 1990, _icon = R.drawable.circuit_silverstone_1987_1990),
        Configuration.Range(min = 1975, max = 1986, _icon = R.drawable.circuit_silverstone_1975_1986),
        Configuration.Range(min = 1950, max = 1973, _icon = R.drawable.circuit_silverstone_1950_1973)
    ),
    SOCHI("sochi", R.drawable.circuit_sochi),
    SPA("spa", R.drawable.circuit_spa),
    SUZUKA("suzuka", R.drawable.circuit_suzuka),
    TREMBLANT("tremblant", R.drawable.circuit_tremblant),
    VALENCIA("valencia", R.drawable.circuit_valencia),
    VEGAS("vegas", R.drawable.circuit_vegas),
    VILLENEUVE("villeneuve", R.drawable.circuit_villeneuve),
    WATKINS_GLEN("watkins_glen", R.drawable.circuit_watkins_glen),
    YAS_MARINA("yas_marina", R.drawable.circuit_yas_marina,
        Configuration.Range(min = 2009, max = 2020, R.drawable.circuit_yas_marina_2009_2020)
    ),
    YEONGAM("yeongam", R.drawable.circuit_yeongam),
    ZANDVOORT("zandvoort", R.drawable.circuit_zandvoort),
    ZELTWEG("zeltweg", R.drawable.circuit_zeltweg),
    ZOLDER("zolder", R.drawable.circuit_zolder);

    @DrawableRes
    fun getDefaultIcon(): Int {
        return icon
    }

    @DrawableRes
    fun getIcon(year: Int, name: String): Int {
        if (overrides.isEmpty()) {
            return icon
        }

        val override = overrides
            .firstOrNull { option ->
                return@firstOrNull when (option) {
                    is Configuration.OneOff -> if (option.name != null) {
                        year == option.year && name == option.name
                    } else {
                        year == option.year
                    }
                    is Configuration.Range -> year <= option.max && year >= option.min
                }
            }
        if (override != null) {
            return override.icon
        } else {
            return icon
        }
    }

    companion object {

        fun getTrack(circuitId: String): TrackLayout? {
            return TrackLayout.values().firstOrNull { it.circuitId == circuitId }
        }
    }
}

sealed class Configuration(
    @DrawableRes
    val icon: Int
){

    data class OneOff(
        val year: Int,
        val name: String? = null,
        @DrawableRes
        private val _icon: Int
    ): Configuration(_icon)

    data class Range(
        val min: Int,
        val max: Int,
        @DrawableRes
        private val _icon: Int
    ): Configuration(_icon)
}