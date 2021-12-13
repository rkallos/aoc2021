typealias Graph = HashMap<String, HashSet<String>>

fun main() {
    fun makeGraph(input: List<String>): Graph =
        input.map{ it.split("-") }
            .fold(HashMap<String, HashSet<String>>()) {acc, pair ->
                acc.getOrPut(pair[0]) { HashSet() }.add(pair[1])
                acc.getOrPut(pair[1]) { HashSet() }.add(pair[0])
                acc
            }

    fun dfs(src: String, dst: String, g: Graph, f: (String, List<String>, String?) -> Boolean,
             path: List<String> = listOf(src), specialSmallCave: String? = null)
    : List<List<String>> =
        g.getOrDefault(src, emptySet()).flatMap {
            if (it[0].isLowerCase() && it in path) {
                if (f(it, path, specialSmallCave)) {
                    dfs(it, dst, g, f, path + it, it)
                } else {
                    emptyList()
                }
            } else if (it == dst) {
                listOf(path + dst)
            } else {
                dfs(it, dst, g, f, path + it, specialSmallCave)
            }
        }

    fun part1(input: List<String>): Int = dfs("start", "end", makeGraph(input), { _, _, _ -> false }).size
    fun part2(input: List<String>): Int = dfs("start", "end", makeGraph(input), { v, path, specialSmallCave ->
        if (specialSmallCave == null || path.count { it == specialSmallCave } < 2) {
            !(v == "start" || v == "end")
        } else {
            false
        }
    }).size

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    check(part1(testInput) == 10)
    check(part2(testInput) == 36)

    val testInput2 = readInput("Day12_test2")
    check(part1(testInput2) == 19)
    check(part2(testInput2) == 103)

    val testInput3 = readInput("Day12_test3")
    check(part1(testInput3) == 226)
    check(part2(testInput3) == 3509)

    val input = readInput("Day12")
    println(part1(input))
    println(part2(input))
}
