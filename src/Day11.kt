fun main() {

    fun step(grid: Grid): Int {
        var allFlashes = HashSet<Pair<Int, Int>>()
        // add 1 to every cell, collect flashes
        grid.forEachIndexed {rIdx, r -> r.forEachIndexed { cIdx, _ ->
            grid[rIdx][cIdx] += 1
            if (grid[rIdx][cIdx] > 9) allFlashes += (rIdx to cIdx)
        } }
        // increment neighbors of flashes, add new flashes, repeat until fixpoint
        var newFlashes = allFlashes
        while (newFlashes.size > 0) {
            allFlashes += newFlashes
            newFlashes.forEach { (r, c) ->
                val neighbors = intArrayOf(-1, 0, 1).flatMap {r -> intArrayOf(-1, 0, 1).map {c -> r to c}} - (0 to 0)
                neighbors.forEach {(dr, dc) ->
                    if ((r + dr in 0 until grid.size) && (c + dc in 0 until grid[r].size))
                        grid[r + dr][c + dc] += 1
                }
            }
            newFlashes = HashSet()
            grid.forEachIndexed {rIdx, r -> r.forEachIndexed { cIdx, c ->
                if (c > 9 && !allFlashes.contains(rIdx to cIdx))
                    newFlashes += (rIdx to cIdx)
            } }
        }
        // reset flashed cells to 0
        allFlashes.forEach { (r, c) -> grid[r][c] = 0 }
        return allFlashes.size
    }

    fun part1(input: List<String>): Int {
        val grid: Grid = input.map { l -> l.map { it.digitToInt() }.toCollection(ArrayList()) }.toCollection(ArrayList())
        return (1..100).sumOf { step(grid) }
    }

    fun part2(input: List<String>): Int {
        val grid: Grid = input.map { l -> l.map { it.digitToInt() }.toCollection(ArrayList()) }.toCollection(ArrayList())
        var step = 0
        do {
            step += 1
            step(grid)
        } while (grid.sumOf {r -> r.sumOf {it}} != 0)
        return step
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    check(part1(testInput) == 1656)
    check(part2(testInput) == 195)

    val input = readInput("Day11")
    println(part1(input))
    println(part2(input))
}
