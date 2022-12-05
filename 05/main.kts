#!/usr/bin/kotlinc -script

import java.io.File
import java.util.Stack


fun parseInput(): Pair<Stacks, Moves> = File("./input.txt")
    .readLines()
    .let { lines ->
        lines.take(8).toStacks() to lines.subList(10, lines.size).map(::Move)
    }


//region Stacks
typealias Stacks = List<Stack<Char>>

fun Stacks.perform(moves: Moves, shouldPrint: Boolean = false, performMoveBody: (Move) -> Unit) {
    if (shouldPrint) this.print()

    for (move in moves) {
        if (shouldPrint) move.print()

        performMoveBody(move)

        if (shouldPrint) this.print()
    }
}

fun Stacks.topCrates() = joinToString("") { "${it.peek()}" }

/**
 * @receiver a top-down list of levels with possibly crates
 * @return list of stacks of crates
 */
fun List<String>.toStacks(): Stacks {
    val bottomUp = this.reversed()

    return buildList<Stack<Char>> {
        repeat(9) { i ->
            // create each stack
            add(Stack<Char>())

            // fill each stack
            for (currentLevel in bottomUp) {
                currentLevel[4 * i + 1]
                    .takeIf { crate -> crate in 'A' .. 'Z' }
                    ?.also { crate -> this[i].push(crate) }
            }
        }
    }
}

fun Stacks.print() {
    listOf(
        (1 .. size).joinToString(" ") { " $it " },
        *(0 until (maxOfOrNull { it.size } ?: 0))
            .map { i -> joinToString(" ") { stack -> stack.getOrNull(i)?.let { "[$it]" } ?: "   " } }
            .toTypedArray(),
    )
        .reversed()
        .joinToString("\n")
        .also(::println)
}
//endregion

//region Moves
typealias Moves = List<Move>

data class Move private constructor(
    val cratesCount: Int,
    val fromStackIndex: Int,
    val toStackIndex: Int,
) {
    constructor(statement: String) : this(statement(1), statement(3) - 1, statement(5) - 1)

    fun print() = "move $cratesCount from ${fromStackIndex + 1} to ${toStackIndex + 1}"

    companion object {
        private operator fun String.invoke(index: Int) =
            this.split(" ")[index].toInt()
    }
}
//endregion


Pair(
    first = parseInput().let { (stacks: Stacks, moves: Moves) ->

        stacks.perform(moves) { move ->
            repeat(move.cratesCount) {
                stacks[move.fromStackIndex]
                    .pop()
                    .run(stacks[move.toStackIndex]::push)
            }
        }

        stacks.topCrates() // SHQWSRBDL
    },

    second = parseInput().let { (stacks: Stacks, moves: Moves) ->

        stacks.perform(moves) { move ->
            val crates = buildList<Char> {
                repeat(move.cratesCount) {
                    stacks[move.fromStackIndex]
                        .pop()
                        .also { crate -> add(0, crate) }
                }
            }

            stacks[move.toStackIndex].addAll(crates)
        }

        stacks.topCrates() // CDTQZHBRS
    },
)
