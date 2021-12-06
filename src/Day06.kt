inline fun LongArray.rotate(): LongArray = LongArray(size) { this[(it + 1) % size] }

fun main() {
    fun nextDay(fish: LongArray): LongArray {
        val res = fish.rotate()
        res[6] += res[8]
        return res
    }

    fun solve(input: List<String>, days: Int): Long {
        var fish = longArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0)
        intsSplitBy(input[0], ",").forEach { fish[it]++ }
        for (d in 1..days) fish = nextDay(fish)
        return fish.sum()
    }

    fun part1(input: List<String>) = solve(input, 80)
    fun part2(input: List<String>) = solve(input, 256)

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 5934.toLong())
    check(part2(testInput) == 26984457539)

    val input = readInput("Day06")
    println(part1(input))
    println(part2(input))
}
