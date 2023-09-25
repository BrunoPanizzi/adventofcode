package days.day11

import java.io.File
import java.math.BigInteger


fun run() {
    val input = File("src/main/days/day11/in.txt").readLines()

    println("Part 1 result: ${part1(input)}")
    println("Part 2 result: ${part2(input)}")
}


/**
 * finds the first integer from a string
 */
fun findInt(string: String): Int = Integer.parseInt(Regex("\\d+").find(string)?.value)

// really not the best idea, but we need to keep track of the
// numbers that we need to take mod of
val divisibilityChecks = mutableListOf<Int>()

fun parseFile(input: List<String>): List<Monkey> {
    val rawMonkeys = input.chunked(7)
    val monkeys = mutableListOf<Monkey>()


    for (rawMonkey in rawMonkeys) {
        val test = Integer.parseInt(Regex("\\d+").find(rawMonkey[3])?.value)
        divisibilityChecks.add(test)
    }


    for ((id, rawMonkey) in rawMonkeys.withIndex()) {
        val items = rawMonkey[1].replace("Starting items:", "").split(",").map { Integer.parseInt(it.trim()) }

        val opType = if (Regex("old").findAll(rawMonkey[2]).toList().size == 2) {
            OpTypes.SQUARE
        } else {
            OpTypes.fromStr(Regex("[*+]").find(rawMonkey[2])?.value ?: "")
        }
        val opAmount = if (opType != OpTypes.SQUARE) findInt(rawMonkey[2]) else 0
        val operation = Operation(opType, opAmount)

        val test = findInt(rawMonkey[3])

        val idWhenTrue = findInt(rawMonkey[4])
        val idWhenFalse = findInt(rawMonkey[5])

        monkeys.add(
            Monkey(
                id, items.map { Item(it) }.toMutableList(), operation, test, idWhenTrue, idWhenFalse
            )
        )
        divisibilityChecks.add(test)
    }

    return monkeys
}


enum class OpTypes {
    TIMES, PLUS, SQUARE;

    companion object {
        fun fromStr(string: String): OpTypes {
            return when (string) {
                "*" -> TIMES
                "+" -> PLUS
                else -> throw IllegalArgumentException("value not supported")
            }
        }
    }
}

class Item(private var worryLevel: Int) {
    /**
     * Stores the remainder of the division with each item in `divisibilityChecks`.
     */
    private val divisibilityMap = divisibilityChecks.associateWith { worryLevel % it }.toMutableMap()
    private var w = worryLevel.toBigInteger()
        set(value) {
            val asStr = value.toString()
            field = if (asStr.length > 45) {
                BigInteger(asStr.substring(asStr.length - 40))
            } else {
                value
            }
        }

    operator fun plusAssign(other: Int) {
        // updates the divisibility
        for ((key, value) in divisibilityMap) {
            divisibilityMap[key] = (value + other) % key
        }
        w += other.toBigInteger()
    }

    operator fun timesAssign(other: Int) {
        for ((key, value) in divisibilityMap) {
            divisibilityMap[key] = (value * other) % key
        }
        w *= other.toBigInteger()
    }

    operator fun divAssign(other: Int) {
        // we need to divide first and then take the mod, not use a fancy mod division alg
        for ((key) in divisibilityMap) {
            val div = w / other.toBigInteger() // this is fine but not really

            divisibilityMap[key] = (div % key.toBigInteger()).toInt()
        }
        w /= other.toBigInteger()
    }

    operator fun rem(other: Int): Int =
        divisibilityMap[other] ?: throw Exception("Number $other is not in global `divisibilityChecks`")

    fun squareInPlace() {
        for ((k, v) in divisibilityMap) {
            divisibilityMap[k] = (v * v) % k
        }

        w *= w
    }

    override fun toString() = w.toString()
}


class Operation(val type: OpTypes, val amount: Int)

class Monkey(
    val id: Int,
    val items: MutableList<Item>,
    val operation: Operation,

    val testValue: Int,
    val monkeyWhenTrue: Int,
    val monkeyWhenFalse: Int,
) {
    var inspections = 0
}


fun part1(input: List<String>): Int {
    val monkeys = parseFile(input).toMutableList()

    repeat(20) {
        for (monkey in monkeys) {
            while (monkey.items.size > 0) {
                val item = monkey.items.removeAt(0)
                monkey.inspections++

                when (monkey.operation.type) {
                    OpTypes.SQUARE -> item.squareInPlace()
                    OpTypes.TIMES -> item *= monkey.operation.amount
                    OpTypes.PLUS -> item += monkey.operation.amount
                }

                item /= 3

                val monkeyToThrow = if (item % monkey.testValue == 0) {
                    monkeys.find { mnk -> mnk.id == monkey.monkeyWhenTrue }
                } else {
                    monkeys.find { mnk -> mnk.id == monkey.monkeyWhenFalse }
                }


                if (monkeyToThrow == null) {
                    throw Exception("Monkey to throw not found")
                }

                monkeyToThrow.items.add(item)
            }
        }
    }

    monkeys.sortBy { it.inspections }
    monkeys.reverse()
    val (first, second) = monkeys

    return first.inspections * second.inspections
}


fun part2(input: List<String>): BigInteger {
    val monkeys = parseFile(input).toMutableList()

    repeat(10000) {
        for (monkey in monkeys) {
            while (monkey.items.size > 0) {
                val item = monkey.items.removeAt(0)
                monkey.inspections++


                when (monkey.operation.type) {
                    OpTypes.SQUARE -> item.squareInPlace()
                    OpTypes.TIMES -> item *= monkey.operation.amount
                    OpTypes.PLUS -> item += monkey.operation.amount
                }

                val monkeyToThrow = if (item % monkey.testValue == 0) {
                    monkeys.find { it.id == monkey.monkeyWhenTrue }
                } else {
                    monkeys.find { it.id == monkey.monkeyWhenFalse }
                }

                if (monkeyToThrow == null) {
                    throw Exception("Monkey to throw not found")
                }

                monkeyToThrow.items.add(item)
            }
        }
    }

    monkeys.sortBy { it.inspections }
    monkeys.reverse()
    val (first, second) = monkeys

    return first.inspections.toBigInteger() * second.inspections.toBigInteger()
}
