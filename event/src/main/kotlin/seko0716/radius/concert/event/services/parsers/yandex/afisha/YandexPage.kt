package seko0716.radius.concert.event.services.parsers.yandex.afisha

import seko0716.radius.concert.event.domains.Event

internal data class YandexPage(
    val paging: Page = Page(),
    val valid: Boolean = true,
    val data: List<Event> = listOf()
) {
    data class Page(
        val limit: Int = 0,
        val offset: Int = 0,
        val total: Int = 0
    )

    companion object {
        @JvmField
        val INVALID_YANDEX_PAGE =
            YandexPage(valid = false)
    }
}