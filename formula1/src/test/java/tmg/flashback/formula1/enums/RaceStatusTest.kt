package tmg.flashback.formula1.enums

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import tmg.flashback.formula1.enums.RaceStatus.TRANSMISSION
import tmg.flashback.formula1.enums.RaceStatus.UNKNOWN

internal class RaceStatusTest {

    @ParameterizedTest
    @CsvSource(
        "FINISHED,true,Finished",
        "FINISHED,true,Finish",
        "LAPS_1,true,+1 Lap",
        "LAPS_1,true,+1 Laps",
        "LAPS_2,true,+2 Laps",
        "LAPS_3,true,+3 Laps",
        "LAPS_4,true,+4 Laps",
        "LAPS_5,true,+5 Laps",
        "LAPS_6,true,+6 Laps",
        "LAPS_7,true,+7 Laps",
        "LAPS_8,true,+8 Laps",
        "LAPS_9,true,+9 Laps",
        "ENGINE,false,Engine",
        "WHEEL_NUT,false,Wheel nut",
        "TYRE,false,Tyre",
        "WHEEL,false,Wheel",
        "DAMAGE,false,Damage",
        "STEERING,false,Steering",
        "BRAKES,false,Brakes",
        "VIBRATIONS,false,Vibrations",
        "SUSPENSION,false,Suspension",
        "POWER_UNIT,false,Power Unit",
        "HYDRAULICS,false,Hydraulics",
        "WATER_LEAK,false,Water leak",
        "MECHANICAL,false,Mechanical",
        "GEARBOX,false,Gearbox",
        "ILLNESS,false,Illness",
        "DEBRIS,false,Debris",
        "COLLISION,false,Collision",
        "COLLISION,false,Collision damage",
        "POWER_LOSS,false,Power loss",
        "WITHDREW,false,Withdrew",
        "ACCIDENT,false,Accident",
        "OIL_PRESSURE,false,Oil pressure",
        "DISQUALIFIED,false,Disqualified",
        "PUNCTURE,false,Puncture",
        "ERS,false,ERS",
        "ELECTRICAL,false,Electrical",
        "ELECTRONICS,false,Electronics",
        "ELECTRONIC,false,Electronic",
        "DRIVESHAFT,false,Driveshaft",
        "FUEL_PRESSURE,false,Fuel pressure",
        "SPUN_OFF,false,Spun off",
        "TURBO,false,Turbo",
        "FUEL_SYSTEM,false,Fuel system",
        "TRANSMISSION,false,Transmission",
        "CLUTCH,false,Clutch",
        "OIL_LEAK,false,Oil leak",
        "EXHAUST,false,Exhaust",
        "DRIVETRAIN,false,Drivetrain",
        "REAR_WING,false,Rear wing",
        "FRONT_WING,false,Front wing",
        "WATER_PRESSURE,false,Water pressure",
        "SEAT,false,Seat",
        "BATTERY,false,Battery",
        "OUT_OF_FUEL,false,Out of fuel",
        "OVERHEATING,false,Overheating",
        "SPARK_PLUGS,false,Spark plugs",
        "THROTTLE,false,Throttle"
    )
    fun `race status maps to expected enum type`(expected: RaceStatus, isStatusFinished: Boolean, value: String) {
        assertEquals(expected, RaceStatus.from(value))
        assertEquals(isStatusFinished, expected.isStatusFinished())
    }

    @Test
    fun `race status maps mismatched capitalisation`() {
        assertEquals(TRANSMISSION, RaceStatus.from("TransMission"))
        assertEquals(TRANSMISSION, RaceStatus.from("TRANSMISSION"))
        assertEquals(TRANSMISSION, RaceStatus.from("transmission"))
    }

    @Test
    fun `race status maps unknown when not found`() {
        assertEquals(UNKNOWN, RaceStatus.from("Help"))
        assertEquals(UNKNOWN, RaceStatus.from("Yeeted"))
    }
}