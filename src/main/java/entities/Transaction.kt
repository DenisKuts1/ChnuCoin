package entities

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class Transaction(id: EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<Transaction>(Transactions)
    var time by Transactions.time
    var sender by User referencedOn Transactions.sender
    var receiver by User referencedOn Transactions.receiver
    var amount by Transactions.amount
    var hash by Transactions.hash
    var signature by Transactions.signature
    var isConfirmed by Transactions.isConfirmed
}