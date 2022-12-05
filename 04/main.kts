#!/usr/bin/kotlinc -script

import java.io.File


fun String.toIntRange(): IntRange =
    this.split("-")
        .let { (start, end) -> start.toInt() .. end.toInt() }


fun count(condition: (range: Pair<IntRange, IntRange>) -> Boolean): Int = File("./input.txt")
    .readLines()
    .map { line ->
        val range = line
            .split(",")
            .let { it[0].toIntRange() to it[1].toIntRange() }

        if (condition(range))
            return@map 1

        return@map 0
    }
    .sum()


operator fun IntRange.contains(other: IntRange): Boolean =
    (this.first <= other.first && other.last <= this.last) || (other.first <= this.first && this.last <= other.last)


infix fun IntRange.overlap(other: IntRange): Boolean =
    this.intersect(other)
        .isNotEmpty()


Pair(
    first = count { range ->
        range.first in range.second
    }, // 584

    second = count { range ->
        range.first overlap range.second
    } // 933
)
