package seko0716.radius.concert.event.config

fun <E> Collection<E>.addToCollection(dest: MutableCollection<E>): Collection<E> {
    return dest.let { it.addAll(this);it }
}