package days.day7

import java.io.File as FilesystemFile

import days.day7.entities.*


fun run() {
    val input = FilesystemFile("src/main/days/day7/in.txt").readLines()

    println("Part 1 result: ${part1(input)}")
    println("Part 2 result: ${part2(input)}")
}


// represents the line that called the command and the lines with the output
typealias command = Pair<String, List<String>>

fun parseInput(input: List<String>): List<command> {
    val parsed: MutableList<Pair<String, MutableList<String>>> = mutableListOf()

    for (s in input) {
        if (s.startsWith("$")) {
            parsed.add(
                Pair(s.replace("$", "").trim(), mutableListOf())
            )
        } else {
            parsed.last().second.add(s)
        }
    }

    return parsed
}

fun createDirectoryTree(input: List<command>): Dir {
    val rootDir = Dir("/")
    val dirStack = mutableListOf(rootDir)
    val currDir = { dirStack.last() }

    for (command in input) {
        val splitCommand = command.first.split(" ")

        val prefix = splitCommand.getOrNull(0)
        val args = splitCommand.getOrNull(1)

        val output = command.second

        when (prefix) {
            "cd" -> when (args) {
                "/" -> dirStack.clear().also { dirStack.add(rootDir) }
                ".." -> dirStack.removeLast()

                is String -> {
                    val child = currDir().findChild(args)

                    if (child is Dir) {
                        dirStack.add(child) // already has the dir so we just move there
                    } else {
                        val dir = Dir(args)
                        currDir().appendChildren(dir)
                        dirStack.add(dir)
                    }
                }
            }


            "ls" -> for (line in output) {
                val (identifier, name) = line.split(" ")

                val newEntity: Entity = if (identifier == "dir") {
                    Dir(name)
                } else {
                    File(name, Integer.parseInt(identifier))  // identifier is just the size
                }

                currDir().appendChildren(newEntity)
            }
        }
    }

    return rootDir
}

fun part1(input: List<String>): Int {
    val rootDir = createDirectoryTree(parseInput(input))

    val smallDirs = rootDir.findSubdirsSmallerThan(100_000)

    return smallDirs.sumOf { it.size }
}

const val DISK_SIZE = 70_000_000
const val UPDATE_SIZE = 30_000_000

fun part2(input: List<String>): Int {
    val rootDir = createDirectoryTree(parseInput(input))

    val availableSize = DISK_SIZE - rootDir.size
    val sizeToFree = UPDATE_SIZE - availableSize

    val possibleDirs = rootDir.findSubdirsBiggerThan(sizeToFree)

    return possibleDirs.minOf { it.size }
}
