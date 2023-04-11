package tmg.flashback.formula1.enums

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import tmg.flashback.formula1.R
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
    private val options: List<String>,
    @DrawableRes
    private val icon: Int,
//    @StringRes
//    private val label: Int
) {
    FINISHED(listOf("Finished", "Finish"), R.drawable.ic_status_finished),
    LAPS_1(listOf("+1 Lap", "+1 Laps"), R.drawable.ic_status_lap_1),
    LAPS_2(listOf("+2 Laps"), R.drawable.ic_status_lap_2),
    LAPS_3(listOf("+3 Laps"), R.drawable.ic_status_lap_3),
    LAPS_4(listOf("+4 Laps"), R.drawable.ic_status_lap_4),
    LAPS_5(listOf("+5 Laps"), R.drawable.ic_status_lap_5),
    LAPS_6(listOf("+6 Laps"), R.drawable.ic_status_lap_6),
    LAPS_7(listOf("+7 Laps"), R.drawable.ic_status_lap_7),
    LAPS_8(listOf("+8 Laps"), R.drawable.ic_status_lap_8),
    LAPS_9(listOf("+9 Laps"), R.drawable.ic_status_lap_9),
    ENGINE(listOf("Engine"), R.drawable.ic_status_engine),
    WHEEL_NUT(listOf("Wheel nut"), R.drawable.ic_status_wheel_nut),
    TYRE(listOf("Tyre"), R.drawable.ic_status_wheel),
    WHEEL(listOf("Wheel"), R.drawable.ic_status_wheel),
    DAMAGE(listOf("Damage"), R.drawable.ic_status_damage),
    STEERING(listOf("Steering"), R.drawable.ic_status_steering),
    BRAKES(listOf("Brakes"), R.drawable.ic_status_brakes),
    VIBRATIONS(listOf("Vibrations"), R.drawable.ic_status_vibrations),
    SUSPENSION(listOf("Suspension"), R.drawable.ic_status_suspension),
    POWER_UNIT(listOf("Power Unit"), R.drawable.ic_status_power_unit),
    HYDRAULICS(listOf("Hydraulics"), R.drawable.ic_status_hydraulic),
    WATER_LEAK(listOf("Water leak"), R.drawable.ic_status_water_leak),
    MECHANICAL(listOf("Mechanical"), R.drawable.ic_status_mechanical),
    GEARBOX(listOf("Gearbox"), R.drawable.ic_status_gearbox),
    ILLNESS(listOf("Illness"), R.drawable.ic_status_illness),
    DEBRIS(listOf("Debris"), R.drawable.ic_status_damage),
    COLLISION(listOf("Collision", "Collision damage"), R.drawable.ic_status_collision),
    POWER_LOSS(listOf("Power loss"), R.drawable.ic_status_power_loss),
    WITHDREW(listOf("Withdrew", "Retired"), R.drawable.ic_status_retired),
    ACCIDENT(listOf("Accident"), R.drawable.ic_status_accident),
    OIL_PRESSURE(listOf("Oil pressure"), R.drawable.ic_status_oil_pressure),
    DISQUALIFIED(listOf("Disqualified"), R.drawable.ic_status_disqualified),
    PUNCTURE(listOf("Puncture"), R.drawable.ic_status_puncture),
    ERS(listOf("ERS"), R.drawable.ic_status_electrical),
    ELECTRICAL(listOf("Electrical"), R.drawable.ic_status_electrical),
    ELECTRONICS(listOf("Electronics"), R.drawable.ic_status_electrical),
    ELECTRONIC(listOf("Electronic"), R.drawable.ic_status_electrical),
    DRIVESHAFT(listOf("Driveshaft"), R.drawable.ic_status_driveshaft),
    FUEL_PRESSURE(listOf("Fuel pressure"), R.drawable.ic_status_fuel_system),
    SPUN_OFF(listOf("Spun off"), R.drawable.ic_status_spun_off),
    TURBO(listOf("Turbo"), R.drawable.ic_status_turbo),
    FUEL_SYSTEM(listOf("Fuel system"), R.drawable.ic_status_fuel_system),
    TRANSMISSION(listOf("Transmission"), R.drawable.ic_status_transmission),
    CLUTCH(listOf("Clutch"), R.drawable.ic_status_clutch),
    OIL_LEAK(listOf("Oil leak"), R.drawable.ic_status_oil_leak),
    EXHAUST(listOf("Exhaust"), R.drawable.ic_status_exhaust),
    DRIVETRAIN(listOf("Drivetrain"), R.drawable.ic_status_driveshaft),
    REAR_WING(listOf("Rear wing"), R.drawable.ic_status_rear_wing),
    FRONT_WING(listOf("Front wing"), R.drawable.ic_status_front_wing),
    WATER_PRESSURE(listOf("Water pressure"), R.drawable.ic_status_water_pressure),
    SEAT(listOf("Seat"), R.drawable.ic_status_seat),
    BATTERY(listOf("Battery"), R.drawable.ic_status_battery),
    OUT_OF_FUEL(listOf("Out of fuel"), R.drawable.ic_status_fuel_system),
    OVERHEATING(listOf("Overheating"), R.drawable.ic_status_fire),
    SPARK_PLUGS(listOf("Spark plugs"), R.drawable.ic_status_power_loss),
    THROTTLE(listOf("Throttle"), R.drawable.ic_status_turbo),
    UNKNOWN(listOf("Unknown"), R.drawable.ic_status_unknown);

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