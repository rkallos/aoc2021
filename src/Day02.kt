fun main() {
    fun splitLine(s: String): Pair<String, Int> {
        val pieces = s.split(" ")
        return pieces[0] to pieces[1].toInt()
    }

    fun part1(input: List<String>): Int {
        var h = 0
        var v = 0
        input.forEach {
            val (dir, amt) = splitLine(it)
            when (dir) {
                "forward" -> h += amt
                "backward" -> h -= amt
                "down" -> v += amt
                "up" -> v -= amt
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
                "forward" -> {
                    h += amt
                    v += (aim * amt)
                }
                "backward" -> h -= amt
                "down" -> aim += amt
                "up" -> aim -= amt
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