#!/usr/bin/kotlinc -script

import java.io.File


mutableListOf<Int>()
    .apply {
        var currentElvesCalories = 0

        for (line in File("./input.txt").readLines()) {
            if (line.isBlank()) {
                add(currentElvesCalories)
                currentElvesCalories = 0
            }
            else {
                currentElvesCalories += line.toInt()
            }
        }
    }
    .sorted()
    .takeLast(3)
    .reduce(Int::plus) // 195292
