fun main() {
    fun solve(input: List<String>, days: Int): Long {
        var fish = longArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0)
        intsSplitBy(input[0], ",").forEach { fish[it]++ }
        for (d in 1..days) {
            fish = LongArray(fish.size) { fish[(it + 1) % fish.size]}
            fish[6] += fish[8]
        }
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
