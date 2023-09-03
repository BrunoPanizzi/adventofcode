package days.day6

import java.io.File

fun run() {
    val input = File("src/main/days/day6/in.txt").readText() // this day is just a big string

    println("Part 1 result: ${part1(input)}")
    println("Part 2 result: ${part2(input)}")
}

// iterates over the string until it finds a substring of size N
// in which all chars are unique, then returns the index of the last element
fun findFirstNUniqueCharsIndex(string: String, n: Int): Int {
    for (i in n..<string.length) {
        val packet = string.substring(i - n, i).toSet() // no duplicates

        if (packet.size == n) {
            return i
        }
    }

    return -1
}

fun part1(input: String): Int = findFirstNUniqueCharsIndex(input, 4)

fun part2(input: String): Int = findFirstNUniqueCharsIndex(input, 14)
