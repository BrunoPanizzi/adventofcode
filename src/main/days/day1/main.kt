package days.day1

import java.io.File

fun run() {
    val filePath = System.getProperty("user.dir") + "/src/main/days/day1/in.txt"
    val input = File(filePath).readText()

    println("Part 1 result: ${part1(input)}")
    println("Part 2 result: ${part2(input)}")
}

// TODO: make this more elegant, using maps and stuff
fun part1(input: String): Int {
    val lines = input.split("\n").map { it.trim()}

    var sum = 0
    var max = 0

    for (line in lines) {
        if (line.isBlank()) {
            max = if (sum > max) sum else max
            sum = 0
        } else {
            sum += Integer.valueOf(line)
        }
    }

    return max
}

fun part2(input: String): Int {
    val lines = input.split("\n").map { it.trim()}
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
