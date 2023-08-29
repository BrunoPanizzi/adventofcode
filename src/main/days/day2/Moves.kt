package days.day2

interface IMoves {
    val wins: Moves
    val loses: Moves
}

enum class Moves : IMoves {
    ROCK {
        override val wins: Moves get() = SCISSORS
        override val loses: Moves get() = PAPER
    },
    PAPER {
        override val wins: Moves get() = ROCK
        override val loses: Moves get() = SCISSORS
    },
    SCISSORS {
        override val wins: Moves get() = PAPER
        override val loses: Moves get() = ROCK
    };

    fun fight(enemy: Moves): Outcomes {
        return when (enemy) {
            this.loses -> Outcomes.LOSS
            this.wins -> Outcomes.WIN
            else -> Outcomes.DRAW
        }
    }
}
