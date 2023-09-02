package days.day5

typealias crateMoverFrom<T> = (crates: MutableList<MutableList<String>>) -> CrateMover<T>

interface CrateMover<T> {
    val crates: MutableList<MutableList<T>>
    fun getTopCrates(): List<T>
    fun executeInstruction(instruction: Instruction)
}

class CrateMover9000<T>(override val crates: MutableList<MutableList<T>>) : CrateMover<T> {
    override fun getTopCrates(): List<T> {
        return this.crates.map { it[0] }
    }

    override fun executeInstruction(instruction: Instruction) {
        val fromIndex = instruction.from - 1
        val toIndex = instruction.to - 1

        for (i in 0..<instruction.amount) {
            val popped = this.crates[fromIndex].removeFirst()
            this.crates[toIndex].add(0, popped)
        }
    }
}

fun <T> crateMover9000From(crates: MutableList<MutableList<T>>): CrateMover<T> {
    return CrateMover9000(crates)
}

class CrateMover9001<T>(override val crates: MutableList<MutableList<T>>) : CrateMover<T> {
    override fun getTopCrates(): List<T> {
        return this.crates.map { it[0] }
    }

    override fun executeInstruction(instruction: Instruction) {
        val fromIndex = instruction.from - 1
        val toIndex = instruction.to - 1

        for (i in 0..<instruction.amount) {
            val popped = this.crates[fromIndex].removeFirst()
            this.crates[toIndex].add(i, popped)
        }
    }
}

fun <T> crateMover9001From(crates: MutableList<MutableList<T>>): CrateMover<T> {
    return CrateMover9001(crates)
}
