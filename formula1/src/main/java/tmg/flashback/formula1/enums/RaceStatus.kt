package tmg.flashback.formula1.enums

import tmg.flashback.formula1.enums.RaceStatus.FINISHED
import tmg.flashback.formula1.enums.RaceStatus.LAPS_1
import tmg.flashback.formula1.enums.RaceStatus.LAPS_2
import tmg.flashback.formula1.enums.RaceStatus.LAPS_3
import tmg.flashback.formula1.enums.RaceStatus.LAPS_4
import tmg.flashback.formula1.enums.RaceStatus.LAPS_5
import tmg.flashback.formula1.enums.RaceStatus.LAPS_6
import tmg.flashback.formula1.enums.RaceStatus.LAPS_7
import tmg.flashback.formula1.enums.RaceStatus.LAPS_8
import tmg.flashback.formula1.enums.RaceStatus.LAPS_9

enum class RaceStatus(
    vararg statuses: String
) {
    FINISHED("Finished", "Finish"),
    LAPS_1("+1 Lap", "+1 Laps"),
    LAPS_2("+2 Laps"),
    LAPS_3("+3 Laps"),
    LAPS_4("+4 Laps"),
    LAPS_5("+5 Laps"),
    LAPS_6("+6 Laps"),
    LAPS_7("+7 Laps"),
    LAPS_8("+8 Laps"),
    LAPS_9("+9 Laps"),
    ENGINE("Engine"),
    WHEEL_NUT("Wheel nut"),
    TYRE("Tyre"),
    WHEEL("Wheel"),
    DAMAGE("Damage"),
    STEERING("Steering"),
    BRAKES("Brakes"),
    VIBRATIONS("Vibrations"),
    SUSPENSION("Suspension"),
    POWER_UNIT("Power Unit"),
    HYDRAULICS("Hydraulics"),
    WATER_LEAK("Water leak"),
    MECHANICAL("Mechanical"),
    GEARBOX("Gearbox"),
    ILLNESS("Illness"),
    DEBRIS("Debris"),
    COLLISION("Collision", "Collision damage"),
    POWER_LOSS("Power loss"),
    WITHDREW("Withdrew"),
    ACCIDENT("Accident"),
    OIL_PRESSURE("Oil pressure"),
    DISQUALIFIED("Disqualified"),
    PUNCTURE("Puncture"),
    ERS("ERS"),
    ELECTRICAL("Electrical"),
    ELECTRONICS("Electronics"),
    ELECTRONIC("Electronic"),
    DRIVESHAFT("Driveshaft"),
    FUEL_PRESSURE("Fuel pressure"),
    SPUN_OFF("Spun off"),
    TURBO("Turbo"),
    FUEL_SYSTEM("Fuel system"),
    TRANSMISSION("Transmission"),
    CLUTCH("Clutch"),
    OIL_LEAK("Oil leak"),
    EXHAUST("Exhaust"),
    DRIVETRAIN("Drivetrain"),
    REAR_WING("Rear wing"),
    FRONT_WING("Front wing"),
    WATER_PRESSURE("Water pressure"),
    SEAT("Seat"),
    BATTERY("Battery"),
    OUT_OF_FUEL("Out of fuel"),
    OVERHEATING("Overheating"),
    SPARK_PLUGS("Spark plugs"),
    THROTTLE("Throttle"),
    UNKNOWN("Unknown");

    private val options: List<String> = statuses.toList()
    val label: String = options.first()
    companion object {
        fun from(status: String): RaceStatus {
            return RaceStatus.values()
                .firstOrNull { raceStatus -> raceStatus.options.any { it.lowercase() == status.lowercase() } }
                ?: UNKNOWN
        }
    }
}

fun RaceStatus.isStatusFinished(): Boolean {
    return this in listOf(
        FINISHED,
        LAPS_1,
        LAPS_2,
        LAPS_3,
        LAPS_4,
        LAPS_5,
        LAPS_6,
        LAPS_7,
        LAPS_8,
        LAPS_9
    )
}