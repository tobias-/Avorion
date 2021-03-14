package be.olsson.avorion

import javafx.application.Application
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.transformation.FilteredList
import javafx.collections.transformation.SortedList
import javafx.geometry.Insets
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.TextField
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.text.Font
import javafx.stage.Stage
import java.util.function.Predicate

class Main : Application() {
    private val table = TableView<Tradelane>()
    private val fromStation = TextField()
    private val goods = TextField()
    private val toStation = TextField()

    private val data = FilteredList(TradelaneDatabase)

    override fun start(primaryStage: Stage) {
        primaryStage.withStage()
    }

    private fun updateFilter() {
        data.predicate = TradelaneFilter(
            goods = goods.text,
            fromStation = fromStation.text,
            toStation = toStation.text
        )
    }

    private fun setupTable() {
        table.setMaxSize(1200.0, 800.0)
        table.setMinSize(1200.0, 800.0)

        val fromStationType = TableColumn<Tradelane, String>("From station").apply {
            cellValueFactory { it.soldBy }
        }
        val goodsName = TableColumn<Tradelane, String>("Goods").apply {
            cellValueFactory { it.name }
        }
        val toStationType = TableColumn<Tradelane, String>("To Station").apply {
            cellValueFactory { it.boughtBy }
        }
        val avgPrice = TableColumn<Tradelane, Number>("Avg. price").apply {
            setCellValueFactory { SimpleIntegerProperty(it.value.avgPrice) }
        }
        val sortedList = SortedList(data)
        table.items = sortedList
        sortedList.comparatorProperty().bind(table.comparatorProperty())
        table.columns.addAll(fromStationType, goodsName, toStationType, avgPrice)

        fromStation.setOnKeyTyped { updateFilter() }
        toStation.setOnKeyTyped { updateFilter() }
        goods.setOnKeyTyped { updateFilter() }
    }

    private fun Stage.withStage() {
        val scene = Scene(Group(), 1200.0, 900.0)
        title = "Avorion goods"

        val label = Label("Avorion")
        label.font = Font("Arial", 20.0)

        setupTable()

        with(fromStation) {
            promptText = "From station"
        }
        with(goods) {
            promptText = "Goods"
        }
        with(toStation) {
            promptText = "To station"
        }
        val inputs = HBox(fromStation, goods, toStation)

        val vbox = VBox(label, table, inputs)
        vbox.spacing = 5.0
        vbox.padding = Insets(10.0, 0.0, 0.0, 10.0)

        (scene.root as Group).children.addAll(vbox)
        fromStation.requestFocus()
        isAlwaysOnTop = true
        setScene(scene)
        show()
    }

    fun foo() {
        launch()
    }
}

fun main() {
    Main().foo()
}

fun <S> TableColumn<S, String>.cellValueFactory(block: (S) -> String) {
    setCellValueFactory { SimpleStringProperty(block(it.value)) }
}

data class TradelaneFilter(
    val goods: String,
    val fromStation: String,
    val toStation: String
) : Predicate<Tradelane> {
    override fun test(tradelane: Tradelane): Boolean =
        (goods.isBlank() || tradelane.name.contains(goods, true)) &&
            (fromStation.isBlank() || tradelane.soldBy.contains(fromStation, true)) &&
            (toStation.isBlank() || tradelane.boughtBy.contains(toStation, true))
}
