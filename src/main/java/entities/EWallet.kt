package entities

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class EWallet(id: EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<EWallet>(EWallets)
    var user by User referencedOn EWallets.user
    var amount by EWallets.amount
}