package tmg.flashback.firebase.mappers

import androidx.core.graphics.toColorInt
import java.lang.NullPointerException
import tmg.crash_reporting.controllers.CrashController
import tmg.flashback.formula1.model.SearchCircuit
import tmg.flashback.formula1.model.SearchConstructor
import tmg.flashback.formula1.model.SearchDriver
import tmg.flashback.firebase.models.FSearchCircuitModel
import tmg.flashback.firebase.models.FSearchConstructorModel
import tmg.flashback.firebase.models.FSearchDriverModel
import tmg.utilities.utils.LocalDateUtils.Companion.isDateValid
import tmg.utilities.utils.LocalDateUtils.Companion.requireFromDate

class SearchMapper(
    private val crashController: CrashController,
    private val locationMapper: LocationMapper
) {

    fun mapSearchDriver(input: FSearchDriverModel, id: String): tmg.flashback.formula1.model.SearchDriver? {
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
        return tmg.flashback.formula1.model.SearchDriver(
            id = id,
            firstName = input.fname,
            lastName = input.sname,
            image = input.image,
            nationality = input.nat ?: "",
            nationalityISO = input.natISO,
            dateOfBirth = requireFromDate(input.dob),
            wikiUrl = input.wikiUrl
        )
    }

    fun mapSearchConstructor(input: FSearchConstructorModel, id: String): tmg.flashback.formula1.model.SearchConstructor? {
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
        return tmg.flashback.formula1.model.SearchConstructor(
            id = id,
            name = input.name,
            nationality = input.nat ?: "",
            nationalityISO = input.natISO,
            wikiUrl = input.wikiUrl,
            colour = input.color.toColorInt(),
        )
    }

    fun mapSearchCircuit(input: FSearchCircuitModel, id: String): tmg.flashback.formula1.model.SearchCircuit? {
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
        return tmg.flashback.formula1.model.SearchCircuit(
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