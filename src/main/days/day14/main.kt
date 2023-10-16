package days.day14

import utils.Vec2
import java.io.File

fun run() {
    val input = File("src/main/days/day14/in.txt").readLines()


    println("Part 1 result: ${part1(input)}")
    println("Part 2 result: ${part2(input)}")
}

enum class Types { ROCK, AIR, SAND }

class Cell(val pos: Vec2, var type: Types, var isAtRest: Boolean) {
    override fun toString(): String {
        return "$pos - ${type.name}"
    }
}

fun parseCoordinate(c: String): Vec2 {
    val (x, y) = c.split(",").map { Integer.valueOf(it) }
    return Vec2(x, y)
}

fun parseRocks(line: String): List<Cell> {
    val coordinates = line.split(" -> ")

    val rocks = mutableListOf<Cell>()

    for (i in 0..<coordinates.size - 1) {
        val start = parseCoordinate(coordinates[i])
        val end = parseCoordinate(coordinates[i + 1])

        if (start.x == end.x) {
            val range = if (start.y > end.y) end.y..start.y else start.y..end.y

            for (y in range) {
                val cell = Cell(Vec2(start.x, y), Types.ROCK, true)
                rocks += cell
            }
        } else {
            val range = if (start.x > end.x) end.x..start.x else start.x..end.x

            for (x in range) {
                val cell = Cell(Vec2(x, start.y), Types.ROCK, true)
                rocks += cell
            }
        }
    }

    return rocks.filter { rocks.find { r -> it.pos == r.pos } != null }
}

fun makeMap(rocks: List<Cell>): List<List<Cell>> {
    val min = Vec2(rocks.minOf { it.pos.x }, rocks.minOf { it.pos.y })
    val max = Vec2(rocks.maxOf { it.pos.x }, rocks.maxOf { it.pos.y })

    val map = Array(max.y + 1) { y ->
        Array(max.x - min.x + 1) { x ->
            val pos = Vec2(min.x + x, y)
            val rock = rocks.find { it.pos == pos }

            rock ?: Cell(pos, Types.AIR, true)
        }
    }

    return map.map { it.toList() }.toList()
}

val directions = listOf(Vec2(0, 1), Vec2(-1, 1), Vec2(1, 1))

fun findActiveSand(map: List<List<Cell>>): Vec2? {
    for ((i, line) in map.withIndex()) {
        for ((j, cell) in line.withIndex()) {
            if (!cell.isAtRest && cell.type == Types.SAND) {
                return Vec2(j, i)
            }
        }
    }
    return null
}

val sandStart = Vec2(500, 0)

fun cellOfPos(map: List<List<Cell>>, pos: Vec2): Cell? {
    for (line in map) {
        for (cell in line) {
            if (cell.pos == pos) return cell
        }
    }
    return null
}

fun simulateP1(map: List<List<Cell>>): List<List<Cell>> {
    w@ while (true) {
        val activeSandPos = findActiveSand(map)

        if (activeSandPos != null) {
            val activeSand = map[activeSandPos.y][activeSandPos.x]
            val nextCells = directions.map { it + activeSandPos }

            val (b, bl, br) = nextCells

            if (b.y >= map.size || bl.x < 0 || br.x >= map[0].size) {
                activeSand.type = Types.AIR
                activeSand.isAtRest = true
                break@w
            }

            for (c in nextCells) {
                val cell = map[c.y][c.x]
                if (cell.type == Types.AIR) {
                    cell.type = Types.SAND
                    cell.isAtRest = false
                    activeSand.type = Types.AIR
                    activeSand.isAtRest = true
                    continue@w
                }
            }

            activeSand.isAtRest = true
        } else {
            val prevCell = cellOfPos(map, sandStart)

            assert(prevCell != null)

            prevCell!!.type = Types.SAND
            prevCell.isAtRest = false
        }
    }

    return map
}

fun simulateP2(map: List<List<Cell>>): List<List<Cell>> {
    w@ while (true) {
        val activeSandPos = findActiveSand(map)

        if (activeSandPos != null) {
            val activeSand = map[activeSandPos.y][activeSandPos.x]
            val nextCells = directions.map { it + activeSandPos }

            for (c in nextCells) {
                val cell = map[c.y][c.x]
                if (cell.type == Types.AIR) {
                    cell.type = Types.SAND
                    cell.isAtRest = false
                    activeSand.type = Types.AIR
                    activeSand.isAtRest = true
                    continue@w
                }
            }

            activeSand.isAtRest = true
        } else {
            val prevCell = cellOfPos(map, sandStart)

            assert(prevCell != null)

            if (prevCell!!.type == Types.SAND) {
                break@w
            }

            prevCell.type = Types.SAND
            prevCell.isAtRest = false
        }
    }

    return map
}

fun part1(input: List<String>): Int {
    val rocks = mutableListOf<Cell>()

    for (line in input) {
        rocks += parseRocks(line)
    }

    val map = simulateP1(makeMap(rocks))

    return map.sumOf { it.sumOf { c -> if (c.type == Types.SAND) 1 as Int else 0 } }
}

fun part2(input: List<String>): Int {
    val rocks = mutableListOf<Cell>()

    for (line in input) {
        rocks += parseRocks(line)
    }
    val maxY = rocks.maxOf { it.pos.y }
    rocks += parseRocks("${500 - (maxY + 2)},${maxY + 2} -> ${500 + maxY + 2},${maxY + 2}")

    val map = simulateP2(makeMap(rocks))

    return map.sumOf { it.sumOf { c -> if (c.type == Types.SAND) 1 as Int else 0 } }
}
