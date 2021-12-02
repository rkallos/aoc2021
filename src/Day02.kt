fun main() {
    fun splitLine(s: String): Pair<Char, Int> {
        val pieces = s.split(" ")
        return pieces[0][0] to pieces[1].toInt()
    }

    fun part1(input: List<String>): Int {
        var h = 0
        var v = 0
        input.forEach {
            val (dir, amt) = splitLine(it)
            when (dir) {
                'f' -> h += amt
                'b' -> h -= amt
                'd' -> v += amt
                'u' -> v -= amt
            }
        }
        return h * v
    }

    fun part2(input: List<String>): Int {
        var h = 0
        var v = 0
        var aim = 0
        input.forEach {
            val (dir, amt) = splitLine(it)
            when (dir) {
                'f' -> {
                    h += amt
                    v += (aim * amt)
                }
                'b' -> h -= amt
                'd' -> aim += amt
                'u' -> aim -= amt
            }
        }
        return h * v
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 150)
    check(part2(testInput) == 900)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}