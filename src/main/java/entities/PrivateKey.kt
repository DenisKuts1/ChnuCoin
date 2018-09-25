package entities

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class PrivateKey(id: EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<PrivateKey>(PrivateKeys)
    var user by User referencedOn PrivateKeys.user
    var privateKey by PrivateKeys.privateKey
}