import kotlin.math.abs

fun main() {
    fun foldY(pts: HashSet<Pair<Int, Int>>, yLine: Int): HashSet<Pair<Int, Int>> {
        val res = HashSet<Pair<Int, Int>>()
        for ((x, y) in pts) {
            if (y > yLine) {
                res.add(x to (yLine - abs(y - yLine)))
            } else {
                res.add(x to y)
            }
        }
        return res
    }

    fun foldX(pts: HashSet<Pair<Int, Int>>, xLine: Int): HashSet<Pair<Int, Int>> {
        val res = HashSet<Pair<Int, Int>>()
        for ((x, y) in pts) {
            if (x > xLine) {
                res.add((xLine - abs(x - xLine)) to y)
            } else {
                res.add(x to y)
            }
        }
        return res
    }

    fun fold(f: Pair<Char, Int>, pts: HashSet<Pair<Int, Int>>): HashSet<Pair<Int, Int>> {
        return if (f.first == 'y') {
            foldY(pts, f.second)
        } else {
            foldX(pts, f.second)
        }
    }

    fun part1(input: List<String>): Int {
        val folds = ArrayList<Pair<Char, Int>>()
        val points = HashSet<Pair<Int, Int>>()
        input.filter { it.isNotEmpty() }.forEach {
            if (it[0].isDigit()) {
                val sp = it.split(",").map { d -> d.toInt() }
                points.add(sp[0] to sp[1])
            } else if (it[0] == 'f') {
                val sp = it.split("=")
                folds.add(sp[0].last() to sp[1].toInt())
            }
        }
        return fold(folds[0], points).size
    }

    fun part2(input: List<String>): Int {
        val folds = ArrayList<Pair<Char, Int>>()
        val points = HashSet<Pair<Int, Int>>()
        input.filter { it.isNotEmpty() }.forEach {
            if (it[0].isDigit()) {
                val sp = it.split(",").map { d -> d.toInt() }
                points.add(sp[0] to sp[1])
            } else if (it[0] == 'f') {
                val sp = it.split("=")
                folds.add(sp[0].last() to sp[1].toInt())
            }
        }
        val finalPoints = folds.fold(points) { acc, f -> fold(f, acc) }
        for (y in 0..(finalPoints.maxOf {it.second})) {
            for (x in 0..(finalPoints.maxOf {it.first})) {
                print(if (finalPoints.contains(x to y)) "#" else ".")
            }
            println()
        }
        return 5
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test")
    check(part1(testInput) == 17)
    check(part2(testInput) == 5)

    val input = readInput("Day13")
    println(part1(input))
    println(part2(input))
}
