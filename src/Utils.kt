import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

fun printP1(s: Any) = println("Part1: $s")
fun printP2(s: Any) = println("Part2: $s")

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("src", "$name.txt")
    .readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')
