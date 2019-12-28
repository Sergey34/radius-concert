package seko0716.radius.concert.admin.services.parsers.kassir

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

data class KassirPage(
    val cnt: Int = 0,
    val next: Int = 0,
    val html: String = "",
    val valid: Boolean = true,
    val doc: Document = Jsoup.parse(html)
) {
    companion object {
        @JvmField
        val INVALID_KASSIR_PAGE =
            KassirPage(valid = false)
    }
}