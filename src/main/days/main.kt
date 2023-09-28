package days

@Throws(IndexOutOfBoundsException::class, NotImplementedError::class)
fun runDay(day: Int) {
    when (day) {
        !in 1..25 -> throw IndexOutOfBoundsException("Day outside valid range.")

        1 -> days.day1.run()
        2 -> days.day2.run()
        3 -> days.day3.run()
        4 -> days.day4.run()
        5 -> days.day5.run()
        6 -> days.day6.run()
        7 -> days.day7.run()
        8 -> days.day8.run()
        9 -> days.day9.run()
        10 -> days.day10.run()
        11 -> days.day11.run()
        12 -> days.day12.run()

        else -> throw NotImplementedError("Day not implemented.")
    }
}
