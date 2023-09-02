package days.day5

class Instruction(val amount: Int, val from: Int, val to: Int)

fun instructionFromLine(line: String): Instruction {
    val (amount, from, to) = line
        .split(Regex("move | from | to "))
        .filter { it.isNotEmpty() }
        .map { Integer.parseInt(it) }

    return Instruction(amount, from, to)
}
