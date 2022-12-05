#!/usr/bin/kotlinc -script

import java.io.File


fun priorityOf(item: Char): Int = item.code + when (item) {
    in 'a' .. 'z' -> -96
    in 'A' .. 'Z' -> -38

    else -> TODO("Offset of item '$this'")
}


val rucksacks = File("./input.txt").readLines()

Pair(
    first = rucksacks.map { line ->
        val compartment = line.run {
            val compartmentSize = length / 2

            take(compartmentSize) to takeLast(compartmentSize)
        }

        for (item in compartment.first) {
            compartment
                .second
                .firstOrNull { item == it }
                ?.let { return@map priorityOf(it) }
        }

        TODO("No item in both compartments")
    }
    .sum(), // 7727

    second = (0 .. rucksacks.lastIndex / 3).sumOf { groupIndex ->
        val groupOffset = groupIndex * 3

        val elve = Triple(
            rucksacks[0 + groupOffset],
            rucksacks[1 + groupOffset],
            rucksacks[2 + groupOffset],
        )

        val commonBadgeItem = elve.first.toSet()
            .intersect(elve.second.toSet())
            .intersect(elve.third.toSet())
            .also { require(it.size == 1) }
            .first()

        priorityOf(commonBadgeItem)
    }, // 2609
)
