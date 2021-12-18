interface Packet {
    fun versionSum(): Long
    fun eval(): Long
}

data class Literal(val version: Long, val value: Long) : Packet {
    override fun versionSum(): Long = version
    override fun eval(): Long = value
}
data class Operator(val version: Long, val opType: Long, val operands: List<Packet>) : Packet {
    override fun versionSum(): Long = version + operands.sumOf { it.versionSum() }
    override fun eval(): Long = when (opType) {
        0L -> operands.fold(0L) { a, b -> a + b.eval() }
        1L -> operands.fold(1L) { a, b -> a * b.eval() }
        2L -> operands.minOf { it.eval() }
        3L -> operands.maxOf { it.eval() }
        5L -> if (operands[0].eval() > operands[1].eval()) 1 else 0
        6L -> if (operands[0].eval() < operands[1].eval()) 1 else 0
        7L -> if (operands[0].eval() == operands[1].eval()) 1 else 0
        else -> throw(Exception("Invalid opType $opType"))
    }
}

typealias Bits = MutableList<Char>

fun Bits.pop(n: Int = 1): Long {
    return (0 until n).fold(0) { acc, _ -> acc.shl(1) + (removeFirst().digitToInt()) }
}

fun main() {
    fun hexDigitToBinaryString(digit: Char): String =
        when (digit) {
            in '0'..'1' -> "000${digit.code - '0'.code}"
            in '2'..'3' -> "00${(digit.code - '0'.code).toString(2)}"
            in '4'..'7' -> "0${(digit.code - '0'.code).toString(2)}"
            in '8'..'9' -> (digit.code - '0'.code).toString(2)
            in 'A'..'F' -> (digit.code - 'A'.code + 10).toString(2)
            else -> throw(Exception("Invalid hex digit"))
        }

    fun parse(bits: Bits): Pair<Packet, Bits> {
        fun parseLiteral(bits: Bits, acc: Long = 0L): Long =
            if (bits.pop() == 0L) {
                acc.shl(4) + bits.pop(4)
            } else {
                parseLiteral(bits, acc.shl(4) + bits.pop(4))
            }

        fun parseSubPacketsIn(bits: Bits, acc: List<Packet> = emptyList()): List<Packet> {
            if (bits.isEmpty()) return acc
            val (packet, rest) = parse(bits)
            return parseSubPacketsIn(rest, acc + packet)
        }

        fun parseNSubPackets(nPackets: Long, bits: Bits, acc: List<Packet> = emptyList()): Pair<List<Packet>, Bits> {
            if (nPackets == 0L) return (acc to bits)
            val (packet, rest) = parse(bits)
            return parseNSubPackets(nPackets - 1, rest, acc + packet)
        }

        val version = bits.pop(3)
        val typeId = bits.pop(3)
        if (typeId == 4L) {
            val value = parseLiteral(bits)
            return Literal(version, value) to bits
        }

        return if (bits.pop() == 1L) {
            val nPackets = bits.pop(11)
            val (packets, rest) = parseNSubPackets(nPackets, bits)
            Operator(version, typeId, packets) to rest
        } else {
            val nBits = bits.pop(15).toInt()
            val packets = parseSubPacketsIn(bits.take(nBits).toMutableList())
            Operator(version, typeId, packets) to bits.drop(nBits).toMutableList()
        }
    }

    fun packetFrom(s: String): Packet {
        val bits = s.map { hexDigitToBinaryString(it) }.joinToString("").toMutableList()
        val (packet, _) = parse(bits)
        return packet
    }

    fun part1(input: List<String>): Long = packetFrom(input[0]).versionSum()
    fun part2(input: List<String>): Long = packetFrom(input[0]).eval()

    // test if implementation meets criteria from the description, like:
    val testInput1 = readInput("Day16_test1")
    val testInput2 = readInput("Day16_test2")
    val testInput3 = readInput("Day16_test3")
    val testInput4 = readInput("Day16_test4")

    check(part1(testInput1) == 16L)
    check(part1(testInput2) == 12L)
    check(part1(testInput3) == 23L)
    check(part1(testInput4) == 31L)

    check(part2(listOf("C200B40A82")) == 3L)
    check(part2(listOf("04005AC33890")) == 54L)
    check(part2(listOf("880086C3E88112")) == 7L)
    check(part2(listOf("CE00C43D881120")) == 9L)
    check(part2(listOf("D8005AC2A8F0")) == 1L)
    check(part2(listOf("F600BC2D8F")) == 0L)
    check(part2(listOf("9C005AC2F8F0")) == 0L)
    check(part2(listOf("9C0141080250320F1802104A08")) == 1L)

    val input = readInput("Day16")
    println(part1(input))
    println(part2(input))
}