#!/usr/bin/kotlinc -script

@file:Import("common.kt")


val (stacks: Stacks, moves: Moves) = parseInput()

stacks.perform(moves) { move ->
    val crates = mutableListOf<Char>()

    repeat(move.cratesCount) {
        stacks[move.fromStackIndex]
            .pop()
            .run(crates::add)
    }

    stacks[move.toStackIndex].addAll(crates.reversed())
}

stacks.topCrates() // CDTQZHBRS
