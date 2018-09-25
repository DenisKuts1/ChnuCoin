package controllers

import entities.DAO
import entities.User
import entities.Users
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.control.PasswordField
import javafx.scene.control.TextField
import javafx.stage.Stage
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.selectAll
import java.util.ResourceBundle

class Authentication {

    @FXML
    lateinit var login: TextField

    @FXML
    lateinit var password: PasswordField



    @FXML
    fun signIn(event: ActionEvent) {
        val login = login.text
        val password = password.text
        val user = DAO.getUser(login, password)
        if(user == null){
            Alert(Alert.AlertType.ERROR, "Wrong login/password, try again").show()
        } else {
            val loader = FXMLLoader(Authentication::class.java.getResource("../main.fxml"))
            val parent = loader.load<Parent>()
            val stage = Stage()
            stage.scene = Scene(parent)
            stage.show()
            loader.getController<MainController>().setUser(user)
            close()
        }
    }

    @FXML
    fun singUp(event: ActionEvent) {
        val login = login.text
        val password = password.text
        if(!DAO.isUserExists(login)){
            DAO.createUser(login, password)
        } else {
            Alert(Alert.AlertType.ERROR, "That login already exists").show()
        }
        empty()
    }

    private fun empty(){
        login.text = ""
        password.text = ""
    }

    private fun close(){
        (login.scene.window as Stage).close()
    }

    @FXML
    fun initialize() {


    }
}