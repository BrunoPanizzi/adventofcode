package days.day3

import java.io.File

fun run() {
    val input = File("src/main/days/day3/in.txt").readLines()

    println("Part 1 result: ${part1(input)}")
    println("Part 2 result: ${part2(input)}")
}


val lower = 'a'..'z'
val upper = 'A'..'Z'
val letters = lower + upper
val values = 1..54
val lettersWithValues = letters zip values


fun getValueOfLetter(letter: String): Int {
    val pair = lettersWithValues.find { it.first.toString() == letter } ?: return 0
    return pair.second
}


fun splitRucksack(rucksack: String): Pair<String, String> {
    val midpoint: Int = rucksack.length / 2
    return Pair(rucksack.substring(0, midpoint), rucksack.substring(midpoint))
}


fun intersectStrings(strArr: List<String>): String {
    var intersection = strArr.first()

    strArr.forEach { str ->
        // `joinToString` is used here because plain `toString` returns: [a b c d e] and messes stuff up
        intersection = intersectStrings(intersection, str).joinToString("")
    }

    return intersection
}

fun intersectStrings(str1: String, str2: String): Set<Char> {
    return str1.toCharArray() intersect str2.toList().toSet()
}


fun part1(input: List<String>): Int {
    return input
        .map { splitRucksack(it) }
        .map { intersectStrings(it.toList()) }
        .sumOf { getValueOfLetter(it) }
}

fun part2(input: List<String>): Int {
    return input
        .chunked(3)
        .map { intersectStrings(it) }
        .sumOf { getValueOfLetter(it) }
}
