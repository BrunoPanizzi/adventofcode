package days.day10

import utils.Vec2
import java.io.File

fun run() {
    val input = File("src/main/days/day10/in.txt").readLines()

    println("Part 1 result: ${part1(input)}")
    println("Part 2 result: ${part2(input)}")
}


fun part1(input: List<String>): Int {
    var cycle = 1
    var register = 1
    val cycleHistory = mutableMapOf<Int, Int>()

    for (line in input) {
        if (line.startsWith("addx")) {
            val amount = line.split(" ")[1]
            repeat(2) {
                cycleHistory[cycle] = register
                cycle++
            }
            register += Integer.parseInt(amount)
        } else if (line.startsWith("noop")) {
            cycleHistory[cycle] = register
            cycle++
        }
    }

    val relevantCycles = 20..cycleHistory.size step 40

    val relevant = cycleHistory.filter { (key) -> key in relevantCycles }.map { it.key * it.value }

    return relevant.sum()
}


const val screenWidth = 40
const val screenHeight = 6

fun part2(input: List<String>): Int {
    var cycle = 1
    var register = 1
    val cycleHistory = mutableMapOf<Int, Int>()

    val screen = Array(screenHeight) { Array(screenWidth) { false } }

    for (line in input) {
        if (line.startsWith("addx")) {
            val amount = line.split(" ")[1]
            repeat(2) {
                cycleHistory[cycle] = register
                cycle++
            }
            register += Integer.parseInt(amount)
        } else if (line.startsWith("noop")) {
            cycleHistory[cycle] = register
            cycle++
        }
    }

    // cathode ray tube
    val crt = Vec2(0, 0)

    for ((c, reg) in cycleHistory) {
        // cycle starts at 1 so we remove one here
        crt.x = (c - 1) % screenWidth
        crt.y = (c - 1) / screenWidth

        if (reg - 1 == crt.x || reg == crt.x || reg + 1 == crt.x) {
            screen[crt.y][crt.x] = true
        }

    }

    for (line in screen) {
        for (pixel in line) {
            print(if (pixel) '#' else '.')
        }
        println()
    }

    return -1
}
