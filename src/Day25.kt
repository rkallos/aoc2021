fun main() {
    fun copy(torus: List<List<Char>>): MutableList<MutableList<Char>> =
        torus.map { it.toMutableList() }.toMutableList()

    fun canMove(torus: List<List<Char>>, row: Int, col: Int): Boolean =
        when (torus[row][col]) {
            'v' -> torus[(row + 1) % torus.size][col] == '.'
            '>' -> torus[row][(col + 1) % torus[row].size] == '.'
            else -> false
        }

    fun step(torus: List<List<Char>>): Pair<Int, MutableList<MutableList<Char>>> {
        val movedEast = copy(torus)
        var moved = 0
        for ((rIdx, row) in torus.withIndex()) {
            for ((cIdx, c) in row.withIndex()) {
                if (c == '>' && canMove(torus, rIdx, cIdx)) {
                    moved += 1
                    movedEast[rIdx][cIdx] = '.'
                    movedEast[rIdx][(cIdx + 1) % row.size] = '>'
                }
            }
        }
        val movedSouth = copy(movedEast)
        for ((rIdx, row) in movedSouth.withIndex()) {
            for ((cIdx, c) in row.withIndex()) {
                if (c == 'v' && canMove(movedEast, rIdx, cIdx)) {
                    moved += 1
                    movedSouth[rIdx][cIdx] = '.'
                    movedSouth[(rIdx + 1) % movedSouth.size][cIdx] = 'v'
                }
            }
        }
        return moved to movedSouth
    }

    fun part1(input: List<String>): Int {
        var current = copy(input.map { it.toList() })
        var steps = 0
        while (true) {
            steps += 1
            val (moved, next) = step(current)
            if (moved == 0) return steps
            current = next
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day25_test")
    check(part1(testInput) == 58)

    val input = readInput("Day25")
    println(part1(input))
}
