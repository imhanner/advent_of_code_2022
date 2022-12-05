#!/usr/bin/kotlinc -script

@file:Import("common.kt")


val (stacks: Stacks, moves: Moves) = parseInput()

stacks.perform(moves) { move ->
    repeat(move.cratesCount) {
        stacks[move.fromStackIndex]
            .pop()
            .run(stacks[move.toStackIndex]::push)
    }
}

stacks.topCrates() // SHQWSRBDL
