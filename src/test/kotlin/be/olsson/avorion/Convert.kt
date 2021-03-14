package be.olsson.avorion

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.jsoup.Jsoup
import java.io.InputStreamReader
import java.math.BigDecimal

fun main() {
    val doc = Main::class.java.getResourceAsStream("/database.html").use {
        requireNotNull(it)
        Jsoup.parse(InputStreamReader(it).readText())
    }

    val rows = doc.select("tr")
        .asSequence()

    val database = rows.asSequence()
        .drop(1)
        .map {
            val columns = it.select("td")
            Goods(
                name = columns[0].text(),
                volume = BigDecimal(columns[1].text()),
                avgPrice = columns[2].text().toInt(),
                soldBy = columns[3].select("a").map { a -> a.text() },
                boughtBy = columns[4].select("a").map { a -> a.text() },
                illegal = columns[5].text() == "yes",
                dangerours = columns[6].text() == "yes"
            )
        }
        .toList()
    println(
        YAMLMapper()
            .registerKotlinModule()
            .writeValueAsString(database)
    )
}
