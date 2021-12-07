import kotlin.math.abs

fun main() {
    fun part1(positions: List<Int>): Int =
        positions.sumOf { abs(it - positions[positions.size / 2]) }

    fun part2(positions: List<Int>): Int =
        (positions.first()..positions.last()).minOf { posA ->
            positions.sumOf { posB ->
                val dist = abs(posB - posA)
                dist * (dist + 1) / 2
            }
        }

    // test if implementation meets criteria from the description, like:
    val testInput = intsSplitBy(readInput("Day07_test")[0], ",").sorted()
    check(part1(testInput) == 37)
    check(part2(testInput) == 168)

    val input = intsSplitBy(readInput("Day07")[0], ",").sorted()
    println(part1(input))
    println(part2(input))
}
