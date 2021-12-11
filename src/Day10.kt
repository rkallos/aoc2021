import java.util.*

fun main() {
    fun check(line: String): Int {
        val points = hashMapOf<Char, Int>(
            ')' to 3,
            ']' to 57,
            '}' to 1197,
            '>' to 25137
        )
        val stack = Stack<Char>()
        for (ch in line) {
            when (ch) {
                '(' -> stack.push(')')
                '[' -> stack.push(']')
                '{' -> stack.push('}')
                '<' -> stack.push('>')
                ')', ']', '}', '>' -> {
                    if (stack.peek() == ch) {
                        stack.pop()
                    } else {
                        return points[ch]!!
                    }
                }
            }
        }
        return 0
    }

    fun part1(input: List<String>): Int = input.sumOf {check(it)}

    fun check2(line: String): Long {
        val points = hashMapOf<Char, Long>(
            ')' to 1,
            ']' to 2,
            '}' to 3,
            '>' to 4
        )
        val stack = Stack<Char>()
        for (ch in line) {
            when (ch) {
                '(' -> stack.push(')')
                '[' -> stack.push(']')
                '{' -> stack.push('}')
                '<' -> stack.push('>')
                ')', ']', '}', '>' -> {
                    if (stack.peek() == ch) {
                        stack.pop()
                    } else {
                        return 0
                    }
                }
            }
        }
        return stack.reversed().fold(0.toLong()) {acc, c -> (acc * 5) + points[c]!! }
    }

    fun part2(input: List<String>): Long {
        val res = input.map { check2(it) }.filter {it != 0.toLong()}.sorted()
        return res[res.size / 2]
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    check(part1(testInput) == 26397)
    check(part2(testInput) == 288957.toLong())

    val input = readInput("Day10")
    println(part1(input))
    println(part2(input))
}
