package tmg.flashback.repo.enums

enum class RaceStatus(
    val statusCode: String
) {
    FINISHED("FINISHED"),
    LAP_1("LAP_1"),
    LAP_2("LAP_2"),
    LAP_3("LAP_3"),
    LAP_4("LAP_4"),
    LAP_5("LAP_5"),
    LAP_6("LAP_6"),
    LAP_7("LAP_7"),
    LAP_8("LAP_8"),
    LAP_9("LAP_9"),
    DAMAGE("DAMAGE"),
    DRIVESHAFT("DRIVESHAFT"),
    FUEL_PRESSURE("FUEL_PRESSURE"),
    WHEEL("WHEEL"),
    ENGINE("ENGINE"),
    SUSPENSION("SUSPENSION"),
    RETIRED("RETIRED"),
    STEERING("STEERING"),
    BRAKES("BRAKES"),
    VIBRATIONS("VIBRATIONS"),
    POWER_UNIT("POWER_UNIT"),
    HYDRAULICS("HYDRAULICS"),
    WATER_LEAK("WATER_LEAK"),
    MECHANICAL("MECHANICAL"),
    COLLISION("COLLISION"),
    POWER_LOSS("POWER_LOSS"),
    WITHDREW("WITHDREW"),
    ACCIDENT("ACCIDENT"),
    DISQUALIFIED("DISQUALIFIED"),
    OIL_PRESSURE("OIL_PRESSURE"),
    ERS("ERS"),
    PUNCTURE("PUNCTURE"),
    ELECTRICAL("ELECTRICAL"),
    TURBO("TURBO"),
    SPUN_OFF("SPUN_OFF"),
    FUEL_SYSTEM("FUEL_SYSTEM"),
    GEARBOX("GEARBOX"),
    TRANSMISSION("TRANSMISSION"),
    CLUTCH("CLUTCH"),
    OIL_LEAK("OIL_LEAK"),
    EXHAUST("EXHAUST"),
    DRIVETRAIN("DRIVETRAIN"),
    REAR_WING("REAR_WING"),
    WATER_PRESSURE("WATER_PRESSURE"),
    SEAT("SEAT"),
    BATTERY("BATTERY"),
    FRONT_WING("FRONT_WING");

    companion object {
        fun fromStatus(status: String): RaceStatus {
            return values().firstOrNull { it.statusCode == status } ?: RETIRED
        }
    }
}