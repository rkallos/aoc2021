fun main() {
    fun Boolean.toInt() = if (this) 1 else 0

    fun part1(input: List<String>): Int {
        val occs = Array(input[0].length) { 0 }
        input.forEach { l -> l.map { it - '0' }.mapIndexed { i, d -> occs[l.length - i - 1] += d } }

        val gamma = occs.map { it * 2 > input.size }.foldIndexed(0) { i, acc, b -> acc.or(b.toInt().shl(i)) }
        val epsilon = gamma.inv().and(1.shl(input[0].length) - 1)

        return gamma * epsilon
    }

    fun nthBitIs(i: Int, pos: Int, expect: Int): Boolean =
        i.and(1.shl(pos)).shr(pos) == expect

    fun onesCount(input: List<Int>, pos: Int): Int =
        input.count { it.or(1.shl(pos)) == it }

    fun part2(input: List<String>): Int {
        var susO2 = input.map { it.toInt(2) }
        var susCO2 = susO2
        for (pos in (input[0].length - 1 downTo 0)) {
            susO2 = if (susO2.size == 1) susO2 else
                susO2.filter { nthBitIs(it, pos, (onesCount(susO2, pos) * 2 >= susO2.size).toInt()) }
            susCO2 = if (susCO2.size == 1) susCO2 else
                susCO2.filter { nthBitIs(it, pos, (onesCount(susCO2, pos) * 2 < susCO2.size).toInt()) }
        }

        return susO2[0] * susCO2[0]
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 198)
    check(part2(testInput) == 230)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}
