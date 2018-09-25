package entities

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class Password(id: EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<Password>(Passwords)

    var user by User referencedOn Passwords.user
    var password by Passwords.password


}