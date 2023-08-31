package days.day4

import java.io.File
import java.util.InputMismatchException


fun run() {
    val input = File("src/main/days/day4/in.txt").readLines()

    println("Part 1 result: ${part1(input)}")
    println("Part 2 result: ${part2(input)}")
}

fun makeRange(input: String): IntRange {
    val split = input.split("-")

    if (split.size != 2) throw InputMismatchException("Input does not have a start and end to create a range from.")

    val (start, end) = split.map { Integer.valueOf(it) }

    return start..end
}

fun lineToRanges(input: String): Pair<IntRange, IntRange> {
    val sections = input.split(",")

    return Pair(makeRange(sections[0]), makeRange(sections[1]))
}

fun <T : Comparable<T>> rangesFullyIntersect(ranges: Pair<ClosedRange<T>, ClosedRange<T>>): Boolean {
    val (first, second) = ranges
    return (first.start <= second.start && first.endInclusive >= second.endInclusive) || // first start before second and end after
            (second.start <= first.start && second.endInclusive >= first.endInclusive)     // second start before first and end after
}


fun part1(input: List<String>): Int {
    return input.map { lineToRanges(it) }
        .map { rangesFullyIntersect(it) }
        .count { it }
}

fun part2(input: List<String>): Int {
    return input
        .map { lineToRanges(it) }
        .map { (first, second) -> first intersect second }
        .map { it.isNotEmpty() }
        .count { it }
}
