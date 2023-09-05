package days.day8

import java.io.File

// not the best code, the loops are very repetitive and inefficient
fun run() {
    val input = File("src/main/days/day8/in.txt").readLines()

    println("Part 1 result: ${part1(input)}")
    println("Part 2 result: ${part2(input)}")
}

class OutsideVisibility(
    var right: Boolean = true,
    var top: Boolean = true,
    var left: Boolean = true,
    var bottom: Boolean = true,
) {
    val isVisibleAsInt: Int
        get() = if (right || top || left || bottom) 1 else 0
}

class InsideVisibility(
    var right: Int = 0,
    var top: Int = 0,
    var left: Int = 0,
    var bottom: Int = 0,
) {
    val scenicScore: Int
        get() = right * top * left * bottom
}

fun <T> parseInputToGrid(
    input: List<String>,
    createVisibility: () -> T,
): List<List<Pair<Int, T>>> {
    val treeGrid = input.map { it.split("").filter(String::isNotEmpty) } // filter is needed
    val finalGrid = mutableListOf<MutableList<Pair<Int, T>>>()

    for ((i, row) in treeGrid.withIndex()) {
        finalGrid.add(mutableListOf())

        for ((j, col) in row.withIndex()) {
            finalGrid[i].add(j, Pair(Integer.parseInt(col), createVisibility()))
        }
    }

    return finalGrid
}

fun part1(input: List<String>): Int {
    val finalGrid = parseInputToGrid(input, ::OutsideVisibility)

    val tallestFromTop = MutableList(finalGrid[0].size) { -1 }
    for (row in finalGrid) {
        var tallestTreeInRow = -1

        for ((j, col) in row.withIndex()) {
            // left
            if (col.first > tallestTreeInRow) {
                tallestTreeInRow = col.first
                col.second.left = true
            } else {
                col.second.left = false // it's smaller, so it's not visible from left
            }

            // top
            if (col.first > tallestFromTop[j]) {
                tallestFromTop[j] = col.first
                col.second.top = true
            } else {
                col.second.top = false
            }
        }
    }

    // do everything again but reversed
    val tallestFromBottom = MutableList(finalGrid[0].size) { -1 }
    for (row in finalGrid.reversed()) {
        var tallestTreeInRow = -1

        for ((j, col) in row.reversed().withIndex()) {
            // right
            if (col.first > tallestTreeInRow) {
                tallestTreeInRow = col.first
                col.second.right = true
            } else {
                col.second.right = false
            }

            // bottom
            if (col.first > tallestFromBottom[j]) {
                tallestFromBottom[j] = col.first
                col.second.bottom = true
            } else {
                col.second.bottom = false
            }
        }
    }


    return finalGrid.sumOf { it.sumOf { tree -> tree.second.isVisibleAsInt } }
}

fun createBlankMap(): MutableMap<Int, Int> = mutableMapOf(
    0 to 0, 1 to 0, 2 to 0, 3 to 0, 4 to 0, 5 to 0, 6 to 0, 7 to 0, 8 to 0, 9 to 0
)

fun getVisibilityDistance(tree: Pair<Int, InsideVisibility>, treePos: Int, occurrencesMap: Map<Int, Int>): Int {
    // trees taller or equal to the current tree. Pair(tree size, tree index)
    val tallTrees = mutableListOf<Pair<Int, Int>>()

    for ((key, value) in occurrencesMap) {
        if (key >= tree.first) {
            tallTrees.add(Pair(key, value))
        }
    }

    tallTrees.sortBy { it.second } // sorted by the distance

    return treePos - tallTrees.last().second
}

fun part2(input: List<String>): Int {
    val finalGrid = parseInputToGrid(input, ::InsideVisibility)

    // top and left
    val colSizeOccurrences = MutableList(finalGrid[0].size) { createBlankMap() }
    for ((i, row) in finalGrid.withIndex()) {
        val rowSizesOccurrences = createBlankMap()

        for ((j, col) in row.withIndex()) {
            val treeSize = col.first
            // left
            col.second.left = getVisibilityDistance(col, j, rowSizesOccurrences)
            rowSizesOccurrences[treeSize] = j
            // top
            col.second.top = getVisibilityDistance(col, i, colSizeOccurrences[j])
            colSizeOccurrences[j][treeSize] = i
        }
    }

    // bottom and right
    val bottomColSizeOccurrences = MutableList(finalGrid[0].size) { createBlankMap() }
    for ((i, row) in finalGrid.reversed().withIndex()) {
        val rowSizesOccurrences = createBlankMap()

        for ((j, col) in row.reversed().withIndex()) {
            val treeSize = col.first
            // right
            col.second.right = getVisibilityDistance(col, j, rowSizesOccurrences)
            rowSizesOccurrences[treeSize] = j
            // bottom
            col.second.bottom = getVisibilityDistance(col, i, bottomColSizeOccurrences[j])
            bottomColSizeOccurrences[j][treeSize] = i
        }
    }

    return finalGrid.maxOf { it.maxOf { tree -> tree.second.scenicScore } }
}
