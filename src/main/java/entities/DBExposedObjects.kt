package entities

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Table

object Users : IntIdTable() {
    val publicKey = text("public_key")
    val login = text("login")
    val isMiner = bool("is_miner")
}

object Passwords : IntIdTable() {
    val user = reference("user", Users)
    val password = text("password")
}

object PrivateKeys : IntIdTable() {
    val user = reference("user", Users)
    val privateKey = text("private_key")
}

object EWallets : IntIdTable() {
    val user = reference("user", Users)
    val amount = decimal("amount", 10, 4)
}

object Transactions : IntIdTable() {
    val time = datetime("time")
    val sender = reference("sender", Users)
    val receiver = reference("receiver", Users)
    val amount = decimal("amount", 10, 4)
    val hash = text("hash")
    val signature = text("signature")
    val isConfirmed = bool("is_confirmed")
}

object BlockChainTable : IntIdTable() {
    val time = datetime("time")
    val miner = reference("miner", Users)
    val hash = text("hash")
    val nonce = integer("nonce")
    val signature = text("signature")
}