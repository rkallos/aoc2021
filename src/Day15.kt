import java.util.*

data class V(val r: Int, val c: Int)
data class C(val cost: Int, val v: V) : Comparable<C> {
    override fun compareTo(other: C) = compareValuesBy(this, other) { it.cost }
}

fun main() {
    fun neighbors(v: V, maxR: Int, maxC: Int): List<V> =
        listOf(
            V(v.r + 1, v.c),
            V(v.r, v.c + 1),
            V(v.r - 1, v.c),
            V(v.r, v.c - 1)
        ).filter { it.r in 0..maxR && it.c in 0..maxC }

    fun dijkstra(g: HashMap<V, Int>, src: V, dst: V): Int {
        val maxR = g.keys.maxOf { it.r }
        val maxC = g.keys.maxOf { it.c }

        val unvisited = g.keys.toMutableSet()
        val tentativeRisks = g.keys.associateWith { 9999 }.toMutableMap()
        tentativeRisks[src] = 0

        val pq = PriorityQueue<C>()
        pq.addAll(tentativeRisks.toList().map { C(it.second, it.first) })
        var current = src

        while (unvisited.contains(dst)) {
            if (unvisited.contains(current)) {
                for (n in neighbors(current, maxR, maxC)) {
                    if (!unvisited.contains(n)) continue
                    val alt = tentativeRisks[current]?.plus(g[n]!!)!!
                    if (alt < tentativeRisks[n]!!) {
                        tentativeRisks[n] = alt
                        pq.add(C(alt, n))
                    }
                }
            }
            unvisited.remove(current)
            current = pq.remove()!!.v
        }
        return tentativeRisks[dst]!!
    }

    fun part1(input: List<String>): Int {
        val g = HashMap<V, Int>(input.size * input[0].length)
        for ((rIdx, r) in input.withIndex()) {
            for ((cIdx, c) in r.withIndex()) {
                g[V(rIdx, cIdx)] = c.digitToInt()
            }
        }

        return dijkstra(g, V(0, 0), V(input.size - 1, input[0].length - 1))
    }

    fun part2(input: List<String>): Int {
        val grid = input.map { r -> r.map { c -> c.digitToInt() }.toCollection(ArrayList()) }.toCollection(ArrayList())
        val rMax = grid.size
        val cMax = grid[0].size
        val g = HashMap<V, Int>((input.size * 5) * (input[0].length * 5))
        for (rIdx in 0 until (input.size * 5)) {
            for (cIdx in 0 until (input[0].length * 5)) {
                var v = grid[rIdx % rMax][cIdx % cMax] + (rIdx / rMax) + (cIdx / cMax)
                while (v > 9) v -= 9
                g[V(rIdx, cIdx)] = v
            }
        }

        return dijkstra(g, V(0, 0), V((rMax * 5) - 1, (cMax * 5) - 1))
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test")
    check(part1(testInput) == 40)
    check(part2(testInput) == 315)

    val input = readInput("Day15")
    println(part1(input))
    println(part2(input))
}
