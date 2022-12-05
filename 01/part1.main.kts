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
    .reduce { currentlyHighestCalories, currentCalories ->
        if (currentlyHighestCalories > currentCalories)
            currentlyHighestCalories
        else
            currentCalories
    } // 66306
