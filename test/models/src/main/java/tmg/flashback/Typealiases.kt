package tmg.flashback

//region Room

typealias RoomConstructor = tmg.flashback.domain.persistence.models.constructors.Constructor

typealias RoomConstructorHistory = tmg.flashback.domain.persistence.models.constructors.ConstructorHistory

typealias RoomDriver = tmg.flashback.domain.persistence.models.drivers.Driver

typealias RoomDriverHistory = tmg.flashback.domain.persistence.models.drivers.DriverHistory

typealias RoomRace = tmg.flashback.domain.persistence.models.race.Race

typealias RoomRaceInfo = tmg.flashback.domain.persistence.models.race.RaceInfo

typealias RoomRaceInfoWithCircuit = tmg.flashback.domain.persistence.models.race.RaceInfoWithCircuit

typealias RoomSchedule = tmg.flashback.domain.persistence.models.overview.Schedule

//endregion

//region Network

typealias NetworkCircuit = tmg.flashback.flashbackapi.api.models.circuits.Circuit

typealias NetworkCircuitHistory = tmg.flashback.flashbackapi.api.models.circuits.CircuitHistory

typealias NetworkCircuitResult = tmg.flashback.flashbackapi.api.models.circuits.CircuitResult

typealias NetworkConstructor = tmg.flashback.flashbackapi.api.models.constructors.Constructor

typealias NetworkDriver = tmg.flashback.flashbackapi.api.models.drivers.Driver

typealias NetworkRaceData = tmg.flashback.flashbackapi.api.models.races.RaceData

typealias NetworkRaceResult = tmg.flashback.flashbackapi.api.models.races.RaceResult

typealias NetworkQualifyingResult = tmg.flashback.flashbackapi.api.models.races.QualifyingResult

typealias NetworkSprintResult = tmg.flashback.flashbackapi.api.models.races.SprintRaceResult

typealias NetworkSchedule = tmg.flashback.flashbackapi.api.models.overview.Schedule

//endregion