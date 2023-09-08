package utils

import kotlin.math.pow
import kotlin.math.sqrt

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

    private infix fun dst2To(to: Vec2) = (x - to.x).toFloat().pow(2) + (y - to.y).toFloat().pow(2)
    infix fun dstTo(to: Vec2) = sqrt(this dst2To to)
}
