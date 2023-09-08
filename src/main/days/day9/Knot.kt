package days.day9

import utils.Vec2


class Knot(private var pos: Vec2 = Vec2(0, 0)) {
    val history: MutableList<Vec2> = mutableListOf()
    var nextKnot: Knot? = null
        set(value) {
            field = if (nextKnot == null && value != null) value else field
        }

    private val distance: Float
        get() = if (nextKnot != null) pos dstTo nextKnot!!.pos else 0f

    val finalKnot: Knot
        get() = nextKnot?.finalKnot ?: this


    fun executeInstruction(instruction: Instruction) {
        repeat(instruction.distance) {
            this.move(instruction.direction)
        }
    }

    private fun move(direction: Direction) {
        if (this.nextKnot == null) {
            pos.plusAssign(direction.vecRepresentation)
            history.add(pos.copy())
            return
        }

        // head relative to the tail
        val headDirection = Direction.fromVec2(pos - nextKnot!!.pos)

        pos.plusAssign(direction.vecRepresentation)

        val nextKnotMovement = if (distance >= 2) headDirection + direction else Direction.NOOP

        history.add(pos.copy())
        nextKnot!!.move(nextKnotMovement)
    }
}

/**
 * A function that creates a chain of knots and returns a pair with the head and tail respectively.
 * */
fun ropeOfSize(n: Int): Pair<Knot, Knot> {
    val head = Knot()
    var prev = head
    var next: Knot

    // -1 because the head counts as one
    repeat(n - 1) {
        next = Knot()
        prev.nextKnot = next
        prev = next
    }

    return Pair(head, head.finalKnot)
}
