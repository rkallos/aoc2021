fun main() {
    fun nextDay(fish: HashMap<Int, Long>): HashMap<Int, Long> {
        val res = hashMapOf<Int, Long>()
        for ((daysUntilSpawn, numFish) in fish) res[daysUntilSpawn - 1] = numFish
        val spawningFish = res.remove(-1) ?: 0
        res[8] = (res[8] ?: 0) + spawningFish
        res[6] = (res[6] ?: 0) + spawningFish
        return res
    }

    fun solve(input: List<String>, days: Int): Long {
        var fish = hashMapOf<Int, Long>()
        input[0].split(",").map { it.toInt() }.forEach { fish[it] = (fish[it] ?: 0) + 1 }
        for (d in 1..days) fish = nextDay(fish)
        return fish.values.sum()
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
