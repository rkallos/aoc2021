class Board {
    private var rows = ArrayList<ArrayList<Int>>()
    private var drawn = HashMap<Pair<Int, Int>, Boolean>()

    fun addRow(row: ArrayList<Int>) = rows.add(row)

    fun draw(drawnNumber: Int) {
        for ((rowIdx, row) in rows.withIndex()) {
            for ((colIdx, n) in row.withIndex()) {
                if (n == drawnNumber) {
                    drawn[rowIdx to colIdx] = true
                }
            }
        }
    }

    private fun checkRows(): Boolean {
        for (rowIdx in 0 until 5) {
            var winning = true
            for (colIdx in 0 until 5) {
                if (drawn[rowIdx to colIdx] != true) winning = false
            }
            if (winning) return true
        }
        return false
    }

    private fun checkCols(): Boolean {
        for (colIdx in 0 until 5) {
            var winning = true
            for (rowIdx in 0 until 5) {
                if (drawn[rowIdx to colIdx] != true) winning = false
            }
            if (winning) return true
        }
        return false
    }

    fun isWinning(): Boolean = checkRows() || checkCols()

    fun sumUnmarked(): Int {
        var res = 0
        for (r in 0 until 5) {
            for (c in 0 until 5) {
                when (drawn[r to c]) {
                    null -> res += rows[r][c]
                    else -> continue
                }
            }
        }
        return res
    }
}

fun main() {
    fun makeBoards(input: List<String>): ArrayList<Board> {
        val boards = ArrayList<Board>()
        boards.add(Board())
        val re = Regex("[0-9]+")
        input.drop(2).forEach { line ->
            when (line) {
                "" -> boards.add(Board())
                else ->
                    boards[boards.size - 1].addRow(
                        re.findAll(line)
                            .map { it.groupValues[0].toInt() }
                            .toCollection(ArrayList(5))
                    )
            }
        }
        return boards
    }

    fun part1(input: List<String>): Int {
        val draws = input[0].split(",").map { it.toInt() }
        val boards = makeBoards(input)

        for (n in draws) {
            for (b in boards) {
                b.draw(n)
                if (b.isWinning()) {
                    return n * b.sumUnmarked()
                }
            }
        }
        throw Exception("All numbers have been drawn")
    }

    fun part2(input: List<String>): Int {
        val draws = input[0].split(",").map { it.toInt() }
        var boards = makeBoards(input)

        for (n in draws) {
            for (b in boards) {
                b.draw(n)
                if (b.isWinning() && boards.size == 1) {
                    return n * b.sumUnmarked()
                }
            }
            boards = boards.filter { b -> !b.isWinning() }.toCollection(ArrayList())
        }
        throw Exception("All numbers have been drawn")
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 4512)
    check(part2(testInput) == 1924)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}