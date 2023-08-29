package days.day2

import java.io.File
import java.util.InputMismatchException

fun run() {
    val input = File("src/main/days/day2/in.txt").readLines()

    println("Part 1 result: ${part1(input)}")
    println("Part 2 result: ${part2(input)}")
}

// parsers
@Throws(InputMismatchException::class)
fun parseLineToRoundWithMoves(inp: String): RoundWithMoves {
    val split = inp.split(" ")

    if (split.size != 2) {
        throw InputMismatchException("input size not compatible")
    }

    val opponent = OpponentMoves.valueOf(split[0])
    val me = MyMoves.valueOf(split[1])

    return Pair(opponent, me)
}

@Throws(InputMismatchException::class)
fun parseLineToRoundWithOutcome(inp: String): RoundWithOutcome {
    val split = inp.split(" ")

    if (split.size != 2) {
        throw InputMismatchException("input size not compatible")
    }

    val opponent = OpponentMoves.valueOf(split[0])
    val desiredOutcome = DesiredOutcomes.valueOf(split[1])

    return Pair(opponent, desiredOutcome)
}


// just calculates the total score of the round
fun calculateRoundScore(round: RoundWithMoves): Int {
    val (opponentMove, myMove) = round

    val outcome = myMove.move.fight(opponentMove.move)

    return outcome.value + myMove.value
}

// <RoundWithOutcome> -> <RoundWithMoves>
fun roundWithOutcomeToMoves(round: RoundWithOutcome): RoundWithMoves {
    val (opponentMove, desiredOutcome) = round

    val myMove = when (desiredOutcome.outcome) {
        Outcomes.LOSS -> opponentMove.move.wins // I should lose, so enemy winning move
        Outcomes.WIN -> opponentMove.move.loses
        else -> opponentMove.move
    }

    val myMoveEncoded = MyMoves.fromMove(myMove)

    return Pair(opponentMove, myMoveEncoded)
}

fun part1(input: List<String>): Int {
    val scores = input
        .map { parseLineToRoundWithMoves(it) }
        .map { calculateRoundScore(it) }

    return scores.sum()
}

fun part2(input: List<String>): Int {
    val scores = input
        .map { parseLineToRoundWithOutcome(it) }
        .map { roundWithOutcomeToMoves(it) }
        .map { calculateRoundScore(it) }

    return scores.sum()
}
