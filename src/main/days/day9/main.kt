package days.day9

import java.io.File
import kotlin.math.pow
import kotlin.math.sqrt

fun run() {
    val input = File("src/main/days/day9/in.txt").readLines()

    println("Part 1 result: ${part1(input)}")
    println("Part 2 result: ${part2(input)}")
}

data class Vec2(
    var x: Int,
    var y: Int,
) {
    operator fun plus(vec: Vec2) = Vec2(x + vec.x, y + vec.y)
    operator fun minus(vec: Vec2) = Vec2(x - vec.x, y - vec.y)
    operator fun plusAssign(vec: Vec2) {
        x += vec.x
        y += vec.y
    }

    operator fun minusAssign(vec: Vec2) {
        x -= vec.x
        y -= vec.y
    }

    override fun equals(other: Any?) = other is Vec2 && other.x == x && other.y == y
    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        return result
    }

    /**
     * Copy the values of the second vector into this vector
     **/
    infix fun makeEqual(to: Vec2) {
        x = to.x
        y = to.y
    }

    private infix fun dst2To(to: Vec2) = (x - to.x).toFloat().pow(2) + (y - to.y).toFloat().pow(2)
    infix fun dstTo(to: Vec2) = sqrt(this dst2To to)
}


class Knot(var pos: Vec2) {
    val distance: Float
        //    idk if I'm dumb or kotlin is because this ⬇⬇  ️assertion is needed
        get() = if (nextKnot != null) pos dstTo nextKnot!!.pos else 0f

    var nextKnot: Knot? = null
        set(value) {
            field = if (nextKnot == null && value != null) value else field
        }

    fun executeInstruction(instruction: Instruction, callback: (Pair<Knot, Knot?>) -> Unit) {
        repeat(instruction.distance) {
            this.move(instruction.direction)
            callback(Pair(this, nextKnot))
        }
    }

    private fun move(direction: Direction) {
        val prevPos = pos.copy()

        pos.plusAssign(direction.vecRepresentation)

        // so this is only good for two knots, if we have more stuff goes doo-doo
        if (nextKnot != null && distance >= 2) {
            nextKnot!!.pos makeEqual prevPos
        }
    }
}


class Rope {
    private val head = Knot(Vec2(0, 0))
    private val tail = Knot(Vec2(0, 0))
    val tailHistory = mutableSetOf<Vec2>()

    init {
        head.nextKnot = tail
    }

    fun executeInstruction(instruction: Instruction) {
        this.head.executeInstruction(instruction) { (_, tail) -> tail?.pos?.copy()?.let { tailHistory.add(it) } }
    }
}

enum class Direction(val vecRepresentation: Vec2) {
    RIGHT(Vec2(1, 0)), TOP(Vec2(0, 1)), LEFT(Vec2(-1, 0)), BOTTOM(Vec2(0, -1));

    companion object {
        fun parse(char: String) = when (char) {
            "R" -> RIGHT
            "U" -> TOP
            "L" -> LEFT
            "D" -> BOTTOM
            else -> throw Exception("Direction not supported. Received: $char")
        }
    }
}

class Instruction(val direction: Direction, val distance: Int)

fun part1(input: List<String>): Int {
    val rope = Rope()

    input.forEach {
        val (direction, distance) = it.split(" ")

        val instruction = Instruction(Direction.parse(direction), Integer.parseInt(distance))

        rope.executeInstruction(instruction)
    }

    return rope.tailHistory.size
}

fun part2(input: List<String>): Int {
    return -1
}
