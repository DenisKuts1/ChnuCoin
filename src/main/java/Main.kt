import entities.*
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SchemaUtils.create

/*fun main(args: Array<String>) {
    /*Database.connect("jdbc:mysql:mem:test", driver = "org.mysql.Driver")
    transaction {
        addLogger(StdOutSqlLogger)
        create(Users, Passwords, PrivateKeys, EWallets, Transactions, BlockChainTable)
    }*/
}*/

class Main: Application(){
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(Main::class.java)
        }
    }

    override fun start(primaryStage: Stage?) {
        primaryStage?.scene = Scene(FXMLLoader().load(javaClass.getResourceAsStream("authentication.fxml")))
        primaryStage?.show()
    }
}