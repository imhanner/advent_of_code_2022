#!/usr/bin/kotlinc -script

import java.io.File

// Rock     A X
// Paper    B Y
// Scissors C Z
infix fun String.vs(other: String) = when (this to other) {
    "A" to "Y",
    "B" to "Z",
    "C" to "X" -> 6 // other wins

    "A" to "X",
    "B" to "Y",
    "C" to "Z" -> 3 // draw

    "A" to "Z",
    "B" to "X",
    "C" to "Y" -> 0 // other loses

    else -> TODO("'$this' vs '$other'")
}

fun String.toPoints() = when (this) {
    "X" -> 1
    "Y" -> 2
    "Z" -> 3

    else -> TODO("Points of move '$this'")
}


File("./input.txt")
    .readLines()
    .map { line -> 
        val (moveOppenent, moveAlly) = line.split(" ")

        moveAlly.toPoints() + (moveOppenent vs moveAlly)
    }
    .sum() // 12772
