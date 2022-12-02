#!/usr/bin/kotlinc -script

import java.io.File

// loss X
// draw Y
// win  Z
fun String.toPoints() = when (this) {
    "X" -> 0
    "Y" -> 3
    "Z" -> 6

    else -> TODO("Points of move '$this'")
}

// Rock     A
// Paper    B
// Scissors C
fun moveAllyPoints(otherMove:String, outcome: String) = when (otherMove to outcome) {
    "B" to "X",
    "A" to "Y",
    "C" to "Z" -> 1 // ally move was Rock

    "C" to "X",
    "B" to "Y",
    "A" to "Z" -> 2 // ally move was Paper

    "A" to "X",
    "C" to "Y",
    "B" to "Z" -> 3 // ally move was Scissors
    
    else -> TODO("Move and points of ally for other move '$otherMove' with outcome '$outcome'")
}


File("./input.txt")
    .readLines()
    .map { line -> 
        val (moveOppenent, outcome) = line.split(" ")

        moveAllyPoints(moveOppenent, outcome) + outcome.toPoints()
    }
    .sum() // 11618
