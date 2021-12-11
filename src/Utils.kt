import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

typealias Grid = ArrayList<ArrayList<Int>>

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("src", "$name.txt").readLines()

fun intsSplitBy(input: String, delim: String): List<Int> = input.split(delim).map { it.toInt() }

/**
 * Converts string to md5 hash.
 */
fun String.md5(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16)
