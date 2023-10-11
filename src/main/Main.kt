fun main(args: Array<String>) {
    try {
        val day = args[0].toInt()
        println("Running day $day")
        days.runDay(day)
    } catch (e: Exception) {
        println("Day to run was not specified.")
    }
}
