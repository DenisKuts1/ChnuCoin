package entities

import crypto.RSA
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.math.BigDecimal

object DAO {

    private val rsa by lazy { RSA() }

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
        val pair = rsa.generateKeys()
        val private = rsa.privateKeyToString(pair.private)
        val public = rsa.publicKeyToString(pair.public)
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

    fun getUser(login: String, password: String): User? {
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
}