fun main() {
    fun part1(input: List<String>): Int =
        input
            .map { it.split("|")[1].trim() }
            .sumOf { line ->
                line.split(" ").count {
                    val l = it.length
                    (l == 2) || (l == 3) || (l == 4) || (l == 7)
                }
            }

    fun digitsOf(sets: List<Set<Char>>): HashMap<Set<Char>, Int> {
        val out = HashMap<Set<Char>, Int>()
        sets.forEach {
            when (it.size) {
                2 -> out[it] = 1
                3 -> out[it] = 7
                4 -> out[it] = 4
                7 -> out[it] = 8
            }
        }
        val digitsForSeven = sets.first { it.size == 3 }

        val digitsForThree = sets.first { it.containsAll(digitsForSeven) && (it - digitsForSeven).size == 2 }
        out[digitsForThree] = 3

        val digitsForNine = sets.first { it.containsAll(digitsForThree) && (it - digitsForThree).size == 1 }
        out[digitsForNine] = 9

        val digitsForZero = sets.first { (it.size == 6) && (it != digitsForNine) && (it.containsAll(digitsForSeven)) }
        out[digitsForZero] = 0

        val digitsForSix = sets.first { (it.size == 6) && (it != digitsForNine) && (it != digitsForZero) }
        out[digitsForSix] = 6

        val digitsForFive = sets.first { (it.size == 5) && (digitsForSix.containsAll(it)) }
        out[digitsForFive] = 5

        val digitsForTwo = sets.first { (it.size == 5) && (it != digitsForFive) && (it != digitsForThree) }
        out[digitsForTwo] = 2

        return out
    }

    fun solve(puzzle: List<Set<Char>>, digits: HashMap<Set<Char>, Int>): Int =
        puzzle
            .map { digits.getOrElse(it) { throw Exception("Missing digit") } }
            .joinToString("")
            .toInt()

    fun part2(input: List<String>): Int =
        input.sumOf {
            val split = it.split("|").map { l -> l.trim().split(" ").map { s -> s.toSet() } }
            solve(split[1], digitsOf(split[0]))
        }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 26)
    check(part2(testInput) == 61229)

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}
