#!/usr/bin/kotlinc -script

import java.io.File
import java.util.Stack


val shouldPrint = false


//region helpers to parse input, pretty print
fun List<String>.toStacks(): List<Stack<Char>> {
    val bottomUp = this.reversed()

    val stacks = mutableListOf<Stack<Char>>()

    repeat(9) { i ->

        stacks.add(Stack<Char>())

        for (level in bottomUp) {
            level[4 * i + 1]
                .takeIf { it in 'A' .. 'Z' }
                ?.also { crate -> stacks[i].push(crate) }
        }
    }

    return stacks
}

fun List<Stack<Char>>.toPrettyString() =
    listOf(
        (1 .. size).joinToString(" ") { " $it " },
        *(0 until (maxOfOrNull { it.size } ?: 0))
            .map { i -> joinToString(" ") { stack -> stack.getOrNull(i)?.let { "[$it]" } ?: "   " } }
            .toTypedArray(),
    )
    .reversed()
    .joinToString("\n")


data class CargoMove(
    val cratesCount: Int,
    val fromStackIndex: Int,
    val toStackIndex: Int,
) {
    constructor(statement: String) : this(statement(1), statement(3) - 1, statement(5) - 1)

    fun toPrettyString() = "move $cratesCount from ${fromStackIndex + 1} to ${toStackIndex + 1}"

    companion object {
        private operator fun String.invoke(index: Int) =
            this.split(" ")[index].toInt()
    }
}
//endregion


// parse input
val (stacks, moves) = File("./input.txt")
    .readLines()
    .let { lines ->
        lines.take(8).toStacks() to lines.subList(10, lines.size).map(::CargoMove)
    }

//region perform moves
if (shouldPrint) println(stacks.toPrettyString())

for (move in moves) {
    if (shouldPrint) println(move.toPrettyString())

    repeat(move.cratesCount) {
        stacks[move.fromStackIndex]
            .pop()
            .run(stacks[move.toStackIndex]::push)
    }

    if (shouldPrint) println(stacks.toPrettyString())
}
//endregion

// result
stacks.joinToString("") { "${it.peek()}" } // SHQWSRBDL
