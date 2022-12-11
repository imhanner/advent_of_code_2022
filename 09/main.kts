#!/usr/bin/kotlinc -script

import java.io.File
import kotlin.math.abs
import kotlin.math.max


typealias Move = Pair<Char, Int>
val Move.direction get() = first
val Move.steps get() = second


typealias Position = Pair<Int, Int>
val Position.x get() = first
val Position.y get() = second
operator fun Position.plus(other: Position): Position = (this.x + other.x) to (this.y + other.y)
infix fun Position.distanceTo(other: Position) = max(abs(this.x - other.x), abs(this.y - other.y))


class Rope(length: Int, val startingPosition: Position = 0 to 0) {

    val memoryOfTailPositions = mutableSetOf<Position>(startingPosition)

    val positions = MutableList(length) { startingPosition }

    private fun moveHead(positionDelta: Position) {
        positions[0] += positionDelta

        for ((index1, index2) in (0 until positions.size - 1).zip(1 until positions.size)) {

            val currentHead = positions[index1]
            val currentTail = positions[index2]

            if (currentHead distanceTo currentTail > 1) {
                positions[index2] += (currentHead.x - currentTail.x).coerceIn(-1, 1) to (currentHead.y - currentTail.y).coerceIn(-1, 1)
            }

            if (index2 == positions.lastIndex) {
                memoryOfTailPositions.add(positions[index2])
            }
        }

    }

    fun performMove(move: Move) {
        repeat(move.steps) {
            when (move.direction) {
                'U' -> moveHead(0 to -1)
                'D' -> moveHead(0 to 1)
                'R' -> moveHead(1 to 0)
                'L' -> moveHead(-1 to 0)
                else -> TODO("direction ${move.direction}")
            }
        }
    }
}

fun uniqueTailPositionsCountAfter(ropeLength: Int, moves: List<Move>) = Rope(ropeLength)
    .apply { moves.forEach(::performMove) }
    .memoryOfTailPositions
    .size


//region examples of part 1
listOf<Move>(
    'R' to 4,
    'U' to 4,
    'L' to 3,
    'D' to 1,
    'R' to 4,
    'D' to 1,
    'L' to 5,
    'R' to 2,
)
    .let { moves -> uniqueTailPositionsCountAfter(2, moves) }
    .also { require(it == 13) }
//endregion

//region examples of part 2
listOf<Move>(
    'R' to 5,
    'U' to 8,
    'L' to 8,
    'D' to 3,
    'R' to 17,
    'D' to 10,
    'L' to 25,
    'U' to 20,
)
    .let { moves -> uniqueTailPositionsCountAfter(10, moves) }
    .also { require(it == 36) }
//endregion


val moves: List<Move> = File("./input.txt").readLines().map {
    val (direction, steps) = it.split(" ")
    direction.first() to steps.toInt()
}

Pair(
    first = uniqueTailPositionsCountAfter(2, moves), // 6037

    second = uniqueTailPositionsCountAfter(10, moves), // 2485
)
