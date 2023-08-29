package days.day1

import java.io.File

fun run() {
    val input = File("src/main/days/day1/in.txt").readLines()

    println("Part 1 result: ${part1(input)}")
    println("Part 2 result: ${part2(input)}")
}

// this can be a one-liner, but it would look weird
fun part1(input: List<String>): Int {
    val elves: List<List<String>> = input
        .joinToString("\n") { it.trim() }
        .split("\n\n")
        .map { it.split("\n") }

    val elvesSum: List<Int> = elves.map { elf -> elf.sumOf { Integer.valueOf(it) } }

    return elvesSum.max()
}

fun part2(input: List<String>): Int {
    val lines = input.map { it.trim() }
    val elves = mutableListOf<Int>()

    var sum = 0

    for (line in lines) {
        if (line.isBlank()) {
            elves.add(sum)
            sum = 0
        } else {
            sum += Integer.valueOf(line)
        }
    }

    val top1 = elves.max()
    elves.remove(top1)
    val top2 = elves.max()
    elves.remove(top2)
    val top3 = elves.max()

    return top1 + top2 + top3
}
