#!/usr/bin/kotlinc -script

import java.io.File


//region part 1
@JvmInline value class Move(private val move: String) {
    // Rock     A X
    // Paper    B Y
    // Scissors C Z
    infix fun vs(otherMove: String) = when (otherMove to move) {
        "A" to "Y", "B" to "Z", "C" to "X" -> 6 // other wins
        "A" to "X", "B" to "Y", "C" to "Z" -> 3 // draw
        "A" to "Z", "B" to "X", "C" to "Y" -> 0 // other loses
        else -> TODO("'$move' vs '$otherMove'")
    }

    fun points() = when (move) {
        "X" -> 1
        "Y" -> 2
        "Z" -> 3
        else -> TODO("Points of move '$move'")
    }
}
//endregion

//region part 2
@JvmInline value class Outcome(private val outcome: String) {
    fun points() = when (outcome) {
        "X" -> 0 // loss
        "Y" -> 3 // draw
        "Z" -> 6 // win
        else -> TODO("Points of move '$outcome'")
    }

    // Rock     A
    // Paper    B
    // Scissors C
    fun moveAllyPoints(otherMove: String) = when (otherMove to outcome) {
        "B" to "X", "A" to "Y", "C" to "Z" -> 1 // ally move was Rock
        "C" to "X", "B" to "Y", "A" to "Z" -> 2 // ally move was Paper
        "A" to "X", "C" to "Y", "B" to "Z" -> 3 // ally move was Scissors
        else -> TODO("Move and points of ally for other move '$otherMove' with outcome '$outcome'")
    }
}
//endregion

fun sumPoints(calculatePoints: (tokens: List<String>) -> Int) =
    File("./input.txt")
        .readLines()
        .map { line -> calculatePoints(line.split(" ")) }
        .sum()


Pair(
    first = sumPoints { (moveOpponent, moveAlly) ->
        Move(moveAlly).run { points() + (this vs moveOpponent) }
    }, // 12772

    second = sumPoints { (moveOpponent, outcome) ->
        Outcome(outcome).run { moveAllyPoints(moveOpponent) + points() }
    }, // 11618
)
