package tmg.flashback.regulations.domain.seasons

import tmg.flashback.regulations.R
import tmg.flashback.regulations.domain.Item
import tmg.flashback.regulations.domain.Section
import tmg.flashback.regulations.domain.items.*
import tmg.flashback.regulations.domain.items.points_25_10
import tmg.flashback.regulations.domain.items.points_8_8
import tmg.flashback.regulations.domain.items.statFastestLapPoint
import tmg.flashback.regulations.domain.items.statMandatoryPitStop
import tmg.flashback.regulations.domain.items.statMaxFuel
import tmg.flashback.regulations.domain.items.statMaxRaceDistance
import tmg.flashback.regulations.domain.items.statQualifyingSessionCount

internal val season2022 = listOf(
    Section(
        label = R.string.format_qualifying,
        items = mutableListOf<Item>().apply {
            add(Item.Header(R.string.format_qualifying_knockout))
            add(statSessionLengthQ1(18))
            add(statSessionLengthQ2(15))
            add(statSessionLengthQ3(12))
            add(Item.Text(R.string.format_qualifying_knockout_summary))
            add(Item.QualifyingKnockout)
        }
    ),
    Section(
        label = R.string.format_sprint,
        items = mutableListOf<Item>().apply {
            add(statMaxRaceDistance(100))
            add(statFastestLapPoint(false))
            add(Item.Text(R.string.format_sprint_summary))
            add(Item.Text(R.string.format_sprint_summary2))
            add(Item.Text(R.string.format_sprint_summary3))
            add(Item.SubHeader(R.string.format_sprint_points))
            add(Item.Text(R.string.format_sprint_points_summary))
            addAll(points_8_8)
        }
    ),
    Section(
        label = R.string.format_race,
        items = mutableListOf<Item>().apply {
            add(Item.Text(R.string.format_race_summary))
            add(statMaxFuel(110))
            add(statMaxRaceDistance(305))
            add(statMandatoryPitStop(true))
            add(statFastestLapPoint(1))
            addAll(points_header)
            addAll(points_25_10)
        }
    )
)

internal val season2021 = listOf(
    Section(
        label = R.string.format_qualifying,
        items = mutableListOf<Item>().apply {
            add(Item.Header(R.string.format_qualifying_knockout))
            add(statSessionLengthQ1(18))
            add(statSessionLengthQ2(15))
            add(statSessionLengthQ3(12))
            add(Item.Text(R.string.format_qualifying_knockout_summary))
            add(Item.Text(R.string.format_qualifying_knockout_q2))
            add(Item.QualifyingKnockout)
        }
    ),
    Section(
        label = R.string.format_sprint_qualifying,
        items = mutableListOf<Item>().apply {
            add(statMaxRaceDistance(100))
            add(statFastestLapPoint(false))
            add(Item.Text(R.string.format_sprint_qualifying_summary))
            add(Item.Text(R.string.format_sprint_qualifying_summary2))
            add(Item.Text(R.string.format_sprint_qualifying_summary3))
            add(Item.SubHeader(R.string.format_sprint_qualifying_points))
            add(Item.Text(R.string.format_sprint_qualifying_points_summary))
            addAll(points_3_3)
        }
    ),
    Section(
        label = R.string.format_race,
        items = mutableListOf<Item>().apply {
            add(statMaxFuel(110))
            add(statMaxRaceDistance(305))
            add(statMandatoryPitStop(true))
            add(statFastestLapPoint(1))
            addAll(points_header)
            addAll(points_25_10)
        }
    )
)


internal val season2020 = listOf(
    Section(
        label = R.string.format_qualifying,
        items = mutableListOf<Item>().apply {
            add(Item.Header(R.string.format_qualifying_knockout))
            add(statSessionLengthQ1(18))
            add(statSessionLengthQ2(15))
            add(statSessionLengthQ3(12))
            add(Item.Text(R.string.format_qualifying_knockout_summary))
            add(Item.Text(R.string.format_qualifying_knockout_q2))
            add(Item.QualifyingKnockout)
        }
    ),
    Section(
        label = R.string.format_race,
        items = mutableListOf<Item>().apply {
            add(statMaxFuel(110))
            add(statMaxRaceDistance(305))
            add(statMandatoryPitStop(true))
            add(statFastestLapPoint(1))
            addAll(points_header)
            addAll(points_25_10)
        }
    )
)
