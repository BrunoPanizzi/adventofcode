package days.day2


typealias RoundWithMoves = Pair<OpponentMoves, MyMoves>
typealias RoundWithOutcome = Pair<OpponentMoves, DesiredOutcomes>

enum class OpponentMoves(val move: Moves) {
    A(Moves.ROCK), B(Moves.PAPER), C(Moves.SCISSORS)
}

enum class MyMoves(val move: Moves, val value: Int) {
    X(Moves.ROCK, 1), Y(Moves.PAPER, 2), Z(Moves.SCISSORS, 3);

    companion object {
        fun fromMove(move: Moves): MyMoves {
            return when (move) {
                Moves.ROCK -> X
                Moves.PAPER -> Y
                Moves.SCISSORS -> Z
            }
        }
    }
}

enum class Outcomes(val value: Int) {
    WIN(6), DRAW(3), LOSS(0)
}

enum class DesiredOutcomes(val outcome: Outcomes) {
    X(Outcomes.LOSS), Y(Outcomes.DRAW), Z(Outcomes.WIN)
}
