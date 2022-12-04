fun main() {
    val parsedData = readInput("Day04").asSequence()
        .map { it.split(",") }
        .map { it[0] to it[1] }
        .map { it.first.split("-") to it.second.split("-") }
        .map { it.first[0].toInt()..it.first[1].toInt() to it.second[0].toInt()..it.second[1].toInt() }
    parsedData
        .count { it.first in it.second || it.second in it.first }
        .also { printP1(it) }
    parsedData
        .count { it.first overlaps it.second || it.second overlaps it.first }
        .also { printP2(it) }
}

operator fun ClosedRange<Int>.contains(other: ClosedRange<Int>): Boolean =
    other.start in this && other.endInclusive in this

infix fun ClosedRange<Int>.overlaps(other: ClosedRange<Int>): Boolean =
    other.start in this || other.endInclusive in this