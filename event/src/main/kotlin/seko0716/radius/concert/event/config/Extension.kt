package seko0716.radius.concert.event.config

import org.springframework.data.geo.Point

fun <E> Collection<E>.addToCollection(dest: MutableCollection<E>): Collection<E> {
    return dest.let { it.addAll(this);it }
}

fun Point.isNan(): Boolean {
    return x.isNaN() || y.isNaN()
}

inline fun <T, R> T.attempt(block: (T) -> R, onError: (e: Exception) -> R): R {
    return try {
        block(this)
    } catch (e: Exception) {
        onError(e)
    }
}