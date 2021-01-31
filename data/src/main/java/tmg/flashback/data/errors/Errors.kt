package tmg.flashback.data.errors

class PermissionError(location: String): Error("Permission to $location is invalid or denied")

class DoesntExistError(record: String): Error("Record $record doesn't exist")

class RecordAlreadyExistsError(record: String): Error("Record $record already exists")

class UnauthenticatedError(resource: String): Error("Access to resource $resource is denied")