import kotlin.math.max

data class Dirac(val p1posn: Int, val p1score: Int, val p2posn: Int, val p2score: Int) {
    fun advance(posn: Int, score: Int, roll: Int): Pair<Int, Int> {
        val newPos = if ((posn + roll) % 10 == 0) 10 else (posn + roll) % 10
        return newPos to (score + newPos)
    }

    fun advancePlayer1(roll: Int): Dirac {
        val (newPosn, newScore) = advance(p1posn, p1score, roll)
        return Dirac(newPosn, newScore, p2posn, p2score)
    }

    fun advancePlayer2(roll: Int): Dirac {
        val (newPosn, newScore) = advance(p2posn, p2score, roll)
        return Dirac(p1posn, p1score, newPosn, newScore)
    }
}

fun main() {
    fun advance(p: Pair<Int, Int>, roll: Int): Pair<Int, Int> {
        val (pos, score) = p
        val newPos = if ((pos + roll) % 10 == 0) 10 else (pos + roll) % 10
        return (newPos) to (score + newPos)
    }

    fun part1(input: List<String>): Int {
        var die = 1
        var p1 = input[0].last().digitToInt() to 0
        var p2 = input[1].last().digitToInt() to 0
        while (true) {
            var roll = die++ + die++ + die++
            p1 = advance(p1, roll)
            if (p1.second >= 1000) return p2.second * (die - 1)
            roll = die++ + die++ + die++
            p2 = advance(p2, roll)
            if (p2.second >= 1000) return p1.second * (die - 1)
        }
    }

    fun part2(input: List<String>): Long {
        val dist3d3 = mapOf((3 to 1), (4 to 3), (5 to 6), (6 to 7), (7 to 6), (8 to 3), (9 to 1))
        var player1Wins = 0L
        var player2Wins = 0L
        var states: Map<Dirac, Long> =
            mapOf(Dirac(input[0].last().digitToInt(), 0, input[1].last().digitToInt(), 0) to 1L)
        while (states.isNotEmpty()) {
            val newStates = emptyMap<Dirac, Long>().toMutableMap()
            for ((state, stateCount) in states) {
                for ((roll, rollCount) in dist3d3) {
                    val stateAfterPlayer1 = state.advancePlayer1(roll)
                    if (stateAfterPlayer1.p1score >= 21) {
                        player1Wins += (stateCount * rollCount)
                    } else {
                        newStates[stateAfterPlayer1] = newStates.getOrDefault(stateAfterPlayer1, 0) + (stateCount * rollCount)
                    }
                }
            }
            val newStates2 = emptyMap<Dirac, Long>().toMutableMap()
            for ((state, stateCount) in newStates) {
                for ((roll, rollCount) in dist3d3) {
                    val stateAfterPlayer2 = state.advancePlayer2(roll)
                    if (stateAfterPlayer2.p2score >= 21) {
                        player2Wins += (stateCount * rollCount)
                    } else {
                        newStates2[stateAfterPlayer2] = newStates2.getOrDefault(stateAfterPlayer2, 0) + (stateCount * rollCount)
                    }
                }
            }
            states = newStates2
        }
        return max(player1Wins, player2Wins)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day21_test")
    check(part1(testInput) == 739785)
    check(part2(testInput) == 444356092776315)

    val input = readInput("Day21")
    println(part1(input))
    println(part2(input))
}
