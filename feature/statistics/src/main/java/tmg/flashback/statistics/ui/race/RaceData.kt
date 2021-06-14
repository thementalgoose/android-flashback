package tmg.flashback.statistics.ui.race

import android.os.Parcel
import android.os.Parcelable
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

private const val dateFormat: String = "yyyy/MM/dd"

data class RaceData(
    val season: Int,
    val round: Int,
    val circuitId: String,
    val defaultToRace: Boolean = true,
    val country: String,
    val raceName: String,
    val trackName: String,
    val countryISO: String,
    val date: LocalDate? = null
): Parcelable {


    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readInt() == 1,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString().let {
            if (it == null) return@let null
            return@let LocalDate.parse(it, DateTimeFormatter.ofPattern(dateFormat))
        }
    )

    override fun describeContents(): Int {
        return season +
                round +
                circuitId.hashCode() +
                defaultToRace.hashCode() +
                country.hashCode() +
                raceName.hashCode() +
                trackName.hashCode() +
                countryISO.hashCode() +
                date.hashCode()
    }

    override fun writeToParcel(p0: Parcel?, p1: Int) {
        p0?.writeInt(season)
        p0?.writeInt(round)
        p0?.writeString(circuitId)
        p0?.writeByte(if (defaultToRace) 1 else 0)
        p0?.writeString(country)
        p0?.writeString(raceName)
        p0?.writeString(trackName)
        p0?.writeString(countryISO)
        p0?.writeString(date?.format(DateTimeFormatter.ofPattern(dateFormat)))
    }

    companion object CREATOR : Parcelable.Creator<RaceData> {
        override fun createFromParcel(parcel: Parcel): RaceData {
            return RaceData(parcel)
        }

        override fun newArray(size: Int): Array<RaceData?> {
            return arrayOfNulls(size)
        }
    }

}