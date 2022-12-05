#!/usr/bin/kotlinc -script

import java.io.File


val elvesCalories = buildList {
    var currentElvesCalories = 0

    File("./input.txt").readLines().forEach { line ->
        if (line.isBlank()) {
            add(currentElvesCalories)
            currentElvesCalories = 0
        }
        else {
            currentElvesCalories += line.toInt()
        }
    }
}


Pair(
    first = elvesCalories.reduce { currentlyHighestCalories, currentCalories ->
        if (currentlyHighestCalories > currentCalories)
            currentlyHighestCalories
        else
            currentCalories
    }, // 66306

    second = elvesCalories
        .sorted()
        .takeLast(3)
        .reduce(Int::plus), // 195292
)

