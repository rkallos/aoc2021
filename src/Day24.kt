import kotlin.math.max
import kotlin.math.min

fun main() {
    fun makeLinks(input: Array<String>): Map<Int, Pair<Int, Int>> {
        val pairs = (0 until 14).map {
            input[it * 18 + 5].substring(6).toInt() to input[it * 18 + 15].substring(6).toInt()
        }
        val stack = emptyList<Pair<Int, Int>>().toMutableList()
        val map = emptyMap<Int, Pair<Int, Int>>().toMutableMap()
        pairs.forEachIndexed { i, (a, b) ->
            if (a > 0) {
                stack.add(i to b)
            } else {
                val (j, bj) = stack.removeLast()
                map[i] = j to (bj + a)
            }
        }
        return map
    }

    fun solve(input: Array<String>, minimize: Boolean = false): String {
        val assignments = emptyMap<Int, Int>().toMutableMap()
        for ((i, p) in makeLinks(input)) {
            val (j, delta) = p
            assignments[i] = if (minimize) max(1, 1 + delta) else min(9, 9 + delta)
            assignments[j] = if (minimize) max(1, 1 - delta) else min(9, 9 - delta)
        }
        return (0 until 14).joinToString("") { assignments[it]!!.toString() }
    }

    fun part1(input: Array<String>): String = solve(input)
    fun part2(input: Array<String>): String = solve(input, true)

    val input = readInput("Day24").toTypedArray()
    println(part1(input))
    println(part2(input))
}
