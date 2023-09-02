package days.day5

import java.io.File

fun <T> parseFileToMoverAndInstructions(
    file: List<String>,
    createMover: crateMoverFrom<T>
): Pair<CrateMover<T>, List<Instruction>> {
    val division = file.indexOf("")
    val (rawCrates, rawInstructions) = Pair(
        file.subList(0, division).toMutableList(), file.subList(division + 1, file.size)
    )

    rawCrates.removeLast() // just contains indexes

    val chunkedCrates = rawCrates.map { it.chunked(4).map { chunk -> chunk.trim() } }
    val lines = chunkedCrates.size
    val cols = chunkedCrates.maxOf { it.size }

    val finalCrates = mutableListOf<MutableList<String>>()

    for (i in 0..<cols) {
        finalCrates.add(mutableListOf())
        for (j in 0..<lines) {
            val elementToAdd: String = try {
                chunkedCrates[j][i]
            } catch (e: IndexOutOfBoundsException) {
                ""
            }
            finalCrates[i].add(elementToAdd)
        }
    }

    finalCrates.forEach { it.removeIf { item -> item.isEmpty() } } // removes blanks

    val crateMover = createMover(finalCrates)
    val instructions = rawInstructions.map { instructionFromLine(it) }

    return Pair(crateMover, instructions)
}

fun run() {
    val input = File("src/main/days/day5/in.txt").readLines()

    println("Part 1 result: ${part1(input)}")
    println("Part 2 result: ${part2(input)}")
}

// funny how part 1 and 2 are the same, except for the crate mover version
fun part1(input: List<String>): String {
    val (crateMover, instructions) = parseFileToMoverAndInstructions(input, ::crateMover9000From)

    for (instruction in instructions) {
        crateMover.executeInstruction(instruction)
    }

    return crateMover
        .getTopCrates()
        .joinToString("")
        { it.replace(Regex("[\\[\\]]"), "") }
}

fun part2(input: List<String>): String {
    val (crateMover, instructions) = parseFileToMoverAndInstructions(input, ::crateMover9001From)

    for (instruction in instructions) {
        crateMover.executeInstruction(instruction)
    }

    return crateMover
        .getTopCrates()
        .joinToString("")
        { it.replace(Regex("[\\[\\]]"), "") }
}
