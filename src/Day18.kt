import kotlin.math.ceil
import kotlin.math.floor

data class SnailNum(var n: Int, var d: Int)

fun main() {
    fun flatten(input: String): MutableList<SnailNum> {
        var d = -1
        val out = emptyList<SnailNum>().toMutableList()
        for (c in input) {
            if (c == '[')
                d += 1
            else if (c == ']')
                d -= 1
            else if (c.isDigit())
                out.add(SnailNum(c.digitToInt(), d))
        }
        return out
    }

    fun explode(idx: Int, sn: MutableList<SnailNum>): Unit {
        if (idx > 0)
            sn[idx-1].n += sn[idx].n
        if (idx + 2 < sn.size)
            sn[idx+2].n += sn[idx+1].n
        sn.add(idx, SnailNum(0, sn[idx].d-1))
        sn.removeAt(idx + 1)
        sn.removeAt(idx + 1)
    }

    fun split(idx: Int, sn: MutableList<SnailNum>): Unit {
        val d = sn[idx].d + 1
        val n = sn[idx].n
        sn.removeAt(idx)
        sn.add(idx, SnailNum(floor(n / 2.0).toInt(), d))
        sn.add(idx+1, SnailNum(ceil(n / 2.0).toInt(), d))
    }

    fun reduce(sn: MutableList<SnailNum>): MutableList<SnailNum> {
        fun step(sn: MutableList<SnailNum>): Boolean {
            var idx = sn.indexOfFirst { it.d == 4 }
            if (idx >= 0) {
                explode(idx, sn)
                return true
            }
            idx = sn.indexOfFirst { it.n >= 10 }
            if (idx >= 0) {
                split(idx, sn)
                return true
            }
            return false
        }
        while (step(sn)) {}
        return sn
    }

    fun add(sn1: List<SnailNum>, sn2: List<SnailNum>): MutableList<SnailNum> =
        sn1.plus(sn2).map { SnailNum(it.n, it.d+1) }.toMutableList()

    fun magnitude(sn: MutableList<SnailNum>): Int {
        while (sn.size > 1) {
            val maxD = sn.maxOf { it.d }
            val idx = sn.indexOfFirst { it.d == maxD }
            val e = sn.removeAt(idx)
            val f = sn.removeAt(idx)
            sn.add(idx, SnailNum(3 * e.n + 2 * f.n, maxD - 1))
        }
        return sn[0].n
    }

    fun sum(nums: List<MutableList<SnailNum>>): Int =
        magnitude(nums.drop(1).fold(nums[0]) { acc, n -> reduce(add(acc, n)) })

    fun part1(input: List<String>): Int = sum(input.map { flatten(it) })

    fun part2(input: List<String>): Int {
        val nums = input.map { flatten(it) }
        var max = 0
        for ((idx1, num1) in nums.withIndex()) {
            for ((idx2, num2) in nums.withIndex()) {
                if (idx1 == idx2) continue
                max = maxOf(max, sum(listOf(num1, num2)), sum(listOf(num2, num1)))
            }
        }
        return max
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day18_test")
    check(part1(testInput) == 4140)
    check(part2(testInput) == 3993)

    val input = readInput("Day18")
    println(part1(input))
    println(part2(input))
}
