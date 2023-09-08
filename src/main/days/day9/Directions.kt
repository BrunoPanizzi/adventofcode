package days.day9

import utils.Vec2


enum class Direction(val vecRepresentation: Vec2) {
    NOOP(Vec2(0, 0)),
    RIGHT(Vec2(1, 0)),
    TOP(Vec2(0, 1)),
    LEFT(Vec2(-1, 0)),
    BOTTOM(Vec2(0, -1)),
    TOP_RIGHT(Vec2(1, 1)),
    TOP_LEFT(Vec2(-1, 1)),
    BOTTOM_LEFT(Vec2(-1, -1)),
    BOTTOM_RIGHT(Vec2(1, -1));


    operator fun plus(other: Direction) =
        fromVec2(vecRepresentation + other.vecRepresentation)

    companion object {
        fun fromStr(char: String) = when (char) {
            "R" -> RIGHT
            "U" -> TOP
            "L" -> LEFT
            "D" -> BOTTOM
            else -> throw Exception("Direction not supported. Received: $char")
        }

        fun fromVec2(vec: Vec2) = when (vec) {
            Vec2(0, 0) -> NOOP
            Vec2(1, 0), Vec2(2, 0) -> RIGHT
            Vec2(-1, 0), Vec2(-2, 0) -> LEFT
            Vec2(0, 1), Vec2(0, 2) -> TOP
            Vec2(0, -1), Vec2(0, -2) -> BOTTOM

            Vec2(1, 1), Vec2(1, 2), Vec2(2, 1), Vec2(2, 2) -> TOP_RIGHT
            Vec2(-1, 1), Vec2(-1, 2), Vec2(-2, 1), Vec2(-2, 2) -> TOP_LEFT
            Vec2(1, -1), Vec2(1, -2), Vec2(2, -1), Vec2(2, -2) -> BOTTOM_RIGHT
            Vec2(-1, -1), Vec2(-1, -2), Vec2(-2, -1), Vec2(-2, -2) -> BOTTOM_LEFT

            else -> throw Exception("Direction not supported. Received: vec(${vec.x}, ${vec.y})")
        }
    }
}
