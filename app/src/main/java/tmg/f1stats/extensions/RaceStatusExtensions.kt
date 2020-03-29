package tmg.f1stats.extensions

import tmg.f1stats.R
import tmg.f1stats.repo.enums.RaceStatus

val RaceStatus.iconRes: Int
    get() {
        when (this) {
            RaceStatus.FINISHED -> R.drawable.ic_status_finished
            RaceStatus.LAP_1 -> R.drawable.ic_status_lap_1
            RaceStatus.LAP_2 -> R.drawable.ic_status_lap_2
            RaceStatus.LAP_3 -> R.drawable.ic_status_lap_3
            RaceStatus.LAP_4 -> R.drawable.ic_status_lap_4
            RaceStatus.LAP_5 -> R.drawable.ic_status_lap_5
            RaceStatus.LAP_6 -> R.drawable.ic_status_lap_6
            RaceStatus.LAP_7 -> R.drawable.ic_status_lap_7
            RaceStatus.LAP_8 -> R.drawable.ic_status_lap_8
            RaceStatus.LAP_9 -> R.drawable.ic_status_lap_9
            RaceStatus.DAMAGE -> R.drawable.ic_status_damage
            RaceStatus.DRIVESHAFT -> R.drawable.ic_status_driveshaft
            RaceStatus.FUEL_PRESSURE -> R.drawable.ic_status_fuel_system
            RaceStatus.WHEEL -> R.drawable.ic_status_wheel
            RaceStatus.ENGINE -> R.drawable.ic_status_engine
            RaceStatus.SUSPENSION -> R.drawable.ic_status_suspension
            RaceStatus.RETIRED -> R.drawable.ic_status_retired
            RaceStatus.STEERING -> R.drawable.ic_status_steering
            RaceStatus.BRAKES -> R.drawable.ic_status_brakes
            RaceStatus.VIBRATIONS -> R.drawable.ic_status_vibrations
            RaceStatus.POWER_UNIT -> R.drawable.ic_status_power_unit
            RaceStatus.HYDRAULICS -> R.drawable.ic_status_hydraulic
            RaceStatus.WATER_LEAK -> R.drawable.ic_status_water_leak
            RaceStatus.MECHANICAL -> R.drawable.ic_status_mechanical
            RaceStatus.COLLISION -> R.drawable.ic_status_collision
            RaceStatus.POWER_LOSS -> R.drawable.ic_status_power_loss
            RaceStatus.WITHDREW -> R.drawable.ic_status_retired
            RaceStatus.ACCIDENT -> R.drawable.ic_status_accident
            RaceStatus.DISQUALIFIED -> R.drawable.ic_status_disqualified
            RaceStatus.OIL_PRESSURE -> R.drawable.ic_status_oil_pressure
            RaceStatus.ERS -> R.drawable.ic_status_electrical
            RaceStatus.PUNCTURE -> R.drawable.ic_status_puncture
            RaceStatus.ELECTRICAL -> R.drawable.ic_status_electrical
            RaceStatus.TURBO -> R.drawable.ic_status_turbo
            RaceStatus.SPUN_OFF -> R.drawable.ic_status_spun_off
            RaceStatus.FUEL_SYSTEM -> R.drawable.ic_status_fuel_system
            RaceStatus.GEARBOX -> R.drawable.ic_status_gearbox
            RaceStatus.TRANSMISSION -> R.drawable.ic_status_transmission
            RaceStatus.CLUTCH -> R.drawable.ic_status_clutch
            RaceStatus.OIL_LEAK -> R.drawable.ic_status_oil_leak
            RaceStatus.EXHAUST -> R.drawable.ic_status_exhaust
        }
        return RaceStatus.RETIRED.iconRes
    }