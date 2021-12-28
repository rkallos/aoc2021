import java.util.*
import kotlin.math.abs

data class Room(val c: Char, val roomsIdx: Int, val hallIdx: Int, val cost: Int)

data class State(val l: List<List<Char>>) {
    fun isFinished(): Boolean =
        rooms.flatten().zip(sequence { yieldAll("ABCD".toList()) }.asIterable()).all { it.first == it.second }

    companion object {
        val destinationRooms = mapOf(
            'A' to Room('A', 0,2, 1),
            'B' to Room('B', 1,4, 10),
            'C' to Room('C', 2,6, 100),
            'D' to Room('D', 3,8, 1000)
        )

        fun from(input: List<String>, extra: List<List<Char>> = emptyList()): State = input
            .drop(2).dropLast(1)
            .map { line -> line.toList().filter { it in 'A'..'D' } }
            .toMutableList()
            .apply {if (extra.isNotEmpty()) addAll(1, extra) }
            .apply { add(0, MutableList(11) { '.' })}
            .let { State(it) }
    }

    private val hallway = l[0]
    private val rooms = l.drop(1)
    private val legalHallwayIndices = listOf(0, 1, 3, 5, 7, 9, 10).filter { hallway[it] == '.' }

    private fun getRoom(c: Char): List<Char> =
        rooms.map { it[destinationRooms.getValue(c).roomsIdx] }

    private fun amphipodsInHallwayThatCanMove(): List<IndexedValue<Char>> =
        hallway.withIndex().filter { it.value.isLetter() && getRoom(it.value).all { ch -> ch == '.' || ch == it.value } }

    private fun roomsWithWrongAmphipods(): List<Char> =
        ('A'..'D').filter { getRoom(it).any { ch -> ch != it && ch != '.' } }

    private fun hallwayPathIsClear(from: Int, to: Int): Boolean {
        val range = if (from > to) (from - 1) downTo to else (from + 1)..to
        return hallway.slice(range).all { it == '.' }
    }

    fun nextStates(): List<StateWithCost> = buildList {
        amphipodsInHallwayThatCanMove()
            .forEach { (idx, a) ->
                val room = destinationRooms.getValue(a)
                if (hallwayPathIsClear(idx, room.hallIdx)) {
                    val y = rooms.map { it[room.roomsIdx] }.lastIndexOf('.') + 1
                    val cost = (abs(idx - room.hallIdx) + y) * room.cost
                    add(StateWithCost(State(
                        l.map { it.toMutableList() }.apply {
                            get(0)[idx] = '.'
                            get(y)[room.roomsIdx] = a
                        }
                    ), cost))
                }
            }
        roomsWithWrongAmphipods()
            .forEach { c ->
                val room = destinationRooms.getValue(c)
                val toMove = rooms.map { it[room.roomsIdx] }.withIndex().first { it.value != '.' }
                legalHallwayIndices.forEach { idx ->
                    if (hallwayPathIsClear(idx, room.hallIdx)) {
                        val y = toMove.index + 1
                        val cost = (abs(room.hallIdx - idx) + y) * destinationRooms.getValue(toMove.value).cost
                        add(StateWithCost(State(
                            l.map { it.toMutableList() }.apply {
                                get(0)[idx] = toMove.value
                                get(y)[room.roomsIdx] = '.'
                            }
                        ), cost))
                    }
                }
            }
        }
}

data class StateWithCost(val state: State, val cost: Int) : Comparable<StateWithCost> {
    override fun compareTo(other: StateWithCost): Int = cost.compareTo(other.cost)
}

fun main() {
    fun solve(s: State): Int {
        val unvisited = PriorityQueue<StateWithCost>().apply {add(StateWithCost(s, 0))}
        val visited = mutableSetOf<StateWithCost>()
        val minCosts = mutableMapOf<State, Int>().withDefault { Int.MAX_VALUE }
        while (unvisited.isNotEmpty()) {
            val current = unvisited.poll().also { visited.add(it) }
            current.state.nextStates().forEach { next ->
                if (!visited.contains(next)) {
                    val newCost = current.cost + next.cost
                    if (newCost < minCosts.getValue(next.state)) {
                        minCosts[next.state] = newCost
                        unvisited.add(StateWithCost(next.state, newCost))
                    }
                }
            }
            if (current.state.isFinished()) return minCosts.getValue(current.state)
        }
        throw(Exception("no visited states were finished"))
    }

    fun part1(input: List<String>): Int = solve(State.from(input))
    fun part2(input: List<String>): Int = solve(State.from(input, listOf("DCBA", "DBAC").map { it.toList() }))

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day23_test")
    check(part1(testInput) == 12521)
    check(part2(testInput) == 44169)

    val input = readInput("Day23")
    println(part1(input))
    println(part2(input))
}