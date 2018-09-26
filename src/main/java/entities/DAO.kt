package entities

import crypto.RSA
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.math.BigDecimal

object DAO {

    init {
        Database.connect("jdbc:mysql://localhost:3306/chnucoin?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "com.mysql.jdbc.Driver", "root", "")
        transaction {
            addLogger(StdOutSqlLogger)
            SchemaUtils.create(Users, Passwords, PrivateKeys, Transactions, EWallets, BlockChainTable)
        }
    }

    fun isUserExists(login: String): Boolean {
        var isEmpty = false
        transaction {
            isEmpty = !User.find { Users.login eq login }.empty()
        }
        return isEmpty
    }

    fun createUser(login: String, password: String) {
        val pair = RSA.generateKeys()
        val private = RSA.privateKeyToString(pair.private)
        val public = RSA.publicKeyToString(pair.public)
        transaction {
            val newUser = User.new {
                this.login = login
                this.isMiner = false
                this.publicKey = public
            }
            Password.new {
                this.user = newUser
                this.password = password
            }
            PrivateKey.new {
                this.user = newUser
                this.privateKey = private
            }
            EWallet.new {
                this.user = newUser
                this.amount = BigDecimal.TEN
            }
        }
    }

    fun getAmountOfCoins(user: User): BigDecimal{
        var amount = BigDecimal.ZERO
        transaction {
            amount = EWallet.find { EWallets.user eq user.id }.first().amount
        }
        return amount
    }

    fun confirmAndGetUser(login: String, password: String): User? {
        var user: User? = null
        var empty = false
        transaction {
            user = User.find { Users.login eq login }.firstOrNull()
            if (user != null) {
                empty = Password.find { (Passwords.user eq user!!.id) and (Passwords.password eq password) }.empty()
            }
        }
        return if (empty) {
            null
        } else {
            user
        }
    }

    fun getUser(login: String): User? {
        var user: User? = null
        transaction {
            user = User.find { Users.login eq login }.firstOrNull()
        }
        return user
    }

    fun getPrivateKey(user: User): String?{
        var privateKey: String? = null
        transaction {
            privateKey = PrivateKey.find { PrivateKeys.user eq user.id }.first().privateKey
        }
        return privateKey
    }

    fun getFirstUnconfirmedTransaction(): Transaction?{
        var result: Transaction? = null
        transaction {
            val transactions = Transaction.find {
                Transactions.isConfirmed eq false
            }
            if (!transactions.empty()){
                result = transactions.sortedBy { it.time }.first()
            }
        }
        return result
    }

    fun getLastBlockChain(): BlockChain?{
        var result: BlockChain? = null
        transaction {
            val blockChain = BlockChain.all()
            if (!blockChain.empty()){
                result = blockChain.sortedBy { it.time }.last()
            }
        }
        return result
    }
    fun getPreviousBlockChain(): BlockChain?{
        var result: BlockChain? = null
        transaction {
            val blockChain = BlockChain.all()
            if (blockChain.count() > 1){
                result = blockChain.sortedBy { it.time }.dropLast(1).last()
            }
        }
        return result
    }

    fun updateWallet(user: User, amount: BigDecimal){
        transaction {
            EWallet.find { EWallets.user eq user.id }.first().amount += amount
        }
    }

    fun confirmTransaction(transaction: Transaction){
        transaction {
            transaction.isConfirmed = true
        }
    }
}