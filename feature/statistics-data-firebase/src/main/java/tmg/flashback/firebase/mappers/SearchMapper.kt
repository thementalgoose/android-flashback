package tmg.flashback.firebase.mappers

import androidx.core.graphics.toColorInt
import java.lang.NullPointerException
import tmg.crash_reporting.controllers.CrashController
import tmg.flashback.data.models.stats.Location
import tmg.flashback.data.models.stats.SearchCircuit
import tmg.flashback.data.models.stats.SearchConstructor
import tmg.flashback.data.models.stats.SearchDriver
import tmg.flashback.firebase.base.ConverterUtils.fromDateRequired
import tmg.flashback.firebase.base.ConverterUtils.isDateValid
import tmg.flashback.firebase.models.FCircuitLocation
import tmg.flashback.firebase.models.FSearchCircuitModel
import tmg.flashback.firebase.models.FSearchConstructorModel
import tmg.flashback.firebase.models.FSearchDriverModel

class SearchMapper(
    private val crashController: CrashController,
    private val locationMapper: LocationMapper
) {

    fun mapSearchDriver(input: FSearchDriverModel, id: String): SearchDriver? {
        if (input.fname == null) {
            crashController.logException(NullPointerException("SearchMapper.mapSearchDriver Driver id $id has a null first name"))
            return null
        }
        if (input.sname == null) {
            crashController.logException(NullPointerException("SearchMapper.mapSearchDriver Driver id $id has a null last name"))
            return null
        }
        if (input.natISO == null) {
            crashController.logException(NullPointerException("SearchMapper.mapSearchDriver Driver id $id has a null nationality ISO"))
            return null
        }
        if (input.dob == null || !isDateValid(input.dob)) {
            crashController.logException(NullPointerException("SearchMapper.mapSearchDriver Driver id $id has a null dob or invalid dob ${input.dob}"))
            return null
        }
        return SearchDriver(
            id = id,
            firstName = input.fname,
            lastName = input.sname,
            image = input.image,
            nationality = input.nat ?: "",
            nationalityISO = input.natISO,
            dateOfBirth = fromDateRequired(input.dob),
            wikiUrl = input.wikiUrl
        )
    }

    fun mapSearchConstructor(input: FSearchConstructorModel, id: String): SearchConstructor? {
        if (input.name == null) {
            crashController.logException(NullPointerException("SearchMapper.mapSearchConstructor Constructor id $id has a null name"))
            return null
        }
        if (input.natISO == null) {
            crashController.logException(NullPointerException("SearchMapper.mapSearchConstructor Constructor id $id has a null nationality iso"))
            return null
        }
        if (input.color == null) {
            crashController.logException(NullPointerException("SearchMapper.mapSearchConstructor Constructor id $id has a null colour"))
            return null
        }
        return SearchConstructor(
            id = id,
            name = input.name,
            nationality = input.nat ?: "",
            nationalityISO = input.natISO,
            wikiUrl = input.wikiUrl,
            colour = input.color.toColorInt(),
        )
    }

    fun mapSearchCircuit(input: FSearchCircuitModel, id: String): SearchCircuit? {
        if (input.loc == null) {
            crashController.logException(NullPointerException("SearchMapper.mapSearchCircuit Circuit id $id has a null location"))
            return null
        }
        if (input.country == null) {
            crashController.logException(NullPointerException("SearchMapper.mapSearchCircuit Circuit id $id has a null country"))
            return null
        }
        if (input.countryISO == null) {
            crashController.logException(NullPointerException("SearchMapper.mapSearchCircuit Circuit id $id has a null country iso"))
            return null
        }
        if (input.name == null) {
            crashController.logException(NullPointerException("SearchMapper.mapSearchCircuit Circuit id $id has a null name"))
            return null
        }
        return SearchCircuit(
            id = id,
            country = input.country,
            countryISO = input.countryISO,
            location = locationMapper.mapCircuitLocation(input.location),
            locationName = input.loc,
            name = input.name,
            wikiUrl = input.wikiUrl,
        )
    }
}