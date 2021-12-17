import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.text.Regex

fun main() {
    fun xDisplacement(dx: Int): Int = (dx * (dx + 1)) / 2

    fun fallsWithin(dx0: Int, dy0: Int, b1: Pair<Int, Int>, b2: Pair<Int, Int>): Boolean {
        val (x1, y1) = b1
        val (x2, y2) = b2
        var x = 0; var dx = dx0
        var y = 0; var dy = dy0
        var maxY = Int.MIN_VALUE
        for (i in 0..500) {
            x += dx; y += dy
            maxY = max(y, maxY)
            if (x in x1..x2 && y in y1..y2) return true
            dx = max(0, dx - 1); dy -= 1
        }
        return false
    }

    fun part1(input: List<String>): Int {
        val y1 = Regex("-?[0-9]+").findAll(input[0]).toList()[2].value.toInt()
        return (abs(y1) * (abs(y1) - 1)) / 2
    }

    fun part2(input: List<String>): Int {
        val (x1, x2, y1, y2) = Regex("-?[0-9]+").findAll(input[0]).map { it.value.toInt() }.toList()
        val boxUL = (x1 to y1)
        val boxDR = (x2 to y2)
        var probes = 0
        for (dx in 0..500) {
            for (dy in -500..500) {
                if (fallsWithin(dx, dy, boxUL, boxDR))
                    probes += 1
            }
        }
        return probes
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day17_test")
    check(part1(testInput) == 45)
    check(part2(testInput) == 112)

    val input = readInput("Day17")
    println(part1(input))
    println(part2(input))
}
