import kotlin.math.abs

data class Point3D(val x: Int, val y: Int, val z: Int) {
    operator fun minus(that: Point3D): Point3D = Point3D(x - that.x, y - that.y, z - that.z)
    operator fun plus(that: Point3D): Point3D = Point3D(x + that.x, y + that.y, z + that.z)
    operator fun get(axis: Int) = arrayOf(x, y, z)[axis]
    fun mh(that: Point3D): Int = abs(x - that.x) + abs(y - that.y) + abs(z - that.z)
}

fun main() {
    fun parseProbes(input: List<String>): List<List<Point3D>> {
        return input.joinToString("\n").split("\n\n").map { g ->
            g.split("\n").drop(1).map { l ->
                val pts = l.split(",").map { it.toInt() }
                Point3D(pts[0], pts[1], pts[2])
            }
        }
    }

    fun tryAlign(aligned: List<Point3D>, candidate: List<Point3D>): Pair<List<Point3D>, Point3D>? {
        val ret: MutableList<List<Int>> = emptyList<List<Int>>().toMutableList()
        val shift: MutableList<Int> = emptyList<Int>().toMutableList()
        val dimsAlreadyMatched = emptySet<Int>().toMutableSet()
        val dimsToTry = listOf((0 to 1), (1 to 1), (2 to 1), (0 to -1), (1 to -1), (2 to -1))
        for (dim in 0..2) {
            val alignedPosns = aligned.map { it[dim] }
            var c = 0 to 0
            for ((cDim, sign) in dimsToTry) {
                if (dimsAlreadyMatched.contains(cDim)) continue
                val candidatePosns = candidate.map { it[cDim] * sign }
                c = alignedPosns
                    .flatMap { al -> candidatePosns.map { ca -> ca - al} }
                    .fold(emptyMap<Int, Int>().toMutableMap()) { acc, delta ->
                        acc.apply { this[delta] = getOrDefault(delta, 0) + 1 }
                    }
                    .maxByOrNull { it.value }!!.toPair()
                if (c.second >= 12) {
                    dimsAlreadyMatched.add(cDim)
                    ret.add(candidatePosns.map { it - c.first })
                    shift.add(c.first)
                    break
                }
            }
            if (c.second < 12) return null
        }
        val pts = ret[0].zip(ret[1]).zip(ret[2]) { (x, y), z -> Point3D(x, y, z) }
        return (pts to Point3D(shift[0], shift[1], shift[2]))
    }

    fun solve(input: List<String>): Pair<Int, Int> {
        val probes = parseProbes(input)
        val beacons = emptySet<Point3D>().toMutableSet()
        val shifts = emptySet<Point3D>().toMutableSet()

        val next = probes.take(1).toMutableList()
        var rest = probes.drop(1).toMutableList()
        while (next.isNotEmpty()) {
            val aligned = next.removeFirst()
            val tmp = emptyList<List<Point3D>>().toMutableList()
            for (candidate in rest) {
                val r = tryAlign(aligned, candidate)
                if (r != null) {
                    val (updated, shift) = r
                    shifts.add(shift)
                    next.add(updated)
                } else {
                    tmp.add(candidate)
                }
            }
            rest = tmp
            beacons.addAll(aligned)
        }

        return beacons.size to shifts.flatMap { a -> shifts.map { b -> a.mh(b) } }.maxOf { it }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day19_test")
    check(solve(testInput) == 79 to 3621)

    val input = readInput("Day19")
    println(solve(input))
}
