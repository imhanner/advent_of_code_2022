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
    first = elvesCalories
        .max(), // 66306

    second = elvesCalories
        .sorted()
        .takeLast(3)
        .sum(), // 195292
)

