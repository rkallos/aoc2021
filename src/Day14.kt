fun main() {

    fun part1(input: List<String>, n: Int = 10): Long {
        val start = HashMap<String, Long>()
            .apply { input[0].windowed(2).forEach { put(it, getOrDefault(it, 0) + 1) } }

        val rules = input
            .filter { " -> " in it }
            .associate { val sp = it.split(" -> "); sp[0] to sp[1][0] }

        val counts = HashMap<Char, Long>()
            .apply { for (c in input[0]) put(c, getOrDefault(c, 0) + 1) }

        (0 until n).fold(start) { acc, _ ->
            HashMap<String, Long>().apply {
                for ((p, count) in acc) {
                    val c = rules[p]!!
                    val p1 = "${p[0]}${c}"
                    val p2 = "${c}${p[1]}"
                    put(p1, getOrDefault(p1, 0) + count)
                    put(p2, getOrDefault(p2, 0) + count)
                    counts[c] = counts.getOrDefault(c, 0) + count
                }
            }
        }

        return (counts.maxOf { it.value } - counts.minOf { it.value })
    }

    fun part2(input: List<String>): Long = part1(input, 40)

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")
    check(part1(testInput) == 1588L)
    check(part2(testInput) == 2188189693529)

    val input = readInput("Day14")
    println(part1(input))
    println(part2(input))
}
