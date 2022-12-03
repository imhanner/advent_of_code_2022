#!/usr/bin/kotlinc -script

import java.io.File


fun priorityOf(item: Char): Int = item.code + when (item) {
    in 'a' .. 'z' -> -96
    in 'A' .. 'Z' -> -38

    else -> TODO("Offset of item '$this'")
}


File("./input.txt")
    .readLines()
    .map { line ->
        val compartment = line.run {
            val singleCompartmentSize = length / 2

            take(singleCompartmentSize) to takeLast(singleCompartmentSize)
        }

        for (item in compartment.first) {
            compartment
                .second
                .firstOrNull { item == it }
                ?.let {
                    // println("$compartment both having '$it'")
                    return@map priorityOf(it)
                }
        }

        TODO("No item in both compartments")
    }
    .onEach { require(it in 1 .. 52) }
    .sum() // 7727
