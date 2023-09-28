package days.day12

import utils.Leaf
import java.io.File

fun run() {
    val input = File("src/main/days/day12/in.txt").readLines()

    println("Part 1 result: ${part1(input)}")
    println("Part 2 result: ${part2(input)}")
}


class Cell(val y: Int, val x: Int, val height: Int, val isEnd: Boolean = false) {
    var top: Cell? = null
        set(to) {
            field = field ?: to
        }
    var bottom: Cell? = null
        set(to) {
            field = field ?: to
        }
    var left: Cell? = null
        set(to) {
            field = field ?: to
        }
    var right: Cell? = null
        set(to) {
            field = field ?: to
        }

    val neighbors get() = listOfNotNull(top, bottom, left, right)

    override fun toString() = "($x, $y) = $height"
}

fun charToHeight(char: Char): Int {
    return when (char) {
        in 'a'..'z' -> ('a'..'z').indexOf(char) + 1
        'S' -> 1
        'E' -> 26
        else -> throw Exception("Value not supported")

    }
}


/**
 * Parses the input into a network of cells, then returns the starting cell.
 *
 * Might return a pair with the end cell too, we'll see.
 */
fun parseInput(input: List<List<Char>>): Cell {
    var start: Cell? = null

    val cellGrid = input.mapIndexed { y, line ->
        line.mapIndexed { x, it ->
            val cell = Cell(y, x, charToHeight(it), it == 'E')
            if (it == 'S') {
                start = cell
            }
            cell
        }
    }

    for (line in cellGrid) {
        for (cell in line) {
            linkNeighbors(cell, cellGrid)
        }
    }

    return start ?: throw Exception("Start cell not found.")
}

fun linkNeighbors(cell: Cell, input: List<List<Cell>>) {
    // the only way to have no top is to be on the very top of the map
    // so here we add the cell
    if (cell.top == null && cell.y != 0) {
        val top = input[cell.y - 1][cell.x]

        cell.top = top
    }

    //                                       last index
    if (cell.bottom == null && cell.y < input.size - 1) {
        val bottom = input[cell.y + 1][cell.x]

        cell.bottom = bottom
    }

    if (cell.right == null && cell.x < input[0].size - 1) {
        val right = input[cell.y][cell.x + 1]

        cell.right = right
    }

    if (cell.left == null && cell.x != 0) {
        val left = input[cell.y][cell.x - 1]

        cell.left = left
    }
}


fun BFS(leaf: Leaf<Cell>): Leaf<Cell> {
    val visitedNodes = hashSetOf(leaf.value)

    // cells to visit
    val queue = mutableListOf(leaf)

    while (queue.isNotEmpty()) {
        val nextLeaf = queue.removeFirst()
        val nextCell = nextLeaf.value

        if (nextCell.isEnd) {
            return nextLeaf
        }

        for (neighbor in nextCell.neighbors.filter { it.height <= nextCell.height + 1 }) {
            if (visitedNodes.find { it == neighbor } == null) { // not visited
                val newLeaf = Leaf(neighbor, null) // make a new leaf
                nextLeaf.addChild(newLeaf)// with the neighbor as a child of the current leaf

                visitedNodes.add(neighbor)

                queue.add(newLeaf)
            }
        }
    }

    throw Exception("End cell not found.")
}


fun part1(input: List<String>): Int {
    val start = parseInput(input.map { line -> line.map { it } })

    val root = Leaf(start, null)

    val end = BFS(root)

    println("Total path size: ${end.pathToLeaf().size}")
    println("Final node depth: ${end.depth}")

    return end.depth
}

fun part2(input: List<String>): Int {
    return -1
}
