package days.day9

import java.io.File

fun run() {
    val input = File("src/main/days/day9/in.txt").readLines()

    println("Part 1 result: ${part1(input)}")
    println("Part 2 result: ${part2(input)}")
}


class Instruction(val direction: Direction, val distance: Int) {
    companion object {
        fun fromInput(input: String): Instruction {
            val (direction, distance) = input.split(" ")
            return Instruction(Direction.fromStr(direction), Integer.parseInt(distance))
        }
    }
}


fun part1(input: List<String>): Int {
    val (head, tail) = ropeOfSize(2)

    input.forEach {
        val instruction = Instruction.fromInput(it)

        head.executeInstruction(instruction)
    }

    return tail.history.distinct().size
}

fun part2(input: List<String>): Int {
    val (head, tail) = ropeOfSize(10)

    input.forEach {
        val instruction = Instruction.fromInput(it)

        head.executeInstruction(instruction)
    }

    return tail.history.distinct().size
}
