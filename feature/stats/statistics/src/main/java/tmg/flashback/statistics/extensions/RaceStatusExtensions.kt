package tmg.flashback.statistics.extensions

import tmg.flashback.formula1.enums.RaceStatus
import tmg.flashback.formula1.enums.raceStatusUnknown
import tmg.flashback.statistics.R

val RaceStatus.iconRes: Int
    get() {
        return when (this) {
            "Finished" -> R.drawable.ic_status_finished
            "Retired" -> R.drawable.ic_status_retired
            "+1 Lap" -> R.drawable.ic_status_lap_1
            "+2 Laps" -> R.drawable.ic_status_lap_2
            "+3 Laps" -> R.drawable.ic_status_lap_3
            "+4 Laps" -> R.drawable.ic_status_lap_4
            "+5 Laps" -> R.drawable.ic_status_lap_5
            "+6 Laps" -> R.drawable.ic_status_lap_6
            "+7 Laps" -> R.drawable.ic_status_lap_7
            "+8 Laps" -> R.drawable.ic_status_lap_8
            "+9 Laps" -> R.drawable.ic_status_lap_9
            "Engine" -> R.drawable.ic_status_engine
            "Wheel nut" -> R.drawable.ic_status_wheel_nut
            "Tyre" -> R.drawable.ic_status_wheel
            "Wheel" -> R.drawable.ic_status_wheel
            "Damage" -> R.drawable.ic_status_damage
            "Steering" -> R.drawable.ic_status_steering
            "Brakes" -> R.drawable.ic_status_brakes
            "Vibrations" -> R.drawable.ic_status_vibrations
            "Suspension" -> R.drawable.ic_status_suspension
            "Power Unit" -> R.drawable.ic_status_power_unit
            "Hydraulics" -> R.drawable.ic_status_hydraulic
            "Water leak" -> R.drawable.ic_status_water_leak
            "Mechanical" -> R.drawable.ic_status_mechanical
            "Gearbox" -> R.drawable.ic_status_gearbox
            "Illness" -> R.drawable.ic_status_illness
            "Debris",
            "Collision",
            "Collision damage" -> R.drawable.ic_status_collision
            "Power loss" -> R.drawable.ic_status_power_loss
            "Withdrew" -> R.drawable.ic_status_retired
            "Accident" -> R.drawable.ic_status_accident
            "Oil pressure" -> R.drawable.ic_status_oil_pressure
            "Disqualified" -> R.drawable.ic_status_disqualified
            "Puncture" -> R.drawable.ic_status_puncture
            "ERS",
            "Electrical",
            "Electronics",
            "Electronic" -> R.drawable.ic_status_electrical
            "Driveshaft" -> R.drawable.ic_status_driveshaft
            "Fuel pressure" -> R.drawable.ic_status_fuel_system
            "Spun off" -> R.drawable.ic_status_spun_off
            "Turbo" -> R.drawable.ic_status_turbo
            "Fuel system" -> R.drawable.ic_status_fuel_system
            "Transmission" -> R.drawable.ic_status_transmission
            "Clutch" -> R.drawable.ic_status_clutch
            "Oil leak" -> R.drawable.ic_status_oil_leak
            "Exhaust" -> R.drawable.ic_status_exhaust
            "Drivetrain" -> R.drawable.ic_status_transmission
            "Rear wing" -> R.drawable.ic_status_rear_wing
            "Front wing" -> R.drawable.ic_status_front_wing
            "Water pressure" -> R.drawable.ic_status_water_pressure
            "Seat" -> R.drawable.ic_status_seat
            "Battery" -> R.drawable.ic_status_battery
            "Out of fuel" -> R.drawable.ic_status_fuel_system
            "Overheating" -> R.drawable.ic_status_fire
            "Spark plugs" -> R.drawable.ic_status_flash
            "Throttle" -> R.drawable.ic_status_speed
            raceStatusUnknown -> R.drawable.ic_status_unknown
            else -> R.drawable.ic_status_retired
        }
    }