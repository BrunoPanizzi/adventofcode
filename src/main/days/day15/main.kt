package days.day15

import utils.Vec2
import java.io.File

fun run() {
    val input = File("src/main/days/day15/in.txt").readLines()


    println("Part 1 result: ${part1(input)}")
    println("Part 2 result: ${part2(input)}")

}

enum class Type {
    BEACON, SENSOR, NULL;

    override fun toString(): String {
        return when (this) {
            SENSOR -> "S"
            BEACON -> "B"
            NULL -> "."
        }
    }
}

open class Cell(val pos: Vec2, val type: Type, val isCovered: Boolean) {
    override fun toString(): String {
        return when {
            type == Type.SENSOR -> "S"
            type == Type.BEACON -> "B"
            isCovered -> "+"
            !isCovered -> "."
            else -> " "
        }
    }
}

class SensorCell(pos: Vec2, val distanceToBeacon: Int) : Cell(pos, Type.SENSOR, true)


fun extractCoordinates(str: String): Vec2 {
    val x = Regex("(?<=x=)-?\\d+").find(str)
    val y = Regex("(?<=y=)-?\\d+").find(str)

    if (x == null || y == null) {
        throw Exception("x and y not found")
    }

    return Vec2(Integer.parseInt(x.value), Integer.parseInt(y.value))
}

class Map(input: List<String>) {
    private val min = Vec2(0, 0)
    private val max = Vec2(0, 0)
    private val sensors = mutableListOf<SensorCell>()
    private val beaconsPos = mutableListOf<Vec2>()

    init {
        for (line in input) {
            val (sensorStr, beaconStr) = line.split(":")
            val sensorPos = extractCoordinates(sensorStr)
            val beaconPos = extractCoordinates(beaconStr)

            val sensor = SensorCell(
                sensorPos, sensorPos absDstTo beaconPos
            )
            sensors.add(sensor)
            beaconsPos.add(beaconPos)
        }

        min.x = sensors.minOf { it.pos.x - it.distanceToBeacon }
        min.y = sensors.minOf { it.pos.y - it.distanceToBeacon }
        max.x = sensors.maxOf { it.pos.x + it.distanceToBeacon }
        max.y = sensors.maxOf { it.pos.y + it.distanceToBeacon }
    }

    fun getNOfCoveredAtLine(index: Int): Int {
        var count = 0
        for (x in (min.x..max.x)) {
            val pos = Vec2(x, index)

            if (beaconsPos.find { it == pos } != null) continue

            for (sensor in sensors) {
                if (sensor.pos absDstTo pos <= sensor.distanceToBeacon) {
                    count++
                    break
                }
            }
        }

        return count
    }

    fun getFirstNotCoveredCell(min: Vec2, max: Vec2): Vec2? {
        for (y in (min.y..max.y)) {
            var x = min.x
            main@ while (x < max.x) {
                val pos = Vec2(x, y)
                for (sensor in sensors) {
                    if (sensor.pos absDstTo pos <= sensor.distanceToBeacon) {
                        val increment = sensor.distanceToBeacon - (sensor.pos absDstTo pos)
                        x += increment + 1

                        continue@main
                    }
                }
                return pos
            }
        }

        return null
    }
}

fun part1(input: List<String>): Int {
    val map = Map(input)

    val lineToCheck = 2_000_000

    return map.getNOfCoveredAtLine(lineToCheck)
}

fun part2(input: List<String>): Any {
    val map = Map(input)

    val max = 4_000_000

    val beacon = map.getFirstNotCoveredCell(Vec2(0, 0), Vec2(max, max))
    if (beacon == null) {
        println("beacon not found")
        return -1
    }
    return beacon.x.toLong() * max + beacon.y
}
