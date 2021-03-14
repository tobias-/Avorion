package be.olsson.avorion

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.math.BigDecimal

object Database {
    val rows: List<Goods> =
        Database::class.java.getResourceAsStream("/database.yaml")
            .use {
                requireNotNull(it) { "Database /database.yaml was not found" }
                YAMLMapper().registerKotlinModule().readValue(it)
            }

    val stationTypes = rows
        .asSequence()
        .flatMap {
            it.boughtBy.asSequence() + it.soldBy.asSequence()
        }
        .toSet()

    val goodsTypes = rows
        .map { it.name }
        .toSet()
}

object GoodsDatabase : ImmutableObservableList<Goods>(Database.rows)

object TradelaneDatabase : ImmutableObservableList<Tradelane>(
    sequence {
        for (row in Database.rows) {
            for (boughtBy in row.boughtBy) {
                for (soldBy in row.soldBy) {
                    yield(
                        Tradelane(
                            name = row.name,
                            volume = row.volume,
                            avgPrice = row.avgPrice,
                            soldBy = soldBy,
                            boughtBy = boughtBy,
                            illegal = row.illegal,
                            dangerours = row.dangerours
                        )
                    )
                }
            }
        }
    }.toList()
)

data class Goods(
    val name: String,
    val volume: BigDecimal,
    val avgPrice: Int,
    val soldBy: List<String>,
    val boughtBy: List<String>,
    val illegal: Boolean,
    val dangerours: Boolean
)

data class Tradelane(
    val name: String,
    val volume: BigDecimal,
    val avgPrice: Int,
    val soldBy: String,
    val boughtBy: String,
    val illegal: Boolean,
    val dangerours: Boolean
)
