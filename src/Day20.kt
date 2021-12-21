fun main() {
    fun neighbors(pt: Pair<Int, Int>): List<Pair<Int, Int>> {
        val (r, c) = pt
        return intArrayOf(-1, 0, 1).flatMap {dr -> intArrayOf(-1, 0, 1).map {dc -> (r + dr) to (c + dc)}}
    }

    fun kernel(grid: Map<Pair<Int, Int>, Boolean>, pt: Pair<Int, Int>, gen: Int, bitmap: ArrayList<Boolean>): Int {
        return neighbors(pt).fold(0) { acc, (r, c) ->
            acc.shl(1) + (when (grid[r to c]) {
                true -> 1
                false -> 0
                else -> if (bitmap[0]) (gen % 2) else 0
            })
        }
    }

    fun enhance(m: Map<Pair<Int, Int>, Boolean>, bitmap: ArrayList<Boolean>, gen: Int): Map<Pair<Int, Int>, Boolean> {
        return m.keys.fold(m.keys.toMutableSet()) { acc, pt ->
            acc.apply { addAll(neighbors(pt)) }
        }.associateWith { pt -> bitmap[kernel(m, pt, gen, bitmap)] }
    }

    fun part1(input: List<String>, n: Int = 2): Int {
        val bitmap = input[0].map { it == '#' }.toCollection(ArrayList())

        val m = emptyMap<Pair<Int, Int>, Boolean>().toMutableMap()

        for ((rIdx, r) in input.drop(2).withIndex()) {
            for ((cIdx, c) in r.withIndex()) {
                m[rIdx to cIdx] = (c == '#')
            }
        }

        return (0 until n).fold(m.toMap()) { acc, n -> enhance(acc, bitmap, n) }.count { it.value }
    }

    fun part2(input: List<String>): Int {
        return part1(input, 50)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day20_test")
    check(part1(testInput) == 35)
    check(part2(testInput) == 3351)

    val input = readInput("Day20")
    println(part1(input))
    println(part2(input))
}
