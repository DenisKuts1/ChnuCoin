package controllers

import entities.DAO
import entities.User
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.TextField

import javafx.scene.text.Text




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
    lateinit var publicKey: Text

    @FXML
    lateinit var wallet: Text

    private lateinit var user: User

    fun setUser(user: User){
        this.user = user
        login.text = user.login
        publicKey.text = user.publicKey
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

    }

    @FXML
    fun initialize() {

    }
}