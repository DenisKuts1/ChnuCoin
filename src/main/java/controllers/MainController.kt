package controllers

import crypto.RSA
import entities.*
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.Alert
import javafx.scene.control.TextField

import javafx.scene.text.Text
import org.apache.commons.codec.digest.DigestUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormatter
import java.lang.Error
import java.math.BigDecimal

class MainController {

    @FXML
    lateinit var login: Text

    @FXML
    lateinit var amount: Text

    @FXML
    lateinit var transactionAmount: TextField

    @FXML
    lateinit var loginWalletField: TextField

    @FXML
    lateinit var wallet: Text

    private lateinit var user: User

    fun setUser(user: User) {
        this.user = user
        login.text = user.login
        amount.text = DAO.getAmountOfCoins(user).toPlainString()
    }

    @FXML
    fun exit(event: ActionEvent) {

    }

    @FXML
    fun quit(event: ActionEvent) {

    }

    @FXML
    fun send(event: ActionEvent) {
        val amount = BigDecimal(transactionAmount.text)
        val receiverLogin = loginWalletField.text
        if (amount > BigDecimal(this.amount.text)) {
            Alert(Alert.AlertType.ERROR, "Not enough money to make transaction").show()
            return
        }
        val receiver = DAO.getUser(receiverLogin)
        if (receiver == null) {
            Alert(Alert.AlertType.ERROR, "There are no users with this login").show()
            return
        }

        val time = DateTime.now()
        val transactionText = time.toString() + user.login + receiver.login + amount.toString()
        val transactionHash = DigestUtils.sha256Hex(transactionText)
        val transactionSignature = RSA.encrypt(DAO.getPrivateKey(user)!!, transactionHash)

        transaction {
            Transaction.new {
                this.time = time
                this.sender = user
                this.receiver = receiver
                this.amount = amount
                this.isConfirmed = false
                this.hash = transactionHash
                this.signature = transactionSignature
            }
        }
        Alert(Alert.AlertType.CONFIRMATION, "Transaction is waiting for conformation").show()

    }

    @FXML
    fun initialize() {

    }

    @FXML
    fun getPublicKey(event: ActionEvent) {
        Alert(Alert.AlertType.INFORMATION, user.publicKey).show()
    }

    @FXML
    fun mine(event: ActionEvent) {
        val unconfirmedTransaction = DAO.getFirstUnconfirmedTransaction()
        if (unconfirmedTransaction == null) {
            Alert(Alert.AlertType.ERROR, "There are no unconfirmed transactions at that time")
            return
        }
        val transactionHash = unconfirmedTransaction.hash
        val lastBlockChain = DAO.getLastBlockChain()
        val blockChainHash = if (lastBlockChain == null) {
            "0"
        } else {
            lastBlockChain.hash
        }
        var nonce = 0
        do {
            val hash = getBlockChainHash(transactionHash, blockChainHash, nonce)
            println("$nonce: $hash")
            if(hash.startsWith("0000")){
                transaction {
                    BlockChain.new {
                        this.miner = user
                        this.hash = hash
                        this.nonce = nonce
                        this.time = DateTime.now()
                        this.signature = RSA.encrypt(DAO.getPrivateKey(user)!!, hash)
                    }
                    unconfirmedTransaction.run {
                        DAO.updateWallet(sender, -amount)
                        DAO.updateWallet(receiver, amount)
                        DAO.confirmTransaction(this)
                    }
                    val previousBlockChain = DAO.getPreviousBlockChain()
                    if(previousBlockChain != null){
                        DAO.updateWallet(previousBlockChain.miner, BigDecimal("0.01"))
                    }
                }
                Alert(Alert.AlertType.ERROR, "Transaction confirmed").show()
                return
            } else {
                nonce++
            }
        } while (true)


    }

    fun getBlockChainHash(transactionHash: String, blockChainHash: String, nonce: Int): String {
        return DigestUtils.sha256Hex(transactionHash + blockChainHash + nonce)
    }
}