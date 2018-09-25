package entities

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class BlockChain(id: EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<BlockChain>(BlockChainTable)
    var time by BlockChainTable.time
    var miner by User referencedOn BlockChainTable.miner
    var hash by BlockChainTable.hash
    var nonce by BlockChainTable.nonce
    var signature by BlockChainTable.signature
}