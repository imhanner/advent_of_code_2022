#!/usr/bin/kotlinc -script

import java.io.File


fun String.toIntRange(): IntRange =
    this.split("-")
        .let { (start, end) -> start.toInt() .. end.toInt() }


fun count(condition: (IntRange, IntRange) -> Boolean): Int = File("./input.txt")
    .readLines()
    .map { line ->
        val ranges = line
            .split(",")
            .let { it[0].toIntRange() to it[1].toIntRange() }

        if (condition(ranges.first, ranges.second))
            return@map 1

        return@map 0
    }
    .sum()


operator fun IntRange.contains(other: IntRange): Boolean =
    other.first <= this.first && this.last <= other.last


infix fun IntRange.overlapping(other: IntRange): Boolean =
    this.intersect(other).isNotEmpty()


Pair(
    first = count { range1, range2 ->
        range1 in range2 || range2 in range1
    }, // 584

    second = count { range1, range2 ->
        range1 overlapping range2
    } // 933
)
