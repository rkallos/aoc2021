import kotlin.math.abs
import kotlin.math.max

data class Point(val x: Int, val y: Int) {
    fun delta(dx: Int, dy: Int) = Point(x + dx, y + dy)
}

data class Line(val from: Point, val to: Point) {
    fun isStraight(): Boolean =
        (from.x == to.x) || (from.y == to.y)

    fun isDiagonal(): Boolean =
        abs(from.x - to.x) == abs(from.y - to.y)

    fun points(): List<Point> {
        if (!isStraight() && !isDiagonal()) {
            return emptyList()
        }
        val diffX = to.x - from.x
        val dx = if (diffX == 0) 0 else (diffX / abs(diffX))
        val diffY = to.y - from.y
        val dy = if (diffY == 0) 0 else (diffY / abs(diffY))
        return (0..max(abs(diffX), abs(diffY))).map { d -> from.delta(d * dx, d * dy) }
    }
}

fun main() {
    fun parseLine(line: String): Line {
        val pts = Regex("[0-9]+").findAll(line)
            .map { it.value.toInt() }
            .chunked(2) { (x, y) -> Point(x, y) }
            .toList()
        return Line(pts[0], pts[1])
    }

    fun solve(lines: List<Line>): Int {
        val grid = HashMap<Point, Int>()
        for (line in lines) {
            for (point in line.points()) {
                grid[point] = grid.getOrDefault(point, 0) + 1
            }
        }
        return grid.filter { it.value > 1 }.count()
    }

    fun part1(input: List<String>): Int {
        return solve(input.map { parseLine(it) }.filter { it.isStraight() })
    }

    fun part2(input: List<String>): Int {
        return solve(input.map { parseLine(it) })
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 5)
    check(part2(testInput) == 12)

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}
