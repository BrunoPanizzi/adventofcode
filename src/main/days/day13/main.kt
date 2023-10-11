package days.day13

import java.io.File

fun run() {
    val input = File("src/main/days/day13/in.txt").readLines()

    println("Part 1 result: ${part1(input)}")
    println("Part 2 result: ${part2(input)}")
}

enum class Results(val value: Int) { GOOD(1), BAD(-1), NULL(0) }

abstract class MyObj(open val content: Any)

class MyArray(override val content: List<MyObj>) : MyObj(content) {
    override fun toString(): String {
        return content.joinToString(",", "[", "]")
    }
}

class MyInt(override val content: Int) : MyObj(content) {
    override fun toString(): String {
        return content.toString()
    }
}


fun parseLine(line: String): MyArray {
    val content = line.removePrefix("[").removeSuffix("]")

    val split = mutableListOf<String>()
    var openBrackets = 0
    var tmp = ""
    for (char in content) {
        tmp += char
        when (char) {
            '[' -> openBrackets++
            ']' -> openBrackets--
            ',' -> if (openBrackets == 0) {
                split += tmp.removeSuffix(",")
                tmp = ""
            }
        }
    }
    split += tmp

    return MyArray(split.filter { it.isNotEmpty() }.map {
        if (it.startsWith("[")) {
            parseLine(it)
        } else {
            MyInt(Integer.parseInt(it))
        }
    })
}

fun compareObjs(lhs: MyObj, rhs: MyObj): Results {
    if (lhs is MyInt && rhs is MyInt) {
        if (lhs.content < rhs.content) return Results.GOOD
        if (lhs.content > rhs.content) return Results.BAD
        return Results.NULL
    }

    if (lhs is MyArray && rhs is MyArray) {
        for ((first, second) in lhs.content.zip(rhs.content)) {
            val res = compareObjs(first, second)
            if (res == Results.GOOD) return Results.GOOD
            if (res == Results.BAD) return Results.BAD
        }

        if (lhs.content.size < rhs.content.size) return Results.GOOD
        if (lhs.content.size > rhs.content.size) return Results.BAD
        return Results.NULL
    }

    if (lhs is MyInt && rhs is MyArray) {
        return compareObjs(MyArray(listOf(lhs)), rhs)
    }

    if (lhs is MyArray && rhs is MyInt) {
        return compareObjs(lhs, MyArray(listOf(rhs)))
    }

    throw Exception("Shit")
}

fun part1(input: List<String>): Int {
    val packets = input.chunked(3).map { it.filter { str -> str.isNotEmpty() } }

    val parsed = packets.map { it.map { line -> parseLine(line) } }

    val indexes = parsed.mapIndexedNotNull { i, it ->
        if (compareObjs(it[0], it[1]) == Results.GOOD) i + 1 else null
    }

    return indexes.sum()
}

class MyComp : Comparator<MyObj> {
    override fun compare(o1: MyObj?, o2: MyObj?): Int {
        if (o1 == null || o2 == null) {
            return 0
        }

        return compareObjs(o1, o2).value
    }

}

fun part2(input: List<String>): Int {
    val packets = input.filter { str -> str.isNotEmpty() }.toMutableList()
    val dec2 = "[[2]]"
    val dec6 = "[[6]]"
    packets += dec2
    packets += dec6

    val parsed = packets.map { parseLine(it) }

    val sorted = parsed.sortedWith(MyComp()).reversed()

    val dec2index = sorted.indexOfFirst { it.toString() == dec2 } + 1
    val dec6index = sorted.indexOfFirst { it.toString() == dec6 } + 1

    return dec2index * dec6index
}
