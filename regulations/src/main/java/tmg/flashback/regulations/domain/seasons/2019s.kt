package tmg.flashback.regulations.domain.seasons

import tmg.flashback.regulations.R
import tmg.flashback.regulations.domain.Item
import tmg.flashback.regulations.domain.Section
import tmg.flashback.regulations.domain.items.*
import tmg.flashback.regulations.domain.items.points_25_10
import tmg.flashback.regulations.domain.items.points_3_3
import tmg.flashback.regulations.domain.items.points_header
import tmg.flashback.regulations.domain.items.statFastestLapPoint
import tmg.flashback.regulations.domain.items.statMandatoryPitStop
import tmg.flashback.regulations.domain.items.statMaxFuel
import tmg.flashback.regulations.domain.items.statMaxRaceDistance
import tmg.flashback.regulations.domain.items.statSessionLengthQ1
import tmg.flashback.regulations.domain.items.statSessionLengthQ2
import tmg.flashback.regulations.domain.items.statSessionLengthQ3

internal val season2019 = listOf(
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

internal val season2018 = listOf(
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
            add(statFastestLapPoint(0))
            addAll(points_header)
            addAll(points_25_10)
        }
    )
)

internal val season2017 = listOf(
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
            add(statFastestLapPoint(0))
            addAll(points_header)
            addAll(points_25_10)
        }
    )
)

internal val season2016 = listOf(
    Section(
        label = R.string.format_qualifying,
        items = mutableListOf<Item>().apply {
            add(Item.Header(R.string.format_qualifying_elimination))
            add(Item.Text(R.string.format_qualifying_elimination_summary))
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
            add(statFastestLapPoint(0))
            addAll(points_header)
            addAll(points_25_10)
        }
    )
)