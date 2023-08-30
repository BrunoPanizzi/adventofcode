package days

@Throws(IndexOutOfBoundsException::class, NotImplementedError::class)
fun runDay(day: Int) {
    when (day) {
        !in 1..25 -> throw IndexOutOfBoundsException("Day outside valid range.")

        1 -> days.day1.run()
        2 -> days.day2.run()
        3 -> days.day3.run()

        else -> throw NotImplementedError("Day not implemented.")
    }
}
