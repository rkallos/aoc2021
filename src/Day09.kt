typealias DjsGrid = ArrayList<ArrayList<Djs>>

class Djs(val pt: Pair<Int, Int>, val depth: Int) {
    var parent: Djs = this
    var size: Int = 1

    private fun find(): Djs {
        return if (parent != this) {
            parent = parent.find()
            parent
        } else {
            this
        }
    }

    private fun union(that: Djs): Unit {
        val thisRoot = this.find()
        val thatRoot = that.find()

        if (thisRoot.pt == thatRoot.pt) return

        if (thisRoot.depth < thatRoot.depth) {
            thatRoot.parent = thisRoot
            thisRoot.size += thatRoot.size
        } else {
            thisRoot.parent = thatRoot
            thatRoot.size += thisRoot.size
        }
    }

    fun findBasin(grid: DjsGrid): Djs {
        if (this.depth == 9) return this
        val n: Djs? = grid.getOrNull(pt.first - 1)?.getOrNull(pt.second)
        val w: Djs? = grid.getOrNull(pt.first)?.getOrNull(pt.second - 1)

        for (neighbor in arrayOf(n, w)) {
            if (neighbor == null || neighbor.depth == 9) continue

            if (depth < neighbor.depth) {
                neighbor.union(this)
            } else {
                this.union(neighbor)
            }
        }
        return parent // unreachable?
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        val grid = Grid(input.size)
        for (line in input) grid.add(line.map { it.digitToInt() }.toCollection(ArrayList(line.length)))
        return grid.indices
            .flatMap {r -> grid[0].indices.map {c -> r to c}}
            .sumOf { (row, col) ->
                val pt = grid[row][col]
                if (listOf( -1 to 0, 1 to 0, 0 to -1, 0 to 1)
                        .all { (dr, dc) ->
                            pt < (grid.getOrNull(row + dr)?.getOrNull(col + dc) ?: 9) })
                    pt + 1 else 0
            }
    }

    fun part2(input: List<String>): Int {
        // UNION FIND!
        val grid = input.mapIndexed { rowIdx, line ->
            line.mapIndexed { colIdx, digit ->
                Djs(rowIdx to colIdx, digit.digitToInt())
            }.toCollection(ArrayList(line.length))
        }.toCollection(ArrayList(input.size))

        return grid
            .flatMap { row -> row.map { it.findBasin(grid) } }
            .toSet()
            .sortedWith { a, b -> b.size - a.size }
            .take(3)
            .fold(1) { a, b -> a * b.size }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 1134)

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
}

