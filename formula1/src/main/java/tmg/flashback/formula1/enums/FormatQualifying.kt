package tmg.flashback.formula1.enums

enum class FormatQualifying(
    val key: String
) {
    TWO_SESSIONS("two_sessions"),
    ONE_HOUR("one_hour"),
    ONE_LAP("one_lap"),
    ONE_LAP_SATURDAY("one_lap_saturday"),
    AGGREGATE("aggregate"),
    KNOCKOUT_RACE_FUEL("knockout_race_fuel"),
    KNOCKOUT_TYRE_RULE("knockout_tyre_rule"),
    KNOCKOUT("knockout"),
    KNOCKOUT_ELIMINATION("knockout_elimination"),
    KNOCKOUT_SPRINT("knockout_sprint"),
}