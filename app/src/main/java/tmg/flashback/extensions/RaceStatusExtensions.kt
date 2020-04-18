package tmg.flashback.extensions

import androidx.annotation.DrawableRes
import tmg.flashback.R
import tmg.flashback.repo.enums.RaceStatus

val RaceStatus.iconRes: Int
    get() {
        return when (this) {
            "Finished" -> R.drawable.ic_status_finished
            "+1 Lap" -> R.drawable.ic_status_lap_1
            "+2 Laps" -> R.drawable.ic_status_lap_2
            "+3 Laps" -> R.drawable.ic_status_lap_3
            "+4 Laps" -> R.drawable.ic_status_lap_4
            "+5 Laps" -> R.drawable.ic_status_lap_5
            "+6 Laps" -> R.drawable.ic_status_lap_6
            "+7 Laps" -> R.drawable.ic_status_lap_7
            "+8 Laps" -> R.drawable.ic_status_lap_8
            "+9 Laps" -> R.drawable.ic_status_lap_9
            "Engine" -> R.drawable.ic_status_damage
            "Wheel nut" -> R.drawable.ic_status_driveshaft
            "Tyre" -> R.drawable.ic_status_fuel_system
            "Wheel" -> R.drawable.ic_status_wheel
            "Damage" -> R.drawable.ic_status_engine
            "Steering" -> R.drawable.ic_status_suspension
            "Brakes" -> R.drawable.ic_status_retired
            "Vibrations" -> R.drawable.ic_status_steering
            "Suspension" -> R.drawable.ic_status_brakes
            "Power Unit" -> R.drawable.ic_status_vibrations
            "Hydraulics" -> R.drawable.ic_status_power_unit
            "Water leak" -> R.drawable.ic_status_hydraulic
            "Mechanical" -> R.drawable.ic_status_water_leak
            "Gearbox" -> R.drawable.ic_status_mechanical
            "Collision" -> R.drawable.ic_status_collision
            "Collision damage" -> R.drawable.ic_status_power_loss
            "Retired" -> R.drawable.ic_status_retired
            "Power loss" -> R.drawable.ic_status_accident
            "Withdrew" -> R.drawable.ic_status_disqualified
            "Accident" -> R.drawable.ic_status_oil_pressure
            "ERS" -> R.drawable.ic_status_electrical
            "Oil pressure" -> R.drawable.ic_status_puncture
            "Disqualified" -> R.drawable.ic_status_electrical
            "Puncture" -> R.drawable.ic_status_turbo
            "Electrical" -> R.drawable.ic_status_spun_off
            "Driveshaft" -> R.drawable.ic_status_fuel_system
            "Fuel pressure" -> R.drawable.ic_status_gearbox
            "Spun off" -> R.drawable.ic_status_transmission
            "Turbo" -> R.drawable.ic_status_clutch
            "Fuel system" -> R.drawable.ic_status_oil_leak
            "Transmission" -> R.drawable.ic_status_exhaust
            "Clutch" -> R.drawable.ic_status_transmission
            "Oil leak" -> R.drawable.ic_status_rear_wing
            "Exhaust" -> R.drawable.ic_status_water
            "Drivetrain" -> R.drawable.ic_status_seat
            "Rear wing" -> R.drawable.ic_status_battery
            "Front wing" -> R.drawable.ic_status_front_wing

            "Water pressure" -> R.drawable.ic_status_retired
            "Seat" -> R.drawable.ic_status_retired
            "Battery" -> R.drawable.ic_status_retired
            "Out of fuel" -> R.drawable.ic_status_retired
            "Overheating" -> R.drawable.ic_status_retired
            "Spark plugs" -> R.drawable.ic_status_retired
            "Throttle" -> R.drawable.ic_status_retired

            else -> R.drawable.ic_status_retired
        }
    }