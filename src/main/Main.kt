import kotlin.system.exitProcess

fun main(args: Array<String>) {
    val day: Int
    try {
        day = args[0].toInt()
    } catch (e: Exception) {
        println("Day to run was not specified.")
        exitProcess(1)
    }

    println("Running day $day")
    days.runDay(day)
}
